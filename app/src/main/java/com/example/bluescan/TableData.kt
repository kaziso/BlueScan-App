package com.example.bluescan

data class TableData(
    var tableName: String = "Project",
    var date: String = "",
    var startUnit: Int = 1,
    var endUnit: Int = 200,
    var parts: MutableList<String> = mutableListOf("RAM", "Motherboard", "SSD", "Heatsink", "WIFI", "Unit", "Battery", "Speaker"),
    // Map<PartName, Map<UnitNumber, Value>>
    // Important: PartName is key, inner key is Unit Number
    var matrix: MutableMap<String, MutableMap<Int, String>> = mutableMapOf()
)
