package com.example.bluescan

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import org.json.JSONArray
import org.json.JSONObject

data class ProjectItem(val uri: String, val name: String, val timestamp: Long)

class FolderSelectionActivity : AppCompatActivity() {

    private lateinit var rvProjects: RecyclerView
    private lateinit var tvEmptyState: TextView
    private val projects = mutableListOf<ProjectItem>()
    private lateinit var adapter: ProjectAdapter

    private val folderPicker = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
        uri?.let {
            contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            saveProject(it)
            navigateToDashboard(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_selection)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)

        rvProjects = findViewById(R.id.rvProjects)
        tvEmptyState = findViewById(R.id.tvEmptyState)

        findViewById<Button>(R.id.btnSelectFolder).setOnClickListener {
            folderPicker.launch(null)
        }
        
        setupRecyclerView()
        loadProjects()
    }
    
    override fun onResume() {
        super.onResume()
        loadProjects() // Reload in case returning from somewhere updates it, or just to be safe
    }

    private fun setupRecyclerView() {
        adapter = ProjectAdapter(projects) { item ->
            val uri = Uri.parse(item.uri)
            if (checkPermission(uri)) {
                // Update timestamp on click
                saveProject(uri)
                navigateToDashboard(uri)
            } else {
                Toast.makeText(this, "Permission lost, please reselect", Toast.LENGTH_SHORT).show()
                folderPicker.launch(uri)
            }
        }
        rvProjects.layoutManager = LinearLayoutManager(this)
        rvProjects.adapter = adapter
    }

    private fun loadProjects() {
        projects.clear()
        val prefs = getSharedPreferences("bluescan_prefs", Context.MODE_PRIVATE)
        val jsonStr = prefs.getString("saved_projects", null)

        if (jsonStr != null) {
            try {
                val jsonArray = JSONArray(jsonStr)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    projects.add(
                        ProjectItem(
                            uri = obj.getString("uri"),
                            name = obj.getString("name"),
                            timestamp = obj.getLong("timestamp")
                        )
                    )
                }
                // Sort by newst
                projects.sortByDescending { it.timestamp }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        // Fallback for old single version migration (optional, but good UX)
        // If empty, check old key
        if (projects.isEmpty()) {
             val oldUri = prefs.getString("recent_uri", null)
             if (oldUri != null) {
                 val oldName = prefs.getString("recent_name", "Project") ?: "Project"
                 projects.add(ProjectItem(oldUri, oldName, System.currentTimeMillis()))
                 // Save to new format immediately
                 saveProjectsList()
             }
        }

        adapter.notifyDataSetChanged()
        updateEmptyState()
    }

    private fun saveProject(uri: Uri) {
        val name = androidx.documentfile.provider.DocumentFile.fromTreeUri(this, uri)?.name ?: "Project"
        val newItem = ProjectItem(uri.toString(), name, System.currentTimeMillis())
        
        // Remove existing if any (to update timestamp/move to top)
        projects.removeAll { it.uri == newItem.uri }
        projects.add(0, newItem)
        
        saveProjectsList()
    }

    private fun saveProjectsList() {
        val jsonArray = JSONArray()
        projects.forEach { 
            val obj = JSONObject()
            obj.put("uri", it.uri)
            obj.put("name", it.name)
            obj.put("timestamp", it.timestamp)
            jsonArray.put(obj)
        }
        
        getSharedPreferences("bluescan_prefs", Context.MODE_PRIVATE).edit {
            putString("saved_projects", jsonArray.toString())
            // Clear old keys to avoid confusion or double migration
            remove("recent_uri")
            remove("recent_name")
        }
    }
    
    private fun updateEmptyState() {
        if (projects.isEmpty()) {
            tvEmptyState.visibility = View.VISIBLE
            rvProjects.visibility = View.GONE
        } else {
            tvEmptyState.visibility = View.GONE
            rvProjects.visibility = View.VISIBLE
        }
    }
    
    private fun checkPermission(uri: Uri): Boolean {
        return try {
            contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            true
        } catch (e: Exception) {
            contentResolver.persistedUriPermissions.any { it.uri == uri }
        }
    }

    private fun navigateToDashboard(folderUri: Uri) {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("PROJECT_URI", folderUri.toString())
        startActivity(intent)
    }
    
    // Inner Adapter Class
    inner class ProjectAdapter(
        private val items: List<ProjectItem>,
        private val onClick: (ProjectItem) -> Unit
    ) : RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById(R.id.tvProjectName)
            val tvPath: TextView = view.findViewById(R.id.tvProjectPath)
            
            init {
                view.setOnClickListener { onClick(items[adapterPosition]) }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_project, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.tvName.text = item.name
            holder.tvPath.text = Uri.parse(item.uri).path ?: item.uri
        }

        override fun getItemCount() = items.size
    }
}
