package com.example.armyprojectdbmsapk.data

import com.example.armyprojectdbmsapk.model.Battalion
import com.example.armyprojectdbmsapk.model.Weapon
import com.example.armyprojectdbmsapk.model.Inventory
import com.example.armyprojectdbmsapk.model.Location
import com.example.armyprojectdbmsapk.model.War
import com.example.armyprojectdbmsapk.model.Medal
import com.example.armyprojectdbmsapk.model.Posting
import com.example.armyprojectdbmsapk.model.Soldier
import com.example.armyprojectdbmsapk.model.SoldierStatus
import com.example.armyprojectdbmsapk.model.Visited
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Helper class for Firebase Firestore operations.
 * Follows the Repository pattern to provide a clean API for data access.
 */
class FirebaseStorageHelper {
    private val firestore = FirebaseFirestore.getInstance()

    /**
     * Helper method to create a Soldier object from a document
     */
    private fun createSoldierFromDocument(document: DocumentSnapshot): Soldier {
        try {
            // Try different field name variations
            val id = document.getLong("id") ?: document.getLong("ID") ?: 0
            val name = document.getString("name") ?: document.getString("Name") ?: ""
            val rank = document.getString("rank") ?: document.getString("Rank") ?: ""
            val sex = document.getString("sex") ?: document.getString("Sex") ?: ""
            val height = document.getLong("height") ?: document.getLong("Height") ?: 0
            val weight = document.getLong("weight") ?: document.getLong("Weight") ?: 0
            val chest = document.getLong("chest") ?: document.getLong("Chest") ?: 0
            val squadNo = document.getString("squadNo") ?: document.getString("SquadNo") ?: ""
            val birthPlacePincode = document.getLong("birthPlacePincode") ?: document.getLong("BirthPlacePincode") ?: 0
            
            // Try different variations of basic_pay field name
            val basicPayLong = document.getLong("basic_pay") ?: document.getLong("basicPay") ?: 
                          document.getLong("BasicPay") ?: document.getLong("BASIC_PAY") ?: 
                          document.getLong("basic") ?: // Added this field name
                          0
            
            // Add debug print to see the raw document data
            println("Debug: Raw document data: ${document.data}")
            println("Debug: basic field = ${document.getLong("basic")}")  // Add this debug line
            val medals = document.getLong("medals") ?: 0
            
            // Handle DOJ as Timestamp or String
            val doj = try {
                // Try to get as Timestamp first
                val timestamp = document.getTimestamp("DOJ") ?: document.getTimestamp("doj")
                if (timestamp != null) {
                    // Convert timestamp to a readable date string
                    val date = timestamp.toDate()
                    java.text.SimpleDateFormat("dd MMMM yyyy", java.util.Locale.US).format(date)
                } else {
                    // Fall back to string if not a timestamp
                    document.getString("DOJ") ?: document.getString("doj") ?: ""
                }
            } catch (e: Exception) {
                println("Error parsing DOJ: ${e.message}")
                ""  // Default empty string if parsing fails
            }
        
            println("Creating Soldier with ID: $id, Name: $name, Rank: $rank, DOJ: $doj, Basic Pay: $basicPayLong, Medals: $medals")
            
            return Soldier(
                id = id.toInt(),
                name = name,
                rank = rank,
                sex = sex,
                height = height.toInt(),
                weight = weight.toInt(),
                chest = chest.toInt(),
                squadNo = squadNo,
                birthPlacePincode = birthPlacePincode.toInt(),
                doj = doj,
                basicPay = basicPayLong.toInt(),  // Use basicPayLong directly here
                medals = medals.toInt()
            )
        } catch (e: Exception) {
            println("Error creating Soldier object: ${e.message}")
            e.printStackTrace()
            
            // Return a default soldier object to prevent crashes
            return Soldier(
                id = 0,
                name = "",
                rank = "",
                sex = "",
                height = 0,
                weight = 0,
                chest = 0,
                squadNo = "",
                birthPlacePincode = 0,
                doj = "",
                basicPay = 0,
                medals = 0
            )
        }
    }

