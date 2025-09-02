package com.chrislapointe.keyboardmaestrotv.models

/**
 * Represents the connection settings for communicating with the Mac
 */
data class ConnectionSettings(
    val macIpAddress: String = "192.168.1.100",
    val macPort: Int = 1234,
    val connectionTimeout: Int = 5000,
    val useHttps: Boolean = false
) {
    /**
     * Get the base URL for API calls
     */
    fun getBaseUrl(): String {
        val protocol = if (useHttps) "https" else "http"
        return "$protocol://$macIpAddress:$macPort"
    }
    
    /**
     * Get the scripts endpoint URL
     */
    fun getScriptsUrl(): String {
        return "${getBaseUrl()}/api/scripts"
    }
    
    /**
     * Get the execute script endpoint URL
     */
    fun getExecuteScriptUrl(scriptId: String): String {
        return "${getBaseUrl()}/api/scripts/$scriptId/execute"
    }
    
    /**
     * Validate the connection settings
     */
    fun isValid(): Boolean {
        return macIpAddress.isNotBlank() && 
               macPort in 1..65535 && 
               connectionTimeout > 0
    }
}
