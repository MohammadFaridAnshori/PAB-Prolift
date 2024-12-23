package sleepplant

import achievement.Achivment
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import mealplant.MealPlant
import mealplant.MealPlantHistory
import target.Target
import workout.Workout

class Sleepplanform : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance() // Inisialisasi Firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleepplanform)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        //1. 22523190 Joaquin Brilliant Pramustya
        //2. 22523124 Mohammad Farid Anshori
        //3. 22523016 M. Da’il Falah
        //4. 22523171 Nurandra Satya Adhi

        // Set listener for menu item selection
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

        // Initialize the form fields and button
        val dateInput: EditText = findViewById(R.id.date_input)
        val sleepTimeInput: EditText = findViewById(R.id.sleep_time_input)
        val wakeTimeInput: EditText = findViewById(R.id.wake_time_input)
        val createButton: Button = findViewById(R.id.create_button)
        val historyButton = findViewById<Button>(R.id.history)
        historyButton.setOnClickListener {
            val intent = Intent(this, Sleephistory::class.java)
            startActivity(intent)
        }

        createButton.setOnClickListener {
            val date = dateInput.text.toString()
            val sleepTime = sleepTimeInput.text.toString()
            val wakeTime = wakeTimeInput.text.toString()

            if (date.isNotEmpty() && sleepTime.isNotEmpty() && wakeTime.isNotEmpty()) {
                // Ambil User ID dari SharedPreferences
                val sharedPreferences = getSharedPreferences("Prolift", MODE_PRIVATE)
                val userId = sharedPreferences.getString("USER_DOCUMENT_ID", null)

                if (userId.isNullOrEmpty()) {
                    Toast.makeText(this, "User not logged in. Please login again.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Data to be saved in Firestore
                val sleepPlanData = hashMapOf(
                    "userId" to userId,
                    "date" to date,
                    "sleepTime" to sleepTime,
                    "wakeTime" to wakeTime,
                    "createdAt" to System.currentTimeMillis() // Timestamp
                )

                // Save to Firestore
                db.collection("sleepPlans")
                    .add(sleepPlanData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Sleep Plan Saved!", Toast.LENGTH_SHORT).show()

                        // Navigate to Sleephistory activity
                        val intent = Intent(this, Sleephistory::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error saving sleep plan: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