    /**
     * Fetch a soldier by ID
     */
    suspend fun getSoldierById(id: Int): Soldier? {
        return try {
            println("Searching for soldier with ID: $id")
            
            // Debug: Print Firebase instance details
            println("Firebase instance: $firestore")
            
            // Debug: Check if we can access any collections at all
            try {
                // Get all collections by trying to access them directly
                println("Trying to access known collections:")
                val soldierExists = firestore.collection("soldier").limit(1).get().await()
                println("'soldier' collection exists: ${!soldierExists.isEmpty}")
                
                val statusExists = firestore.collection("SoldierStatus").limit(1).get().await()
                println("'SoldierStatus' collection exists: ${!statusExists.isEmpty}")
                
                val postingExists = firestore.collection("posting").limit(1).get().await()
                println("'posting' collection exists: ${!postingExists.isEmpty}")
            } catch (e: Exception) {
                println("Failed to check collections: ${e.message}")
            }
            
            // First, let's get all soldiers to see what we have
            try {
                val allSnapshot = firestore.collection("soldier")
                    .get()
                    .await()
                    
                println("Total soldiers in database: ${allSnapshot.size()}")
                allSnapshot.documents.forEach { doc ->
                    println("Found soldier: ${doc.id} => ${doc.data}")
                }
            } catch (e: Exception) {
                println("Failed to get all soldiers: ${e.message}")
                e.printStackTrace()
            }
            
            // Now try to find the specific soldier
            try {
                val snapshot = firestore.collection("soldier")
                    .whereEqualTo("id", id)
                    .get()
                    .await()

                if (snapshot.isEmpty) {
                    println("No soldier found with ID: $id")
                    
                    // Try with a different approach - query as a Long
                    val longSnapshot = firestore.collection("soldier")
                        .whereEqualTo("id", id.toLong())
                        .get()
                        .await()
                    
                    if (longSnapshot.isEmpty) {
                        println("No soldier found with ID as Long: $id")
                        
                        // Try with string ID as a last resort
                        try {
                            val stringSnapshot = firestore.collection("soldier")
                                .whereEqualTo("id", id.toString())
                                .get()
                                .await()
                                
                            if (stringSnapshot.isEmpty) {
                                println("No soldier found with ID as String: $id")
                                return null
                            } else {
                                val document = stringSnapshot.documents.first()
                                println("Found soldier with String ID: ${document.data}")
                                
                                return createSoldierFromDocument(document)
                            }
                        } catch (e: Exception) {
                            println("Error querying with string ID: ${e.message}")
                            return null
                        }
                    } else {
                        val document = longSnapshot.documents.first()
                        println("Found soldier with Long ID: ${document.data}")
                        
                        return createSoldierFromDocument(document)
                    }
            }

                val document = snapshot.documents.first()
                println("Found soldier document: ${document.data}")
                
                return createSoldierFromDocument(document)
            } catch (e: Exception) {
                println("Error finding soldier by ID: ${e.message}")
                e.printStackTrace()
                return null
            }
        } catch (e: Exception) {
            println("Fatal error in getSoldierById: ${e.message}")
            e.printStackTrace()
            null
        }
    }
    suspend fun getPostingsForSoldier(id: Int): List<Posting> {
        return try {
            println("Fetching postings for soldier ID: $id")
            
            // First, let's get all postings to see what we have
            val allPostingsSnapshot = firestore.collection("posting")
                .get()
                .await()
                
            println("Total postings in database: ${allPostingsSnapshot.size()}")
            allPostingsSnapshot.documents.forEach { doc ->
                println("Found posting: ${doc.id} => ${doc.data}")
            }
            
            // Try different field names for soldier ID
            val postingsRef1 = firestore.collection("posting")
                .whereEqualTo("id", id)
                .get()
                .await()
                
            val postingsRef2 = firestore.collection("posting")
                .whereEqualTo("soldier_id", id)
                .get()
                .await()
                
            val postingsRef3 = firestore.collection("posting")
                .whereEqualTo("soldId", id)
                .get()
                .await()

            println("Results for soldier ID=$id: id=${postingsRef1.size()}, soldier_id=${postingsRef2.size()}, soldId=${postingsRef3.size()}")
            
            // Use the non-empty result, or empty list if all are empty
            val postingsRef = when {
                !postingsRef1.isEmpty -> postingsRef1
                !postingsRef2.isEmpty -> postingsRef2
                !postingsRef3.isEmpty -> postingsRef3
                else -> {
                    println("No postings found for soldier ID: $id")
                    return emptyList()
                }
            }

            println("Debug: Found ${postingsRef.size()} posting documents for soldier ID: $id")

            postingsRef.documents.mapNotNull { document ->
                try {
                    // Debug the document data
                    println("Debug: Posting document data: ${document.data}")
                    
                    // Extract posting fields directly from the document
                    val id = document.getLong("id") ?: 0
                    val pincode = document.getLong("pincode") ?: 0
                    
                    // Handle Timestamp conversion
                    val dateStr = try {
                        val timestamp = document.getTimestamp("date")
                        if (timestamp != null) {
                            // Convert timestamp to a readable date string
                            val date = timestamp.toDate()
                            java.text.SimpleDateFormat("dd MMMM yyyy", java.util.Locale.US).format(date)
                        } else {
                            document.getString("date") ?: ""
                        }
                    } catch (e: Exception) {
                        println("Error parsing date: ${e.message}")
                        ""
                    }
                    
                    // Create a map of the posting data with correct field names
                    val postingMap = mapOf(
                        "id" to id,
                        "date" to dateStr,
                        "pincode" to pincode
                    )
                    
                    println("Debug: Created posting map: $postingMap")
                    
                    // Create a custom DocumentSnapshot with our map
                    val customDoc = CustomDocumentSnapshot(postingMap)
                    
                    // Create the posting using our helper method
                    val posting = createPostingFromDocument(customDoc)
                    println("Debug: Created posting: $posting")
                    posting
                } catch (e: Exception) {
                    println("Debug: Error creating posting: ${e.message}")
                    e.printStackTrace()
                    null
                }
            }
        } catch (e: Exception) {
            println("Debug: Error fetching postings: ${e.message}")
            emptyList()
        }
    }
    
