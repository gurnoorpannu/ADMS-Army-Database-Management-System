package com.example.armyprojectdbmsapk.model

/**
 * Soldier data model representing a military personnel
 */
data class Soldier(
    val id: Int = 0,
    val name: String = "",
    val rank: String = "",
    val sex: String = "",
    val height: Int = 0,
    val weight: Int = 0,
    val chest: Int = 0,
    val squadNo: String = "",
    val birthPlacePincode: Int = 0,
    val doj: String = "",
    val basicPay: Int = 0,
    val medals: Int = 0
)
/**
 * SoldierStatus data model representing the current status of a soldier
 */
data class SoldierStatus(
    val id: Int = 0,
    val alive: Int = 1, // 1 for alive, 0 for deceased
    val pincode: Int = 0,
    val warDateNo: String = ""
)

/**
 * Posting data model representing a soldier's posting record
 */
data class Posting(
    val id: Int = 0,
    val date: String = "",
    val pincode: Int = 0
)
/**
 * Visited data model representing a soldier's visit record
 */
data class Visited(
    val soldId: Int = 0,
    val date: String = "",
    val pincode: Int = 0,
    val reason: String = ""
)

/**
 * Location data model representing a geographical location
 */
data class Location(
    val country: String = "",
    val state: String = "",
    val district: String = "",
    val pincode: Int = 0
)

/**
 * Battalion data model representing a military unit
 */

data class Battalion(
    val id: String = "",
    val name: String = "",
    val capacity: Int = 0
)

data class BattalionDetail(
    val id: String = "",
    val Battalion_name: String = "",  // Matches the Firestore field
    val captain_id: Int = 0,
    val total_capacity: Int = 0,
    val year: Int = 0
)

/**
 * Weapon data model representing a military weapon
 */
data class Weapon(
    val weaponId: Int = 0,
    val name: String = "",
    val manufacturer: String = "",
    val manufacturerDate: String = "",
    val caliber: Double = 0.0,
    val range: Long = 0,
    val total: Int = 0,
    val imageURL: String = "",
    val category: String = "Assault Rifle",
    val description: String = "Iconic, rugged, weapon known for reliability and impact."
)
/**
 * Inventory data model representing a weapon inventory record
 */
data class Inventory(
    val id: Int = 0,
    val weaponId: Int = 0
)

/**
 * War data model representing a war record
 */
data class War(
    val dateNo: String = "",
    val status: Int = 0, // 0 for ongoing, 1 for completed
    val pincode: Int = 0
)

/**
 * Medal data model representing a military medal
 */
data class Medal(
    val name: String = ""
)

/**
 * Work data model representing a soldier's work assignment
 */
data class Work(
    val salary: Int = 0,
    val type: String = ""
)

/**
 * Category data model representing a weapon category
 */
data class Category(
    val className: String = "",
    val name: String = ""
)

/**
 * Company data model representing a manufacturing company
 */
data class Company(
    val companyName: String = "",
    val countryName: String = ""
)

/**
 * ManufacturingDetails data model representing weapon manufacturing details
 */
data class ManufacturingDetails(
    val companyName: String = "",
    val manufacturingDate: String = "",
    val manufacturingLocation: String = "",
    val weaponId: Int = 0
)

/**
 * Assign data model representing a soldier's assignment
 */
data class Assign(
    val soldId: Int = 0,
    val type: String = ""
)
