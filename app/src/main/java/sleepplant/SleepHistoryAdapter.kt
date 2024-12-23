package sleepplant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

data class SleepHistory2(
    val date: String,
    val sleepTime: String,
    val wakeTime: String,
    val total:String
)

class SleepHistoryAdapter(private val sleepHistoryList: List<SleepHistory2>) :
    RecyclerView.Adapter<SleepHistoryAdapter.SleepHistoryViewHolder>() {

    class SleepHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.findViewById(R.id.tvDate)
        val total: TextView = view.findViewById(R.id.sleeptotal)
        val sleepTimeTextView: TextView = view.findViewById(R.id.tvSleepTime)
        val wakeTimeTextView: TextView = view.findViewById(R.id.tvWakeTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sleep_history, parent, false)
        return SleepHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SleepHistoryViewHolder, position: Int) {
        val sleepHistory = sleepHistoryList[position]
        holder.dateTextView.text = "date"+sleepHistory.date
        holder.sleepTimeTextView.text = "sleep Time"+ sleepHistory.sleepTime
        holder.wakeTimeTextView.text = "wake Time"+sleepHistory.wakeTime
        holder.total.text = "Sleep Total"+sleepHistory.total
    }

    override fun getItemCount(): Int = sleepHistoryList.size
}
