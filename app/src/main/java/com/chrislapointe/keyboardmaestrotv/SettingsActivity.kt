package com.chrislapointe.keyboardmaestrotv

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chrislapointe.keyboardmaestrotv.models.ConnectionSettings
import com.chrislapointe.keyboardmaestrotv.network.KeyboardMaestroApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var macIpEdit: EditText
    private lateinit var macPortEdit: EditText
    private lateinit var timeoutEdit: EditText
    private lateinit var gridColumnsDisplay: TextView
    private lateinit var gridColumnsDecrease: Button
    private lateinit var gridColumnsIncrease: Button
    private lateinit var testConnectionButton: Button
    private lateinit var backButton: Button
    
    private lateinit var apiService: KeyboardMaestroApiService
    private lateinit var sharedPreferences: SharedPreferences
    
    companion object {
        private const val PREFS_NAME = "KeyboardMaestroTVPrefs"
        private const val KEY_MAC_IP = "mac_ip"
        private const val KEY_MAC_PORT = "mac_port"
        private const val KEY_TIMEOUT = "timeout"
        private const val KEY_GRID_COLUMNS = "grid_columns"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        
        apiService = KeyboardMaestroApiService()
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        
        initializeViews()
        setupClickListeners()
        loadSettings()
    }
    
    private fun initializeViews() {
        macIpEdit = findViewById(R.id.mac_ip_edit)
        macPortEdit = findViewById(R.id.mac_port_edit)
        timeoutEdit = findViewById(R.id.timeout_edit)
        gridColumnsDisplay = findViewById(R.id.grid_columns_display)
        gridColumnsDecrease = findViewById(R.id.grid_columns_decrease)
        gridColumnsIncrease = findViewById(R.id.grid_columns_increase)
        testConnectionButton = findViewById(R.id.test_connection_button)
        backButton = findViewById(R.id.back_button)
    }
    
    private fun setupClickListeners() {
        backButton.setOnClickListener {
            finish()
        }
        
        testConnectionButton.setOnClickListener {
            testConnection()
        }
        
        gridColumnsDecrease.setOnClickListener {
            val currentValue = gridColumnsDisplay.text.toString().toIntOrNull() ?: 3
            val newValue = maxOf(2, currentValue - 1) // Minimum 2 columns
            gridColumnsDisplay.text = newValue.toString()
            saveGridColumnsSetting(newValue)
        }
        
        gridColumnsIncrease.setOnClickListener {
            val currentValue = gridColumnsDisplay.text.toString().toIntOrNull() ?: 3
            val newValue = minOf(6, currentValue + 1) // Maximum 6 columns
            gridColumnsDisplay.text = newValue.toString()
            saveGridColumnsSetting(newValue)
        }
    }
    
    private fun loadSettings() {
        macIpEdit.setText(sharedPreferences.getString(KEY_MAC_IP, "192.168.1.30"))
        macPortEdit.setText(sharedPreferences.getInt(KEY_MAC_PORT, 4490).toString())
        timeoutEdit.setText(sharedPreferences.getInt(KEY_TIMEOUT, 5000).toString())
        val gridColumns = sharedPreferences.getInt(KEY_GRID_COLUMNS, 3)
        gridColumnsDisplay.text = gridColumns.toString()
    }
    
    private fun saveSettings() {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_MAC_IP, macIpEdit.text.toString())
        editor.putInt(KEY_MAC_PORT, macPortEdit.text.toString().toIntOrNull() ?: 4490)
        editor.putInt(KEY_TIMEOUT, timeoutEdit.text.toString().toIntOrNull() ?: 5000)
        editor.putInt(KEY_GRID_COLUMNS, gridColumnsDisplay.text.toString().toIntOrNull() ?: 3)
        editor.apply()
    }
    
    private fun saveGridColumnsSetting(value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_GRID_COLUMNS, value)
        editor.apply()
    }
    
    private fun getConnectionSettings(): ConnectionSettings {
        return ConnectionSettings(
            macIpAddress = macIpEdit.text.toString(),
            macPort = macPortEdit.text.toString().toIntOrNull() ?: 4490,
            connectionTimeout = timeoutEdit.text.toString().toIntOrNull() ?: 5000
        )
    }
    
    private fun testConnection() {
        val settings = getConnectionSettings()
        
        if (!settings.isValid()) {
            Toast.makeText(this, "Please enter valid connection settings", Toast.LENGTH_SHORT).show()
            return
        }
        
        testConnectionButton.isEnabled = false
        testConnectionButton.text = "Testing..."
        
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    apiService.testConnection(settings)
                }
                
                result.fold(
                    onSuccess = {
                        Toast.makeText(this@SettingsActivity, 
                            "Connection successful!", 
                            Toast.LENGTH_SHORT).show()
                        saveSettings()
                    },
                    onFailure = { error ->
                        Toast.makeText(this@SettingsActivity, 
                            "Connection failed: ${error.message}", 
                            Toast.LENGTH_LONG).show()
                    }
                )
            } catch (e: Exception) {
                Toast.makeText(this@SettingsActivity, 
                    "Error testing connection: ${e.message}", 
                    Toast.LENGTH_LONG).show()
            } finally {
                testConnectionButton.isEnabled = true
                testConnectionButton.text = "Test Connection"
            }
        }
    }
    
    override fun onPause() {
        super.onPause()
        // Save settings when leaving the activity
        saveSettings()
    }
}
