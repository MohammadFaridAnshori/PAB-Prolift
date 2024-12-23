package mealplant

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class MealPlantHistory : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_plant_history)

        val backButton: ImageButton = findViewById(R.id.backButton)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewHistory)

        // Tombol Back
        backButton.setOnClickListener {
            Toast.makeText(this, "Back button clicked", Toast.LENGTH_SHORT).show()
            finish()
        }

        // RecyclerView Setup
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load Data from Firestore
        loadMealHistory(recyclerView)
    }

    private fun loadMealHistory(recyclerView: RecyclerView) {
        val sharedPreferences = getSharedPreferences("Prolift", MODE_PRIVATE)
        val userId = sharedPreferences.getString("USER_DOCUMENT_ID", null)

        if (userId.isNullOrEmpty()) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("mealPlans")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val mealHistoryList = mutableListOf<MealHistory>()

                for (document in documents) {
                    val createdAt = document.getLong("createdAt") ?: 0L
                    val date = formatDate(createdAt)
                    val time = formatTime(createdAt)
                    mealHistoryList.add(
                        MealHistory(
                            date = date,
                            mealTime = time,
                            foodName = document.getString("mealName") ?: "Unknown",
                            calorie = document.getString("calori") ?: "0",
                            protein = document.getString("protein") ?: "0g",
                            portion = document.getString("portion") ?: "0"
                        )
                    )
                }

                recyclerView.adapter = MealHistoryAdapter(mealHistoryList)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load meal history: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    private fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
