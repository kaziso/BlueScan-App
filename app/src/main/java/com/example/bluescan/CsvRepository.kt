package com.example.bluescan

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class CsvRepository(private val context: Context) {

    private val fileName = "bluescan_data.csv"
    val tables: MutableList<TableData> = mutableListOf()

    fun init(folderUri: Uri) {
        val folder = DocumentFile.fromTreeUri(context, folderUri) ?: return
        var file = folder.findFile(fileName)
        
        if (file == null || !file.exists()) {
            file = folder.createFile("text/csv", fileName)
            // Create default table
            tables.add(TableData(date = "2026-02-03", tableName = folder.name ?: "New Project"))
            saveAll(folderUri)
        } else {
            readAll(file.uri)
        }
    }

    fun addTable(folderUri: Uri, newTable: TableData) {
        tables.add(newTable)
        saveAll(folderUri)
    }
    
    fun updateTable(folderUri: Uri, index: Int, updatedTable: TableData) {
        if (index in tables.indices) {
            tables[index] = updatedTable
            saveAll(folderUri)
        }
    }

    fun updateCell(folderUri: Uri, tableIndex: Int, unit: Int, part: String, value: String) {
        if (tableIndex !in tables.indices) return
        val table = tables[tableIndex]
        
        if (!table.matrix.containsKey(part)) {
            table.matrix[part] = mutableMapOf()
        }
        table.matrix[part]!![unit] = value
        saveAll(folderUri)
    }

    fun checkDuplicateGlobal(value: String): Boolean {
        for (table in tables) {
            for (part in table.parts) {
                val unitMap = table.matrix[part] ?: continue
                if (unitMap.containsValue(value)) {
                    return true
                }
            }
        }
        return false
    }

    private fun saveAll(folderUri: Uri) {
        val folder = DocumentFile.fromTreeUri(context, folderUri) ?: return
        val file = folder.findFile(fileName) ?: folder.createFile("text/csv", fileName) ?: return

        try {
            context.contentResolver.openOutputStream(file.uri, "wt")?.use { outputStream ->
                val writer = OutputStreamWriter(outputStream)
                
                for ((index, table) in tables.withIndex()) {
                    if (index > 0) writer.append("\n") // Block separator

                    // Block Header: [Project: Name | Date: ... | Target: ...]
                    writer.append("[Project: ${table.tableName} | Date: ${table.date} | Target: ${table.startUnit}-${table.endUnit}]\n")
                    
                    // Column Headers: ,Unit 1, Unit 2...
                    writer.append("Parts") // Corner
                    for (unit in table.startUnit..table.endUnit) {
                        writer.append(",Unit $unit")
                    }
                    writer.append("\n")

                    // Rows: PartName, val, val...
                    for (part in table.parts) {
                        writer.append(part)
                        val partData = table.matrix[part]
                        for (unit in table.startUnit..table.endUnit) {
                            val valStr = partData?.get(unit) ?: ""
                            writer.append(",$valStr")
                        }
                        writer.append("\n")
                    }
                }
                writer.flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun readAll(fileUri: Uri) {
        tables.clear()
        try {
            context.contentResolver.openInputStream(fileUri)?.use { inputStream ->
                val reader = BufferedReader(InputStreamReader(inputStream))
                var currentTable: TableData? = null
                
                reader.forEachLine { line ->
                    val trimmed = line.trim()
                    if (trimmed.isEmpty()) return@forEachLine

                    if (trimmed.startsWith("[Project:")) {
                        // Start new block
                        if (currentTable != null) {
                            tables.add(currentTable!!)
                        }
                        currentTable = parseBlockHeader(trimmed)
                    } else if (trimmed.startsWith("Parts,Unit")) {
                        // Header row, ignore (structure is known)
                    } else if (currentTable != null) {
                        // Data Row: PartName, val1, val2...
                        val cols = trimmed.split(",")
                        if (cols.isNotEmpty()) {
                            val partName = cols[0]
                            // If partName is not in list, add it? Or restrict?
                            // User wants "Edit Parts". So we should trust CSV rows.
                            if (!currentTable!!.parts.contains(partName)) {
                                currentTable!!.parts.add(partName)
                            }
                            
                            if (!currentTable!!.matrix.containsKey(partName)) {
                                currentTable!!.matrix[partName] = mutableMapOf()
                            }
                            
                            // Load values
                            for (i in 1 until cols.size) {
                                val unitOffset = i - 1
                                val unitNum = currentTable!!.startUnit + unitOffset
                                if (unitNum <= currentTable!!.endUnit) {
                                    val valStr = cols[i]
                                    if (valStr.isNotEmpty()) {
                                        currentTable!!.matrix[partName]!![unitNum] = valStr
                                    }
                                }
                            }
                        }
                    }
                }
                // Add last table
                if (currentTable != null) {
                    tables.add(currentTable!!)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun parseBlockHeader(header: String): TableData {
        // [Project: X | Date: Y | Target: 1-200]
        val content = header.trim('[', ']')
        val parts = content.split("|")
        val table = TableData()
        table.parts.clear() // Will fill from rows

        for (part in parts) {
            val kv = part.split(":")
            if (kv.size == 2) {
                val key = kv[0].trim()
                val value = kv[1].trim()
                when (key) {
                    "Project" -> table.tableName = value
                    "Date" -> table.date = value
                    "Target" -> {
                        val range = value.split("-")
                        if (range.size == 2) {
                            table.startUnit = range[0].toIntOrNull() ?: 1
                            table.endUnit = range[1].toIntOrNull() ?: 200
                        }
                    }
                }
            }
        }
        return table
    }
}
