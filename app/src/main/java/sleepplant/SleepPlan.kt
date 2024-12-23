package sleepplant

import achievement.Achivment
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import target.Target
import workout.Workout
import com.google.android.material.bottomnavigation.BottomNavigationView
import mealplant.MealPlant

class SleepPlan : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activty_sleepplan)  // Pastikan layout XML sudah benar

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

        // Button listener for navigating to SleepPlanForm
        val button = findViewById<Button>(R.id.button)  // ID sesuai dengan yang ada di layout XML Anda
        button.setOnClickListener {
            // Start SleepPlanForm Activity
            val intent = Intent(this, Sleepplanform::class.java)
            startActivity(intent)
        }
    }
}
