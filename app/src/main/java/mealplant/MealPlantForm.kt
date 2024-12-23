package mealplant

import achievement.Achivment
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore
import mealplant.MealPlantHistory
import sleepplant.SleepPlan
import target.Target
import workout.Workout

class MealPlantForm : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance() // Inisialisasi Firestore

    //1. 22523190 Joaquin Brilliant Pramustya
    //2. 22523124 Mohammad Farid Anshori
    //3. 22523016 M. Da’il Falah
    //4. 22523171 Nurandra Satya Adhi

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_plant_form)

        // Ambil User ID dari SharedPreferences
        val sharedPreferences = getSharedPreferences("Prolift", MODE_PRIVATE)
        val userId = sharedPreferences.getString("USER_DOCUMENT_ID", null)

        if (userId.isNullOrEmpty()) {
            Toast.makeText(this, "User not logged in. Please login again.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Set listener for menu item selection in BottomNavigationView
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

        // Set listener for back button
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish()  // Finish current activity and return to the previous one
        }
        val historyButton = findViewById<Button>(R.id.btnHistory)
        historyButton.setOnClickListener {
            val intent = Intent(this, MealPlantHistory::class.java)
            startActivity(intent)
        }

        // Find the button in your layout and set its click listener
        val btnCreate = findViewById<Button>(R.id.btnCreate)
        val mealNameField = findViewById<EditText>(R.id.editTextFoodName)
        val caloriField = findViewById<EditText>(R.id.editTextCalories)
        val proteinField = findViewById<EditText>(R.id.editTextProtein)
        val portionField = findViewById<EditText>(R.id.editTextPortion)

        btnCreate.setOnClickListener {
            val mealName = mealNameField.text.toString()
            val calori = caloriField.text.toString()
            val protein = proteinField.text.toString()
            val portion = portionField.text.toString()

            if (mealName.isEmpty() || calori.isEmpty() || protein.isEmpty() || portion.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Data to be saved
            val mealPlanData = hashMapOf(
                "userId" to userId,
                "mealName" to mealName,
                "calori" to calori,
                "protein" to protein,
                "portion" to portion,
                "createdAt" to System.currentTimeMillis() // Optional: Add a timestamp
            )

            // Save to Firestore
            db.collection("mealPlans")
                .add(mealPlanData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Meal Plan saved successfully!", Toast.LENGTH_SHORT).show()
                    // Navigate to MealPlantHistory
                    val intent = Intent(this, MealPlantHistory::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error saving meal plan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
