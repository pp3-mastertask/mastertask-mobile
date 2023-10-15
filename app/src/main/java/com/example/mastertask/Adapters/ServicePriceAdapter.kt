import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.R

class ServicePriceAdapter (private val list: List<Map<String?, Any?>>?) :
    RecyclerView.Adapter<ServicePriceAdapter.Price>()
{
    inner class Price(view: View) : RecyclerView.ViewHolder(view) {
        var habilidade: TextView
        var preco : TextView
        init {
            habilidade = view.findViewById<View>(R.id.service_name_item) as TextView
            preco = view.findViewById<View>(R.id.service_price_item) as TextView
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Price {

        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.service_badge,
                parent,
                false
            )

        return Price(itemView)
    }

    override fun onBindViewHolder(
        holder: Price,
        position: Int
    ) {
        holder.habilidade.text = list!![position].getValue("habilidade") as String?
        holder.preco.text = list!![position].getValue("preco") as String?
    }

    override fun getItemCount(): Int {
        return list!!.size
    }
}