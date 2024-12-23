package sleepplant

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Sleephistory : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance() // Firestore Instance

    //1. 22523190 Joaquin Brilliant Pramustya
    //2. 22523124 Mohammad Farid Anshori
    //3. 22523016 M. Da’il Falah
    //4. 22523171 Nurandra Satya Adhi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleephistory)

        val backButton: ImageButton = findViewById(R.id.backButton)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewSleepHistory)

        // Tombol Back
        backButton.setOnClickListener {
            finish()
        }

        // RecyclerView Setup
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load Sleep History Data
        loadSleepHistory(recyclerView)
    }

    private fun loadSleepHistory(recyclerView: RecyclerView) {
        val sharedPreferences = getSharedPreferences("Prolift", MODE_PRIVATE)
        val userId = sharedPreferences.getString("USER_DOCUMENT_ID", null)

        if (userId.isNullOrEmpty()) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("sleepPlans")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val sleepHistoryList = mutableListOf<SleepHistory2>()
                for (document in documents) {

                    val createdAt = document.getLong("createdAt") ?: 0L
                    val date = formatDate(createdAt)
                    val time = formatTime(createdAt)

                    val sleepTime = document.getString("sleepTime") ?: "Unknown"
                    val wakeTime = document.getString("wakeTime") ?: "Unknown"
                    sleepHistoryList.add(SleepHistory2(date, sleepTime, wakeTime, calculateSleepDuration(sleepTime.toLong(),wakeTime.toLong()).toString()))
                }
                recyclerView.adapter = SleepHistoryAdapter(sleepHistoryList)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading data: ${e.message}", Toast.LENGTH_SHORT).show()
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

    private fun calculateSleepDuration(sleepTime: Long, wakeTime: Long): Double {
        // Convert times to minutes
        val sleepHour = sleepTime / 100
        val sleepMinute = sleepTime % 100
        val wakeHour = wakeTime / 100
        val wakeMinute = wakeTime % 100

        // Calculate total minutes
        val sleepInMinutes = sleepHour * 60 + sleepMinute
        val wakeInMinutes = wakeHour * 60 + wakeMinute

        // Handle cross-midnight case
        val totalMinutes = if (wakeInMinutes < sleepInMinutes) {
            (wakeInMinutes + 24 * 60) - sleepInMinutes
        } else {
            wakeInMinutes - sleepInMinutes
        }

        // Convert minutes to hours
        return totalMinutes / 60.0
    }

}
