package achievement

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import mealplant.MealPlant
import sleepplant.SleepPlan
import target.Target
import workout.Workout
import java.text.SimpleDateFormat
import java.util.*

class Achivment : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var userId: String // Replace with actual userId logic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activty_achivment)

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
                    // Already on this activity
                    true
                }

                else -> false
            }
        }
        val titleTextView: TextView = findViewById(R.id.title) // Referensi TextView dengan ID title


        // Get shared preferences
        val sharedPreferences = getSharedPreferences("Prolift", MODE_PRIVATE)
        userId = sharedPreferences.getString("USER_DOCUMENT_ID", null).toString()
        // Fetch target and update title and achievements
        fetchTargetAndSetTitle(titleTextView) { target ->
            // Fetch and update achievements after retrieving the target
            fetchAchievementData(target)
        }
    }

    private fun fetchAchievementData(target: String) {
        val today = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date()).lowercase()

        // UI References
        val levelTextView: TextView = findViewById(R.id.level_text)
        val pointsTextView: TextView = findViewById(R.id.points_text)
        val progressBar: ProgressBar = findViewById(R.id.progress_bar)
        val gymCountTextView: TextView = findViewById(R.id.gym_count)
        val caloriesCountTextView: TextView = findViewById(R.id.calories_count)
        val proteinCountTextView: TextView = findViewById(R.id.protein_count)
        val sleepCountTextView: TextView = findViewById(R.id.sleep_plan_count)

        // Retrieve user's current level (example: fetched from shared preferences or Firestore)
        val sharedPreferences = getSharedPreferences("Prolift", MODE_PRIVATE)
        val currentLevel = sharedPreferences.getInt("USER_LEVEL", 1) // Default to 1 if not found

        // Dynamically calculate targets
        val targetSleep = (if (target == "bulking") 8.0 else 7.0) + (currentLevel * 0.5)
        val targetCalories = (if (target == "bulking") 3000.0 else 2000.0) + (currentLevel * 200.0)
        val targetProtein = (if (target == "bulking") 150.0 else 100.0) + (currentLevel * 10.0)
        val targetWorkout = (if (target == "bulking") 4 else 6) + (currentLevel / 2)

        // Initialize progress values
        var sleepProgress = 0.0
        var caloriesProgress = 0.0
        var proteinProgress = 0.0
        var workoutProgress = 0.0

        // Fetch workout data
        db.collection("user").document(userId).collection("workout")
            .get()
            .addOnSuccessListener { documents ->
                var gymCount = 0
                for (document in documents) {
                    val createdAt = document.getLong("createdAt")
                    val date = formatDate(createdAt)
                    if (date == today) {
                        gymCount++
                    }
                }
                gymCountTextView.text = "$gymCount/${targetWorkout}"
                workoutProgress = (gymCount.toDouble() / targetWorkout) * 100
                updateProgressBar(progressBar, sleepProgress, caloriesProgress, proteinProgress, workoutProgress,target)
            }
            .addOnFailureListener {
                gymCountTextView.text = "0/${targetWorkout}"
            }

        // Fetch meal plan data
        db.collection("mealPlans").whereEqualTo("userId", userId).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val createdAt = document.getLong("createdAt")
                    val date = formatDate(createdAt)
                    if (date == today) {
                        val protein = document.getString("protein")?.toDouble() ?: 0.0
                        val calories = document.getString("calori")?.toDouble() ?: 0.0
                        proteinCountTextView.text = "${protein}g/${targetProtein}"
                        caloriesCountTextView.text = "${calories} Kcal/${targetCalories}"

                        proteinProgress = (protein / targetProtein) * 100
                        caloriesProgress = (calories / targetCalories) * 100
                        updateProgressBar(progressBar, sleepProgress, caloriesProgress, proteinProgress, workoutProgress,target)
                    }
                }
            }
            .addOnFailureListener {
                proteinCountTextView.text = "0g/${targetProtein}"
                caloriesCountTextView.text = "0 Kcal/${targetCalories}"
            }

        // Fetch sleep plan data
        db.collection("sleepPlans").whereEqualTo("userId", userId).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val createdAt = document.getLong("createdAt")
                    val date = formatDate(createdAt)
                    if (date == today) {
                        val sleepDuration = document.getString("wakeTime")?.let {
                            calculateSleepDuration(
                                document.getString("sleepTime")!!.toLong(),
                                it.toLong()
                            )
                        } ?: 0.0
                        sleepCountTextView.text = "$sleepDuration hours/${targetSleep}"

                        sleepProgress = (sleepDuration / targetSleep) * 100
                        updateProgressBar(progressBar, sleepProgress, caloriesProgress, proteinProgress, workoutProgress,target)
                    }
                }
            }
            .addOnFailureListener {
                sleepCountTextView.text = "0 hours/${targetSleep}"
            }

        // Set current level in UI
        levelTextView.text = "Level $currentLevel"
        pointsTextView.text = "500 Points to next level" // Example text; adjust as needed
    }

    private fun updateProgressBar(progressBar: ProgressBar, sleep: Double, calories: Double, protein: Double, workout: Double,target:String) {
        // Calculate total progress (average of all metrics)
        val totalProgress = (sleep + calories + protein + workout) / 4
        progressBar.progress = totalProgress.toInt()

        // Optional: Level up if progress reaches 100%
        if (totalProgress >= 100) {
            levelUp(target)
        }
    }

    private fun levelUp(target: String) {
        val sharedPreferences = getSharedPreferences("Prolift", MODE_PRIVATE)
        val currentLevel = sharedPreferences.getInt("USER_LEVEL", 1)
        val newLevel = currentLevel + 1

        // Update level in shared preferences
        sharedPreferences.edit().putInt("USER_LEVEL", newLevel).apply()

        // Show a congratulatory message
        Toast.makeText(this, "Congratulations! You've reached Level $newLevel!", Toast.LENGTH_SHORT).show()

        // Reload achievement data with new level
        fetchAchievementData(target) // Replace "bulking" with the user's current target
    }

    private fun formatDate(timestamp: Long?): String {
        if (timestamp == null) return "unknown"
        val date = Date(timestamp)
        return SimpleDateFormat("EEEE", Locale.getDefault()).format(date).lowercase()
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

    private fun fetchTargetAndSetTitle(titleTextView: TextView, callback: (String) -> Unit) {
        db.collection("user").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val target = document.getString("target") ?: "your goal"
                    titleTextView.text = "Achievements for $target" // Set title dynamically
                    callback(target.lowercase()) // Pass the target to the callback
                } else {
                    val defaultTarget = "your goal"
                    titleTextView.text = "Achievements for $defaultTarget" // Default title
                    callback(defaultTarget) // Pass the default target
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error fetching target: ${exception.message}", Toast.LENGTH_SHORT).show()
                val defaultTarget = "your goal"
                callback(defaultTarget) // Pass the default target
            }
    }
}