    /**
     * Helper class to create a custom DocumentSnapshot
     */
    private class CustomDocumentSnapshot(private val data: Map<String, Any?>) {
        fun getLong(field: String): Long? {
            return when (val value = data[field]) {
                is Long -> value
                is Int -> value.toLong()
                is String -> value.toLongOrNull()
                else -> null
            }
        }
        
        fun getString(field: String): String? {
            return when (val value = data[field]) {
                is String -> value
                else -> value?.toString()
            }
        }
        
        fun getData(): Map<String, Any?> {
            return data
        }
    }
    
    /**
     * Helper method to create a Posting object from document data
     */
    private fun createPostingFromDocument(document: CustomDocumentSnapshot): Posting {
        try {
            // Try multiple possible field names for each field
            val id = document.getLong("posting_id") ?: 
                     document.getLong("postingId") ?: 
                     document.getLong("id") ?: 0
                     
            val date = document.getString("start_date") ?: 
                      document.getString("startDate") ?: 
                      document.getString("date") ?: 
                      document.getString("Date") ?: ""
                      
            val pincode = document.getLong("pincode") ?: 
                         document.getLong("Pincode") ?: 
                         document.getLong("location") ?: 0
            
            println("Debug: Raw posting data: ${document.getData()}")
            println("Creating Posting with ID: $id, Date: $date, Pincode: $pincode")
            
            return Posting(
                id = id.toInt(),
                date = date,
                pincode = pincode.toInt()
            )
        } catch (e: Exception) {
            println("Error creating Posting object: ${e.message}")
            e.printStackTrace()
            
            return Posting(
                id = 0,
                date = "",
                pincode = 0
            )
        }
    }

