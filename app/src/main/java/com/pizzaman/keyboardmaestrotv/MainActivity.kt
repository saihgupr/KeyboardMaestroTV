package com.pizzaman.keyboardmaestrotv

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pizzaman.keyboardmaestrotv.adapters.ScriptsAdapter
import com.pizzaman.keyboardmaestrotv.models.ConnectionSettings
import com.pizzaman.keyboardmaestrotv.models.KeyboardMaestroScript
import com.pizzaman.keyboardmaestrotv.network.KeyboardMaestroApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    
    private lateinit var scriptsRecyclerView: RecyclerView
    private lateinit var loadingLayout: LinearLayout
    private lateinit var errorLayout: LinearLayout
    private lateinit var emptyLayout: LinearLayout
    private lateinit var errorMessage: TextView
    private lateinit var retryButton: Button
    private lateinit var settingsButton: Button
    
    private lateinit var scriptsAdapter: ScriptsAdapter
    private lateinit var apiService: KeyboardMaestroApiService
    private var connectionSettings: ConnectionSettings = ConnectionSettings()
    private lateinit var sharedPreferences: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initializeViews()
        
        apiService = KeyboardMaestroApiService()
        sharedPreferences = getSharedPreferences("KeyboardMaestroTVPrefs", MODE_PRIVATE)
        
        setupRecyclerView()
        setupClickListeners()
        setupFocusBehavior()

        // Load connection settings
        loadConnectionSettings()
        
        // Load scripts on startup
        loadScripts()
    }
    
    private fun initializeViews() {
        scriptsRecyclerView = findViewById(R.id.scripts_recycler_view)
        loadingLayout = findViewById(R.id.loading_layout)
        errorLayout = findViewById(R.id.error_layout)
        emptyLayout = findViewById(R.id.empty_layout)
        errorMessage = findViewById(R.id.error_message)
        retryButton = findViewById(R.id.retry_button)
        settingsButton = findViewById(R.id.settings_button)
    }
    
    private fun setupRecyclerView() {
        scriptsAdapter = ScriptsAdapter { script ->
            executeScript(script)
        }
        
        // Get grid columns from settings
        val gridColumns = sharedPreferences.getInt("grid_columns", 3)
        
        // Use GridLayoutManager for TV-optimized layout
        val layoutManager = GridLayoutManager(this, gridColumns)
        scriptsRecyclerView.layoutManager = layoutManager
        scriptsRecyclerView.adapter = scriptsAdapter
        
        // Enable focus for TV navigation
        scriptsRecyclerView.isFocusable = true
        scriptsRecyclerView.isFocusableInTouchMode = true
    }
    
    private fun setupClickListeners() {
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        retryButton.setOnClickListener {
            loadScripts()
        }
    }

    private fun setupFocusBehavior() {
        // Make settings button behave like script cards
        settingsButton.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                settingsButton.animate()
                    .scaleX(1.05f)
                    .scaleY(1.05f)
                    .setDuration(200)
                    .start()

                // Increase elevation for shadow effect
                settingsButton.animate()
                    .setDuration(200)
                    .translationZ(8f)
                    .start()
            } else {
                settingsButton.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(200)
                    .start()

                // Reset elevation
                settingsButton.animate()
                    .setDuration(250)
                    .translationZ(2f)
                    .start()
            }
        }
    }
    
    private fun loadConnectionSettings() {
        connectionSettings = ConnectionSettings(
            macIpAddress = sharedPreferences.getString("mac_ip", "192.168.1.30") ?: "192.168.1.30",
            macPort = sharedPreferences.getInt("mac_port", 4490),
            connectionTimeout = sharedPreferences.getInt("timeout", 5000)
        )
        android.util.Log.d("MainActivity", "Loaded connection settings: ${connectionSettings.getBaseUrl()}")
    }
    
    private fun loadScripts() {
        showLoadingState()
        
        // Debug: Log connection settings
        android.util.Log.d("MainActivity", "Loading scripts with settings: ${connectionSettings.getBaseUrl()}")
        
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    apiService.getScripts(connectionSettings)
                }
                
                result.fold(
                    onSuccess = { scripts ->
                        android.util.Log.d("MainActivity", "Successfully loaded ${scripts.size} scripts")
                        if (scripts.isEmpty()) {
                            showEmptyState()
                        } else {
                            showScriptsState(scripts)
                        }
                    },
                    onFailure = { error ->
                        android.util.Log.e("MainActivity", "Failed to load scripts: ${error.message}")
                        showErrorState(error.message ?: "Unknown error")
                    }
                )
            } catch (e: Exception) {
                android.util.Log.e("MainActivity", "Exception loading scripts: ${e.message}")
                showErrorState(e.message ?: "Unknown error")
            }
        }
    }
    
    private fun executeScript(script: KeyboardMaestroScript) {
        // Show a brief loading state for the script execution
        Toast.makeText(this, "Executing ${script.getDisplayName()}...", Toast.LENGTH_SHORT).show()
        
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    apiService.executeScript(connectionSettings, script.id)
                }
                
                result.fold(
                    onSuccess = {
                        Toast.makeText(this@MainActivity, 
                            "${script.getDisplayName()} executed successfully!", 
                            Toast.LENGTH_SHORT).show()
                    },
                    onFailure = { error ->
                        Toast.makeText(this@MainActivity, 
                            "Failed to execute ${script.getDisplayName()}: ${error.message}", 
                            Toast.LENGTH_LONG).show()
                    }
                )
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, 
                    "Error executing script: ${e.message}", 
                    Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private fun showLoadingState() {
        loadingLayout.visibility = View.VISIBLE
        errorLayout.visibility = View.GONE
        emptyLayout.visibility = View.GONE
        scriptsRecyclerView.visibility = View.GONE
    }
    
    private fun showErrorState(message: String) {
        loadingLayout.visibility = View.GONE
        errorLayout.visibility = View.VISIBLE
        emptyLayout.visibility = View.GONE
        scriptsRecyclerView.visibility = View.GONE
        
        errorMessage.text = message
    }
    
    private fun showEmptyState() {
        loadingLayout.visibility = View.GONE
        errorLayout.visibility = View.GONE
        emptyLayout.visibility = View.VISIBLE
        scriptsRecyclerView.visibility = View.GONE
    }
    
    private fun showScriptsState(scripts: List<KeyboardMaestroScript>) {
        loadingLayout.visibility = View.GONE
        errorLayout.visibility = View.GONE
        emptyLayout.visibility = View.GONE
        scriptsRecyclerView.visibility = View.VISIBLE
        
        scriptsAdapter.updateScripts(scripts)
    }
    
    override fun onResume() {
        super.onResume()
        // Reload connection settings and scripts when returning from settings
        loadConnectionSettings()
        
        // Check if grid columns setting changed and update layout if needed
        val currentGridColumns = sharedPreferences.getInt("grid_columns", 3)
        val currentLayoutManager = scriptsRecyclerView.layoutManager as? GridLayoutManager
        if (currentLayoutManager?.spanCount != currentGridColumns) {
            setupRecyclerView()
        }
        
        loadScripts()
    }
}
