package mealplant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

data class MealHistory(
    val date: String,
    val mealTime: String,
    val foodName: String,
    val calorie: String,
    val protein: String,
    val portion: String
)

class MealHistoryAdapter(private val mealHistoryList: List<MealHistory>) :
    RecyclerView.Adapter<MealHistoryAdapter.MealHistoryViewHolder>() {

    class MealHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.findViewById(R.id.tvDate)
        val mealTimeTextView: TextView = view.findViewById(R.id.tvMealTime)
        val foodNameTextView: TextView = view.findViewById(R.id.tvFoodName)
        val calorieTextView: TextView = view.findViewById(R.id.tvCalorie)
        val proteinTextView: TextView = view.findViewById(R.id.tvProtein)
        val portionTextView: TextView = view.findViewById(R.id.tvPortion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_meal_history, parent, false)
        return MealHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealHistoryViewHolder, position: Int) {
        val mealHistory = mealHistoryList[position]
        holder.dateTextView.text = "Date "+mealHistory.date
        holder.mealTimeTextView.text = "Meal Time "+mealHistory.mealTime
        holder.foodNameTextView.text = "Food "+mealHistory.foodName
        holder.calorieTextView.text = "Calorie "+mealHistory.calorie
        holder.proteinTextView.text = "Protein "+mealHistory.protein
        holder.portionTextView.text = "Portion "+mealHistory.portion
    }

    override fun getItemCount(): Int = mealHistoryList.size
}
