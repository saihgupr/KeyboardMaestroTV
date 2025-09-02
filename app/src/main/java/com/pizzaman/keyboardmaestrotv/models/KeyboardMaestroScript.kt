package com.pizzaman.keyboardmaestrotv.models

import com.google.gson.annotations.SerializedName

/**
 * Represents a Keyboard Maestro script that can be executed remotely
 */
data class KeyboardMaestroScript(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String? = null,
    
    @SerializedName("category")
    val category: String? = null,
    
    @SerializedName("enabled")
    val enabled: Boolean = true,
    
    @SerializedName("lastExecuted")
    val lastExecuted: String? = null
) {
    /**
     * Get a display-friendly name for the script
     */
    fun getDisplayName(): String {
        return name.ifEmpty { "Unnamed Script" }
    }
    
    /**
     * Get a display-friendly description for the script
     */
    fun getDisplayDescription(): String {
        return description?.ifEmpty { "No description available" } ?: "No description available"
    }
    
    /**
     * Get the category or "Uncategorized" if none
     */
    fun getDisplayCategory(): String {
        return category?.ifEmpty { "Uncategorized" } ?: "Uncategorized"
    }
}
