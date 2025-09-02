package com.chrislapointe.keyboardmaestrotv.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chrislapointe.keyboardmaestrotv.R
import com.chrislapointe.keyboardmaestrotv.models.KeyboardMaestroScript

/**
 * Adapter for displaying Keyboard Maestro scripts in a grid
 */
class ScriptsAdapter(
    private var scripts: List<KeyboardMaestroScript> = emptyList(),
    private val onScriptExecute: (KeyboardMaestroScript) -> Unit
) : RecyclerView.Adapter<ScriptsAdapter.ScriptViewHolder>() {
    
    class ScriptViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val scriptTitle: TextView = itemView.findViewById(R.id.script_title)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScriptViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_script_card, parent, false)
        return ScriptViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ScriptViewHolder, position: Int) {
        val script = scripts[position]
        
        holder.scriptTitle.text = script.getDisplayName()
        
        // Set up click listener for the entire card
        holder.itemView.setOnClickListener {
            onScriptExecute(script)
        }
        
        // Set focus handling for TV navigation
        holder.itemView.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                holder.itemView.animate().scaleX(1.05f).scaleY(1.05f).setDuration(200).start()
            } else {
                holder.itemView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            }
        }
    }
    
    override fun getItemCount(): Int = scripts.size
    
    /**
     * Update the scripts list
     */
    fun updateScripts(newScripts: List<KeyboardMaestroScript>) {
        scripts = newScripts
        notifyDataSetChanged()
    }
    
    /**
     * Get script at position
     */
    fun getScriptAt(position: Int): KeyboardMaestroScript? {
        return if (position in 0 until scripts.size) scripts[position] else null
    }
}
