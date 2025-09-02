package com.pizzaman.keyboardmaestrotv.network

import com.pizzaman.keyboardmaestrotv.models.ConnectionSettings
import com.pizzaman.keyboardmaestrotv.models.KeyboardMaestroScript
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

/**
 * Service for communicating with Keyboard Maestro on Mac
 */
class KeyboardMaestroApiService {
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()
    
    /**
     * Test connection to the Mac
     */
    suspend fun testConnection(settings: ConnectionSettings): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(settings.getBaseUrl())
                .get()
                .build()
            
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string() ?: ""
                    if (body.contains("Keyboard Maestro Server")) {
                        Result.success(true)
                    } else {
                        Result.failure(Exception("Unexpected response from server"))
                    }
                } else {
                    Result.failure(Exception("Connection failed: ${response.code}"))
                }
            }
        } catch (e: ConnectException) {
            Result.failure(Exception("Cannot connect to Mac at ${settings.macIpAddress}:${settings.macPort}"))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timeout"))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get list of available scripts from Keyboard Maestro
     */
    suspend fun getScripts(settings: ConnectionSettings): Result<List<KeyboardMaestroScript>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(settings.getBaseUrl())
                .get()
                .build()
            
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val html = response.body?.string() ?: ""
                    val scripts = parseScriptsFromHtml(html)
                    Result.success(scripts)
                } else {
                    Result.failure(Exception("Failed to get scripts: ${response.code}"))
                }
            }
        } catch (e: ConnectException) {
            Result.failure(Exception("Cannot connect to Mac"))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Request timeout"))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Execute a script on the Mac
     */
    suspend fun executeScript(settings: ConnectionSettings, scriptId: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val url = "${settings.getBaseUrl()}/action.html?macro=$scriptId"
            val request = Request.Builder()
                .url(url)
                .get()
                .build()
            
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("Failed to execute script: ${response.code}"))
                }
            }
        } catch (e: ConnectException) {
            Result.failure(Exception("Cannot connect to Mac"))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Request timeout"))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Parse HTML response to extract Keyboard Maestro scripts
     */
    private fun parseScriptsFromHtml(html: String): List<KeyboardMaestroScript> {
        val scripts = mutableListOf<KeyboardMaestroScript>()
        
        // Parse HTML to extract script options
        val optionRegex = """<option label="([^"]+)" value="([^"]+)">([^<]+)</option>""".toRegex()
        val optgroupRegex = """<optgroup label="([^"]+)">""".toRegex()
        
        var currentCategory = "Uncategorized"
        val matches = optionRegex.findAll(html)
        
        for (match in matches) {
            val fullLabel = match.groupValues[1]
            val scriptId = match.groupValues[2]
            val displayName = match.groupValues[3]
            
            // Extract category from the context (look for preceding optgroup)
            val beforeMatch = html.substring(0, match.range.first)
            val lastOptgroup = optgroupRegex.findAll(beforeMatch).lastOrNull()
            if (lastOptgroup != null) {
                currentCategory = lastOptgroup.groupValues[1]
            }
            
            scripts.add(
                KeyboardMaestroScript(
                    id = scriptId,
                    name = displayName,
                    description = "Keyboard Maestro macro",
                    category = currentCategory
                )
            )
        }
        
        return scripts
    }
}