    /**
     * Fetch soldier status by soldier ID
     */
    suspend fun getSoldierStatusById(id: Int): SoldierStatus? {
        return try {
            println("Fetching soldier status for ID: $id")
            
            // First, let's get all soldier statuses to see what we have
            val allSnapshot = firestore.collection("SoldierStatus")
                .get()
                .await()
                
            println("Total soldier statuses in database: ${allSnapshot.size()}")
            allSnapshot.documents.forEach { doc ->
                println("Found soldier status: ${doc.data}")
            }
            
            // Try different field names for ID
            val snapshot1 = firestore.collection("SoldierStatus")
                .whereEqualTo("ID", id)
                .get()
                .await()
                
            val snapshot2 = firestore.collection("SoldierStatus")
                .whereEqualTo("id", id)
                .get()
                .await()
                
            val snapshot3 = firestore.collection("SoldierStatus")
                .whereEqualTo("soldier_id", id)
                .get()
                .await()

            println("Results for ID=$id: uppercase=${snapshot1.size()}, lowercase=${snapshot2.size()}, soldier_id=${snapshot3.size()}")
            
            val snapshot = if (!snapshot1.isEmpty) snapshot1
                else if (!snapshot2.isEmpty) snapshot2
                else snapshot3

            if (snapshot.isEmpty) {
                println("No soldier status found for ID: $id")
                return null
            }

            val document = snapshot.documents.first()
            println("Found soldier status document: ${document.data}")
            
            // Try to extract fields with different possible names
            val idField = document.getLong("ID") ?: document.getLong("id") ?: 0
            val aliveField = document.getLong("Alive") ?: document.getLong("alive") ?: 1
            val pincodeField = document.getLong("Pincode") ?: document.getLong("pincode") ?: 0
            val warDateField = document.getString("war_date_no") ?: document.getString("warDateNo") ?: ""
            
            SoldierStatus(
                id = idField.toInt(),
                alive = aliveField.toInt(),
                pincode = pincodeField.toInt(),
                warDateNo = warDateField
            )
        } catch (e: Exception) {
            null
        }
    }
    /**
     * Fetch visits for a soldier by ID
     * If no visits are found for this soldier, returns random visits from the database
     */
    suspend fun getVisitsForSoldier(id: Int): List<Visited> {
        return try {
            println("Fetching visits for soldier ID: $id")
            
            // First, get all visits from the database
            val allVisitsSnapshot = firestore.collection("visited")
                .get()
                .await()
                
            println("Total visits in database: ${allVisitsSnapshot.size()}")
            val allVisits = allVisitsSnapshot.documents.mapNotNull { doc ->
                println("Found visit: ${doc.data}")
                try {
                    // Handle date as Timestamp
                    val dateStr = try {
                        val timestamp = doc.getTimestamp("date")
                        if (timestamp != null) {
                            // Convert timestamp to a readable date string
                            val date = timestamp.toDate()
                            java.text.SimpleDateFormat("dd MMMM yyyy", java.util.Locale.US).format(date)
                        } else {
                            // Fall back to string if not a timestamp
                            doc.getString("date") ?: ""
                        }
                    } catch (e: Exception) {
                        println("Error parsing date: ${e.message}")
                        ""  // Default empty string if parsing fails
                    }
                    
                    Visited(
                        soldId = doc.getLong("sold_id")?.toInt() ?: 0,
                        date = dateStr,
                        pincode = doc.getLong("pincode")?.toInt() ?: 0,
                        reason = doc.getString("reason") ?: ""
                    )
                } catch (e: Exception) {
                    println("Error parsing visit: ${e.message}")
                    null
                }
            }
            
            // Try to get visits specific to this soldier
            val soldierVisitsSnapshot = firestore.collection("visited")
                .whereEqualTo("sold_id", id)
                .get()
                .await()

            println("Found ${soldierVisitsSnapshot.size()} visits for soldier ID $id")
            
            val soldierVisits = soldierVisitsSnapshot.documents.mapNotNull { document ->
                println("Processing visit document: ${document.data}")
                try {
                    // Handle date as Timestamp
                    val dateStr = try {
                        val timestamp = document.getTimestamp("date")
                        if (timestamp != null) {
                            // Convert timestamp to a readable date string
                            val date = timestamp.toDate()
                            java.text.SimpleDateFormat("dd MMMM yyyy", java.util.Locale.US).format(date)
                        } else {
                            // Fall back to string if not a timestamp
                            document.getString("date") ?: ""
                        }
                    } catch (e: Exception) {
                        println("Error parsing date: ${e.message}")
                        ""  // Default empty string if parsing fails
                    }
                    
                    Visited(
                        soldId = document.getLong("sold_id")?.toInt() ?: 0,
                        date = dateStr,
                        pincode = document.getLong("pincode")?.toInt() ?: 0,
                        reason = document.getString("reason") ?: ""
                    )
                } catch (e: Exception) {
                    println("Error parsing visit: ${e.message}")
                    null
                }
            }
            
            // If we found visits for this soldier, return them
            // Otherwise, return random visits from the database
            if (soldierVisits.isNotEmpty()) {
                println("Returning ${soldierVisits.size} actual visits for soldier ID $id")
                soldierVisits
            } else if (allVisits.isNotEmpty()) {
                // Return 2 random visits from all visits
                val randomVisits = allVisits.shuffled().take(2)
                println("No visits found for soldier ID $id, returning ${randomVisits.size} random visits")
                randomVisits
            } else {
                println("No visits found in the database")
                emptyList()
            }
        } catch (e: Exception) {
            println("Error fetching visits: ${e.message}")
            emptyList()
        }
    }

