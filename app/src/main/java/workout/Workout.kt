package workout


import achievement.Achivment
import android.annotation.SuppressLint
import android.content.Intent
import java.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import mealplant.MealPlant
import sleepplant.SleepPlan
import target.Target
import java.util.Date
import java.util.Locale

class Workout : AppCompatActivity() {
    companion object {
        private const val TAG = "WorkoutActivity"
    }
    private val db = FirebaseFirestore.getInstance() // Inisialisasi Firestore
    private lateinit var documentId: String // ID pengguna yang login

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        // Handle BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bworkout -> {
                    startActivity(Intent(this, Workout::class.java))
                    true
                }
                R.id.bmealplan -> {
                    startActivity(Intent(this, MealPlant::class.java))
                    true
                }
                R.id.bsleep -> {
                    startActivity(Intent(this, SleepPlan::class.java))
                    true
                }
                R.id.btarget -> {
                    startActivity(Intent(this, Target::class.java))
                    true
                }
                R.id.bachievement -> {
                    startActivity(Intent(this, Achivment::class.java))
                    true
                }
                else -> false
            }
        }

        // Find views by ID
        val backButton: ImageButton = findViewById(R.id.backButton)
        val gambar1: Button = findViewById(R.id.button)
        val gambar2: Button = findViewById(R.id.button2)
        val gambar3: Button = findViewById(R.id.button3)
        val gambar4: Button = findViewById(R.id.button4)
        val profileButton: ImageButton = findViewById(R.id.imageButton)

        val todayWorkout:TextView = findViewById(R.id.finishedworkout)
        // Ambil TextView untuk Greeting
        val greetingUser: TextView = findViewById(R.id.greetinguser)

        // Ambil USER_DOCUMENT_ID dari SharedPreferences
        val sharedPreferences = getSharedPreferences("Prolift", MODE_PRIVATE)
        documentId  = sharedPreferences.getString("USER_DOCUMENT_ID", null).toString()
        // Load User Data from Firestore
        db.collection("user").document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Retrieve and display the username
                    val username = document.getString("username") ?: "User"
                    greetingUser.text = "Hello, $username!" // Set greeting text

                    // Now fetch the workout count for today
                    db.collection("user").document(documentId).collection("workout")
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                .format(Date()) // Get today's date in yyyy-MM-dd format

                            val countToday = querySnapshot.documents.count { workoutDocument ->
                                val createdAtMillis = workoutDocument.getLong("createdAt")
                                if (createdAtMillis != null) {
                                    val createdAtDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                        .format(Date(createdAtMillis)) // Convert millis to Date and format
                                    createdAtDate == today
                                } else {
                                    false
                                }
                            }

                            // Update the UI with the workout count
                            todayWorkout.text = "$countToday"
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Error loading workouts: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }

                } else {
                    Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error loading user data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }






        // Set click listeners for buttons
        profileButton.setOnClickListener {
            if (documentId.isEmpty() || documentId == "null") {
                // If no user is logged in, redirect to LoginActivity
                Toast.makeText(this, "Please log in first.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, profile.LoginActivity::class.java))
            } else {
                // If user is logged in, redirect to Profile2Activity
                startActivity(Intent(this, profile.Profile2Activity::class.java))
            }
        }



        backButton.setOnClickListener {
            Log.d(TAG, "Back button clicked")
        }

        gambar1.setOnClickListener {
            Log.d(TAG, "Workout button clicked")
            startActivity(Intent(this, Workoutform::class.java))
        }
        gambar2.setOnClickListener {
            Log.d(TAG, "Squat Exercise button clicked (Workoutform1)")
            startActivity(Intent(this, workout.Workoutform1::class.java))
        }
        gambar3.setOnClickListener {
            Log.d(TAG, "Push-Up Exercise button clicked (Workoutform2)")
            startActivity(Intent(this, Workoutform2::class.java))
        }
        gambar4.setOnClickListener {
            Log.d(TAG, "Plank Exercise button clicked (Workoutform3)")
            startActivity(Intent(this, Workoutform3::class.java))
        }
    }
}