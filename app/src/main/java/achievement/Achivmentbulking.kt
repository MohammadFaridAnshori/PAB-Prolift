package achievement

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import mealplant.MealPlant
import sleepplant.SleepPlan
import target.Target
import workout.Workout

class Achivmentbulking : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activty_achivment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        //1. 22523190 Joaquin Brilliant Pramustya
        //2. 22523124 Mohammad Farid Anshori
        //3. 22523016 M. Da’il Falah
        //4. 22523171 Nurandra Satya Adhi

        // Set listener untuk menu item selection
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
                    // Tidak perlu berpindah ke activity ini jika sudah di sini
                    true
                }

                else -> false
            }
        }

        // Ambil referensi ke komponen UI
        val levelTextView: TextView = findViewById(R.id.level_text)
        val pointsTextView: TextView = findViewById(R.id.points_text)
        val progressBar: ProgressBar = findViewById(R.id.progress_bar)
        val gymCountTextView: TextView = findViewById(R.id.gym_count)
        val caloriesCountTextView: TextView = findViewById(R.id.calories_count)
        val proteinCountTextView: TextView = findViewById(R.id.protein_count)
        val sleepCountTextView: TextView = findViewById(R.id.sleep_plan_count)

        // Data dummy pencapaian
        val achievementData = mapOf(
            "level" to "Level 1",
            "pointsToNextLevel" to 500,
            "progress" to 70,
            "gymCount" to 3,
            "calories" to 2000,
            "protein" to 150,
            "sleep" to 7
        )

        // Update UI dengan data dummy
        levelTextView.text = achievementData["level"] as String
        pointsTextView.text = "${achievementData["pointsToNextLevel"]} Points to next level"
        progressBar.progress = achievementData["progress"] as Int
        gymCountTextView.text = "${achievementData["gymCount"]}x"
        caloriesCountTextView.text = "${achievementData["calories"]} Kcal"
        proteinCountTextView.text = "${achievementData["protein"]}g"
        sleepCountTextView.text = "${achievementData["sleep"]} hours"
    }
}
