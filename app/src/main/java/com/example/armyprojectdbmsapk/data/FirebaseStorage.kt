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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Helper class for Firebase Firestore operations.
 * Follows the Repository pattern to provide a clean API for data access.
 */
class FirebaseStorageHelper {
    private val firestore = FirebaseFirestore.getInstance()

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
    
    /**
     * Helper method to create a Soldier object from a document
     */
    private fun createSoldierFromDocument(document: com.google.firebase.firestore.DocumentSnapshot): Soldier {
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
        
            println("Creating Soldier with ID: $id, Name: $name, Rank: $rank, DOJ: $doj")
            
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
                doj = doj
            )
        } catch (e: Exception) {
            println("Error creating Soldier object: ${e.message}")
            e.printStackTrace()
            
            // Return a default soldier object to prevent crashes
            return Soldier(
                id = document.getLong("id")?.toInt() ?: 0,
                name = document.getString("name") ?: "",
                rank = "",
                sex = "",
                height = 0,
                weight = 0,
                chest = 0,
                squadNo = "",
                birthPlacePincode = 0,
                doj = ""
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
     * Fetch postings for a soldier by ID
     */
    suspend fun getPostingsForSoldier(id: Int): List<Posting> {
        return try {
            println("Fetching postings for soldier ID: $id")
            
            // First, let's get all postings to see what we have
            val allSnapshot = firestore.collection("posting")
                .get()
                .await()
                
            println("Total postings in database: ${allSnapshot.size()}")
            allSnapshot.documents.forEach { doc ->
                println("Found posting: ${doc.data}")
            }
            
            val snapshot = firestore.collection("posting")
                .whereEqualTo("id", id)
                .get()
                .await()

            println("Found ${snapshot.size()} postings for soldier ID $id")
            
            snapshot.documents.mapNotNull { document ->
                println("Processing posting document: ${document.data}")
                Posting(
                    id = document.getLong("id")?.toInt() ?: 0,
                    date = document.getString("date") ?: "",
                    pincode = document.getLong("pincode")?.toInt() ?: 0
                )
            }
        } catch (e: Exception) {
            println("Error fetching postings: ${e.message}")
            emptyList()
        }
    }

    /**
     * Fetch visits for a soldier by ID
     */
    suspend fun getVisitsForSoldier(id: Int): List<Visited> {
        return try {
            println("Fetching visits for soldier ID: $id")
            
            // First, let's get all visits to see what we have
            val allSnapshot = firestore.collection("visited")
                .get()
                .await()
                
            println("Total visits in database: ${allSnapshot.size()}")
            allSnapshot.documents.forEach { doc ->
                println("Found visit: ${doc.data}")
            }
            
            val snapshot = firestore.collection("visited")
                .whereEqualTo("sold_id", id)
                .get()
                .await()

            println("Found ${snapshot.size()} visits for soldier ID $id")
            
            snapshot.documents.mapNotNull { document ->
                println("Processing visit document: ${document.data}")
                Visited(
                    soldId = document.getLong("sold_id")?.toInt() ?: 0,
                    date = document.getString("date") ?: "",
                    pincode = document.getLong("pincode")?.toInt() ?: 0,
                    reason = document.getString("reason") ?: ""
                )
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
}
