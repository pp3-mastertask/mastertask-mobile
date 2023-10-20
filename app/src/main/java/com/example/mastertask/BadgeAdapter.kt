import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.R

class BadgeAdapter(private val badgeList: List<String>) :
    RecyclerView.Adapter<BadgeAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView
        init{
            textView= itemView.findViewById(R.id.rvHabilidades) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val badge = badgeList[position]
        val badgeView = holder.itemView as TextView
        badgeView.text = badgeList[position]
    }

    override fun getItemCount(): Int{
        return badgeList.size
    }

}