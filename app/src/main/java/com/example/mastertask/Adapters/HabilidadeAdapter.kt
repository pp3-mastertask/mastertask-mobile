import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.R

class HabilidadeAdapter(private val onItemClick: (String) -> Unit) :
    ListAdapter<String, HabilidadeAdapter.HabilidadeViewHolder>(HabilidadeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabilidadeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_selected_service, parent, false)//n tenho certeza se é o fragmente selected servicekkkkkkkkk
        return HabilidadeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabilidadeViewHolder, position: Int) {
        val habilidade = getItem(position)
        holder.bind(habilidade)
    }

    inner class HabilidadeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewHabilidade: TextView = itemView.findViewById(R.id.add_skill_name)//nao tenho certeza se é issokkkkkkk

        fun bind(habilidade: String) {
            textViewHabilidade.text = habilidade

            itemView.setOnClickListener {
                onItemClick(habilidade)
            }
        }
    }

    private class HabilidadeDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}
