package com.example.bluescan

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditProjectBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var repo: CsvRepository
    private lateinit var projectUri: Uri
    private lateinit var tableData: TableData
    private lateinit var partsAdapter: PartsAdapter
    private var tableIndex: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_edit_project, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uriString = arguments?.getString("PROJECT_URI") ?: return dismiss()
        tableIndex = arguments?.getInt("TABLE_INDEX", -1) ?: -1

        projectUri = Uri.parse(uriString)
        repo = CsvRepository(requireContext())
        repo.init(projectUri)

        if (tableIndex !in repo.tables.indices) {
            dismiss()
            return
        }

        tableData = repo.tables[tableIndex]

        val etName = view.findViewById<EditText>(R.id.etProjectName)
        val etDate = view.findViewById<EditText>(R.id.etDate)
        val etStart = view.findViewById<EditText>(R.id.etStartUnit)
        val etEnd = view.findViewById<EditText>(R.id.etEndUnit)
        val rvParts = view.findViewById<RecyclerView>(R.id.rvParts)
        val etNewPart = view.findViewById<EditText>(R.id.etNewPart)

        etName.setText(tableData.tableName)
        etDate.setText(tableData.date)
        etStart.setText(tableData.startUnit.toString())
        etEnd.setText(tableData.endUnit.toString())

        val partsList = ArrayList(tableData.parts)
        partsAdapter = PartsAdapter(partsList)

        rvParts.layoutManager = LinearLayoutManager(context)
        rvParts.adapter = partsAdapter

        view.findViewById<Button>(R.id.btnAddPart).setOnClickListener {
            val name = etNewPart.text.toString().trim()
            if (name.isNotEmpty() && !partsList.contains(name)) {
                partsAdapter.addPart(name)
                etNewPart.setText("")
            }
        }

        view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dismiss()
        }

        view.findViewById<Button>(R.id.btnSave).setOnClickListener {
            val updated = tableData.copy(
                tableName = etName.text.toString().trim(),
                date = etDate.text.toString().trim(),
                startUnit = etStart.text.toString().toIntOrNull() ?: tableData.startUnit,
                endUnit = etEnd.text.toString().toIntOrNull() ?: tableData.endUnit,
                parts = partsList
            )

            updated.matrix = tableData.matrix
            repo.updateTable(projectUri, tableIndex, updated)

            // 🔥 REFRESH DASHBOARD DENGAN CARA YANG BENAR
            (activity as? DashboardActivity)?.refreshDashboard()

            dismiss()
        }
    }
}
