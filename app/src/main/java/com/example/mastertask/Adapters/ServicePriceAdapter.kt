import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.R
import java.text.NumberFormat
import java.util.Currency

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
                R.layout.service_price,
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
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.setMaximumFractionDigits(2)
        format.setCurrency(Currency.getInstance("BRL"))
        var preco = list[position].getValue("preco")
        if (preco is Long)
            holder.preco.text = format.format(preco as Long?)
        else
            holder.preco.text = format.format(preco as Double?)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }
}