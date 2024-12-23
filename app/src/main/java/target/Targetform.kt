package target

import achievement.Achivment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import mealplant.MealPlant
import sleepplant.SleepPlan
import workout.Workout
import java.text.SimpleDateFormat
import java.util.*

class Targetform : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()


    private lateinit var userId:String // Replace with actual userId logic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_targetform)

        // Set listener for menu item selection in BottomNavigationView

        //1. 22523190 Joaquin Brilliant Pramustya
        //2. 22523124 Mohammad Farid Anshori
        //3. 22523016 M. Da’il Falah
        //4. 22523171 Nurandra Satya Adhi

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)


        fun updateTodayText() {
            val today = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date()).lowercase()
            val programText = findViewById<TextView>(R.id.program)
            val proteinText = findViewById<TextView>(R.id.protein)
            val caloriesText = findViewById<TextView>(R.id.calories)
            val sleepText = findViewById<TextView>(R.id.sleepschedule)
            val workoutText = findViewById<TextView>(R.id.workoutvariation)

            programText.text = "Program: Today"
            proteinText.text = "Protein: Loading..."
            caloriesText.text = "Calories: Loading..."
            sleepText.text = "Sleep: Loading..."
            workoutText.text = "Workout: Loading..."


            db.collection("user").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val tvTarget = findViewById<TextView>(R.id.program)
                        tvTarget.text ="target: "+ document.getString("target")

                    } else {
                        Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error loading user data: ${exception.message}", Toast.LENGTH_SHORT).show()
                }

            db.collection("mealPlans").whereEqualTo("userId", userId).get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val createdAt = document.getLong("createdAt")
                        val date = formatDate(createdAt)
                        if (date == today) {
                            proteinText.text = "Protein: ${document.getString("protein") ?: 0.0}"
                            caloriesText.text = "Calories: ${document.getString("calori") ?: 0.0}"
                        }
                    }
                }

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
                            }
                            sleepText.text = "Sleep: $sleepDuration hrs"
                        }
                    }
                }

            db.collection("user").document(userId).collection("workout").get()
                .addOnSuccessListener { documents ->
                    var workoutCount = 0
                    for (document in documents) {
                        val createdAt = document.getLong("createdAt")
                        val date = formatDate(createdAt)
                        if (date == today) {
                            workoutCount++
                        }
                    }
                    workoutText.text = "Workout: $workoutCount"
                }
        }
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

        val sharedPreferences = getSharedPreferences("Prolift", MODE_PRIVATE)
        userId = sharedPreferences.getString("USER_DOCUMENT_ID", null).toString()

        updateTodayText()
        fetchMealPlans()
        fetchSleepPlans()
        fetchWorkouts()
    }

    private fun fetchMealPlans() {
        db.collection("mealPlans")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val dayMap = initializeDayMap()

                for (document in documents) {
                    val createdAt = document.getLong("createdAt")
                    val date = formatDate(createdAt)
                    when (date) {
                        "monday" -> {
                            val mondayProtein = findViewById<TextView>(R.id.mondayprotein)
                            val mondayCalories = findViewById<TextView>(R.id.mondaycalories)
                            mondayProtein.text = "Monday: ${document.getString("protein") ?: 0.0}"
                            mondayCalories.text = "Monday: ${document.getString("calori") ?: 0.0}"
                        }
                        "tuesday" -> {
                            val tuesdayProtein = findViewById<TextView>(R.id.tuesdayprotein)
                            val tuesdayCalories = findViewById<TextView>(R.id.tuesdaycalories)
                            tuesdayProtein.text = "Tuesday: ${document.getString("protein") ?: 0.0}"
                            tuesdayCalories.text = "Tuesday: ${document.getString("calori") ?: 0.0}"
                        }
                        "wednesday" -> {
                            val wednesdayProtein = findViewById<TextView>(R.id.wednesdayprotein)
                            val wednesdayCalories = findViewById<TextView>(R.id.wednesdaycalories)
                            wednesdayProtein.text = "Wednesday: ${document.getString("protein") ?: 0.0}"
                            wednesdayCalories.text = "Wednesday: ${document.getString("calori") ?: 0.0}"
                        }
                        "thursday" -> {
                            val thursdayProtein = findViewById<TextView>(R.id.thursdayprotein)
                            val thursdayCalories = findViewById<TextView>(R.id.thursdaycalories)
                            thursdayProtein.text = "Thursday: ${document.getString("protein") ?: 0.0}"
                            thursdayCalories.text = "Thursday: ${document.getString("calori") ?: 0.0}"
                        }
                        "friday" -> {
                            val fridayProtein = findViewById<TextView>(R.id.fridayprotein)
                            val fridayCalories = findViewById<TextView>(R.id.fridaycalories)
                            fridayProtein.text = "Friday: ${document.getString("protein") ?: 0.0}"
                            fridayCalories.text = "Friday: ${document.getString("calori") ?: 0.0}"
                        }
                        "saturday" -> {
                            val saturdayProtein = findViewById<TextView>(R.id.saturdayprotein)
                            val saturdayCalories = findViewById<TextView>(R.id.saturdaycalories)
                            saturdayProtein.text = "Saturday: ${document.getString("protein") ?: 0.0}"
                            saturdayCalories.text = "Saturday: ${document.getString("calori") ?: 0.0}"
                        }
                        "sunday" -> {
                            val sundayProtein = findViewById<TextView>(R.id.sundayprotein)
                            val sundayCalories = findViewById<TextView>(R.id.sundaycalories)
                            sundayProtein.text = "Sunday: ${document.getString("protein") ?: 0.0}"
                            sundayCalories.text = "Sunday: ${document.getString("calori") ?: 0.0}"
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error loading meal plans: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchSleepPlans() {
        db.collection("sleepPlans")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val dayMap = initializeDayMap()

                for (document in documents) {
                    val createdAt = document.getLong("createdAt")
                    val date = formatDate(createdAt)
                    when (date) {
                        "monday" -> {
                            val mondaySleep = findViewById<TextView>(R.id.mondaysleep)
                            val sleepDuration = document.getString("sleepTime")?.let {
                                calculateSleepDuration(
                                    it.toLong(),
                                    document.getString("wakeTime")!!.toLong()
                                )
                            }
                            mondaySleep.text = "Monday: $sleepDuration hrs"
                        }
                        "tuesday" -> {
                            val tuesdaySleep = findViewById<TextView>(R.id.tuesdaysleep)
                            val sleepDuration = document.getString("sleepTime")?.let {
                                calculateSleepDuration(
                                    it.toLong(),
                                    document.getString("wakeTime")!!.toLong()
                                )
                            }
                            tuesdaySleep.text = "Tuesday: $sleepDuration hrs"
                        }
                        "wednesday" -> {
                            val wednesdaySleep = findViewById<TextView>(R.id.wednesdasleep)
                            val sleepDuration = document.getString("sleepTime")?.let {
                                calculateSleepDuration(
                                    it.toLong(),
                                    document.getString("wakeTime")!!.toLong()
                                )
                            }
                            wednesdaySleep.text = "Wednesday: $sleepDuration hrs"
                        }
                        "thursday" -> {
                            val thursdaySleep = findViewById<TextView>(R.id.thursdaysleep)
                            val sleepDuration = document.getString("sleepTime")?.let {
                                calculateSleepDuration(
                                    it.toLong(),
                                    document.getString("wakeTime")!!.toLong()
                                )
                            }
                            thursdaySleep.text = "Thursday: $sleepDuration hrs"
                        }
                        "friday" -> {
                            val fridaySleep = findViewById<TextView>(R.id.fridaysleep)
                            val sleepDuration = document.getString("sleepTime")?.let {
                                calculateSleepDuration(
                                    it.toLong(),
                                    document.getString("wakeTime")!!.toLong()
                                )
                            }
                            fridaySleep.text = "Friday: $sleepDuration hrs"
                        }
                        "saturday" -> {
                            val saturdaySleep = findViewById<TextView>(R.id.saturdaysleep)
                            val sleepDuration = document.getString("sleepTime")?.let {
                                calculateSleepDuration(
                                    it.toLong(),
                                    document.getString("wakeTime")!!.toLong()
                                )
                            }
                            saturdaySleep.text = "Saturday: $sleepDuration hrs"
                        }
                        "sunday" -> {
                            val sundaySleep = findViewById<TextView>(R.id.sundaysleep)
                            val sleepDuration = document.getString("sleepTime")?.let {
                                calculateSleepDuration(
                                    it.toLong(),
                                    document.getString("wakeTime")!!.toLong()
                                )
                            }
                            sundaySleep.text = "Sunday: $sleepDuration hrs"
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error loading sleep plans: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchWorkouts() {
        db.collection("user").document(userId).collection("workout")
            .get()
            .addOnSuccessListener { documents ->
                val dayMap = initializeDayMap()

                for (document in documents) {
                    val createdAt = document.getLong("createdAt")
                    val date = formatDate(createdAt)
                    when (date) {
                        "monday" -> {
                            val mondayWorkout = findViewById<TextView>(R.id.mondayworkout)
                            val currentCount =
                                mondayWorkout.text.toString().replace("Workout: ", "").toIntOrNull()
                                    ?: 0
                            mondayWorkout.text = "Monday: ${currentCount + 1}"
                        }

                        "tuesday" -> {
                            val tuesdayWorkout = findViewById<TextView>(R.id.tuesdayworkout)
                            val currentCount =
                                tuesdayWorkout.text.toString().replace("Workout: ", "")
                                    .toIntOrNull() ?: 0
                            tuesdayWorkout.text = "Tuesday: ${currentCount + 1}"
                        }

                        "wednesday" -> {
                            val wednesdayWorkout = findViewById<TextView>(R.id.wednesdayworkout)
                            val currentCount =
                                wednesdayWorkout.text.toString().replace("Workout: ", "")
                                    .toIntOrNull() ?: 0
                            wednesdayWorkout.text = "Wednesday: ${currentCount + 1}"
                        }

                        "thursday" -> {
                            val thursdayWorkout = findViewById<TextView>(R.id.thursdayworkout)
                            val currentCount =
                                thursdayWorkout.text.toString().replace("Workout: ", "")
                                    .toIntOrNull() ?: 0
                            thursdayWorkout.text = "Thursday: ${currentCount + 1}"
                        }

                        "friday" -> {
                            val fridayWorkout = findViewById<TextView>(R.id.fridayworkout)
                            val currentCount =
                                fridayWorkout.text.toString().replace("Workout: ", "").toIntOrNull()
                                    ?: 0
                            fridayWorkout.text = "Friday: ${currentCount + 1}"
                        }

                        "saturday" -> {
                            val saturdayWorkout = findViewById<TextView>(R.id.saturdayworkout)
                            val currentCount =
                                saturdayWorkout.text.toString().replace("Workout: ", "")
                                    .toIntOrNull() ?: 0
                            saturdayWorkout.text = "Workout: ${currentCount + 1}"
                        }

                        "sunday" -> {
                            val sundayWorkout = findViewById<TextView>(R.id.sundayworkout)
                            val currentCount =
                                sundayWorkout.text.toString().replace("Workout: ", "").toIntOrNull()
                                    ?: 0
                            sundayWorkout.text = "Workout: ${currentCount + 1}"
                        }
                    }
                }
            }
        }
    private fun initializeDayMap(): Map<String, View> {
        val days = listOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")
        return days.associateWith { findViewById(resources.getIdentifier(it, "id", packageName)) }
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

}
