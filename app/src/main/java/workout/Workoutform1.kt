package workout

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore

class Workoutform1 : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()
    private var workoutId: String = "defaultWorkoutId" // Variabel untuk mengatur ID workout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workoutform1)

        // Back button functionality
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Kembali ke aktivitas sebelumnya
        }

        // Example to handle TextView updates
        val titleTextView: TextView = findViewById(R.id.title)
        val descriptionTextView: TextView = findViewById(R.id.description)

        // Update title and description dynamically if needed
        titleTextView.text = "Barbell Rows"
        descriptionTextView.text = "\"Barbel Rows\" refers to a series of exercises designed to stretch and lengthen the muscles throughout the entire body. This practice aims to improve flexibility, increase range of motion, and promote overall muscle relaxation."

        // Button Done functionality
        val buttonDone: Button = findViewById(R.id.buttondone)
        buttonDone.setOnClickListener {
            saveWorkoutToFirestore()
        }
    }

    private fun saveWorkoutToFirestore() {

        setWorkoutId("bboY9aLM6g7IxNTrFJLD")
        // Contoh data yang akan disimpan
        val workoutData = mapOf(
            "WorkoutId" to workoutId,
            "createdAt" to System.currentTimeMillis() // Timestamp
        )

        // Menentukan path: user/<userId>/workout/<workoutId>
        val sharedPreferences = getSharedPreferences("Prolift", MODE_PRIVATE)
        val userId = sharedPreferences.getString("USER_DOCUMENT_ID", null)

        val workoutCollectionRef = userId?.let { firestore.collection("user").document(it).collection("workout") }

        if (workoutCollectionRef != null) {
            workoutCollectionRef
                .add(workoutData)
                ?.addOnSuccessListener {
                    Toast.makeText(this, "Workout saved successfully!", Toast.LENGTH_SHORT).show()
                }
                ?.addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to save workout: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Fungsi untuk mengubah ID workout
    private fun setWorkoutId(newId: String) {
        workoutId = newId
    }
}
