package com.example.bluescan

data class ProjectConfig(
    var projectName: String = "New Project",
    var date: String = "",
    var startUnit: Int = 1,
    var endUnit: Int = 200,
    var parts: MutableList<String> = mutableListOf("RAM", "Motherboard", "SSD", "Heatsink", "WIFI", "Unit", "Battery", "Speaker")
)