    /**
     * Fetch location details by pincode
     */
    suspend fun getLocationByPincode(pincode: Int): Location? {
        return try {
            val snapshot = firestore.collection("Location")
                .whereEqualTo("pincode", pincode)
                .get()
                .await()

            if (snapshot.isEmpty) return null

            val document = snapshot.documents.first()
            Location(
                country = document.getString("Country") ?: "",
                state = document.getString("State") ?: "",
                district = document.getString("district") ?: "",
                pincode = document.getLong("pincode")?.toInt() ?: 0
            )
        } catch (e: Exception) {
            null
        }
    }
    /**
     * Fetch battalion details by ID
     */
    suspend fun getBattalionById(id: Int): Battalion? {
        return try {
            val snapshot = firestore.collection("Battalion")
                .whereEqualTo("captain_id", id)
                .get()
                .await()

            if (snapshot.isEmpty) return null

            val document = snapshot.documents.first()
            Battalion(
                battalionName = document.getString("battalion_name") ?: "",
                captainId = document.getLong("captain_id")?.toInt() ?: 0,
                totalCapacity = document.getLong("total_capacity")?.toInt() ?: 0,
                year = document.getLong("year")?.toInt() ?: 0
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Fetch weapon details by ID
     */
    suspend fun getWeaponById(id: Int): Weapon? {
        return try {
            val snapshot = firestore.collection("weapon")
                .whereEqualTo("weapon_id", id)
                .get()
                .await()

            if (snapshot.isEmpty) return null

            val document = snapshot.documents.first()
            Weapon(
                weaponId = document.getLong("weapon_id")?.toInt() ?: 0,
                name = document.getString("name") ?: ""
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Fetch inventory by ID
     */
    suspend fun getInventoryById(id: Int): Inventory? {
        return try {
            val snapshot = firestore.collection("Inventory")
                .whereEqualTo("ID", id)
                .get()
                .await()

            if (snapshot.isEmpty) return null

            val document = snapshot.documents.first()
            Inventory(
                id = document.getLong("ID")?.toInt() ?: 0,
                weaponId = document.getLong("weapon_id")?.toInt() ?: 0
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Fetch war details by date
     */
    suspend fun getWarByDate(date: String): War? {
        return try {
            val snapshot = firestore.collection("war")
                .whereEqualTo("DateNo", date)
                .get()
                .await()

            if (snapshot.isEmpty) return null

            val document = snapshot.documents.first()
            War(
                dateNo = document.getString("DateNo") ?: "",
                status = document.getLong("Status")?.toInt() ?: 0,
                pincode = document.getLong("pincode")?.toInt() ?: 0
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Fetch medal details by name
     */
    suspend fun getMedalByName(name: String): Medal? {
        return try {
            val snapshot = firestore.collection("Medals")
                .whereEqualTo("Name", name)
                .get()
                .await()

            if (snapshot.isEmpty) return null

            val document = snapshot.documents.first()
            Medal(
                name = document.getString("Name") ?: ""
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Fetch all soldiers (with pagination if needed)
     */
    suspend fun getAllSoldiers(limit: Long = 20): List<Soldier> {
        return try {
            val snapshot = firestore.collection("soldier")
                .limit(limit)
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                Soldier(
                    id = document.getLong("id")?.toInt() ?: 0,
                    name = document.getString("name") ?: "",
                    rank = document.getString("Rank") ?: "",
                    sex = document.getString("Sex") ?: "",
                    height = document.getLong("height")?.toInt() ?: 0,
                    weight = document.getLong("Weight")?.toInt() ?: 0,
                    chest = document.getLong("Chest")?.toInt() ?: 0,
                    squadNo = document.getString("squadNo") ?: "",
                    birthPlacePincode = document.getLong("BirthPlacePincode")?.toInt() ?: 0,
                    doj = document.getString("DOJ") ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Search soldiers by name (partial match)
     */
    suspend fun searchSoldiersByName(name: String): List<Soldier> {
        return try {
            // Firestore doesn't support native LIKE queries, so we fetch all and filter
            // For a production app, consider using Algolia or a similar search service
            val snapshot = firestore.collection("soldier")
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                val soldierName = document.getString("name") ?: ""
                if (soldierName.contains(name, ignoreCase = true)) {
                    Soldier(
                        id = document.getLong("id")?.toInt() ?: 0,
                        name = soldierName,
                        rank = document.getString("Rank") ?: "",
                        sex = document.getString("Sex") ?: "",
                        height = document.getLong("height")?.toInt() ?: 0,
                        weight = document.getLong("Weight")?.toInt() ?: 0,
                        chest = document.getLong("Chest")?.toInt() ?: 0,
                        squadNo = document.getString("squadNo") ?: "",
                        birthPlacePincode = document.getLong("BirthPlacePincode")?.toInt() ?: 0,
                        doj = document.getString("DOJ") ?: ""
                    )
                } else null
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Singleton pattern
    companion object {
        @Volatile
        private var INSTANCE: FirebaseStorageHelper? = null

        fun getInstance(): FirebaseStorageHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = FirebaseStorageHelper()
                INSTANCE = instance
                instance
            }
        }
    }

    /**
     * Fetch all wars and categorize them by status
     */
    suspend fun getAllWars(): Pair<List<War>, List<War>> {
        return try {
            val snapshot = firestore.collection("war")
                .get()
                .await()

            val (pastWars, ongoingWars) = snapshot.documents.mapNotNull { document ->
                try {
                    // Handle date as Timestamp
                    val dateStr = try {
                        val timestamp = document.getTimestamp("DateNo")
                        if (timestamp != null) {
                            // Convert timestamp to a readable date string
                            val date = timestamp.toDate()
                            java.text.SimpleDateFormat("dd MMMM yyyy", java.util.Locale.US).format(date)
                        } else {
                            document.getString("DateNo") ?: ""
                        }
                    } catch (e: Exception) {
                        document.getString("DateNo") ?: ""
                    }

                    War(
                        dateNo = dateStr,
                        status = document.getLong("Status")?.toInt() ?: 0,
                        pincode = document.getLong("pincode")?.toInt() ?: 0
                    )
                } catch (e: Exception) {
                    println("Error parsing war document: ${e.message}")
                    null
                }
            }.partition { it.status == 0 } // 0 for past wars, 1 for ongoing

            Pair(pastWars, ongoingWars)
        } catch (e: Exception) {
            println("Error fetching wars: ${e.message}")
            Pair(emptyList(), emptyList())
        }
    }
}
