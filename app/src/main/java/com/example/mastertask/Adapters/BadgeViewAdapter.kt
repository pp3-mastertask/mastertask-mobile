package com.example.mastertask.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.R

class BadgeViewAdapter (private val list: List<Map<String?, Any?>>?,
                        private val listener: OnBadgeClickListener?) :
    RecyclerView.Adapter<BadgeViewAdapter.Badge>()
{
    inner class Badge(view: View) : RecyclerView.ViewHolder(view) {
        var habilidade: TextView
        init {
            habilidade = view.findViewById<View>(R.id.lbServiceBadge) as TextView
        }

        fun bind(hab: Map<String?, Any?>, listener: OnBadgeClickListener?, position: Int) {
            habilidade.text = hab.getValue("habilidade") as String?

            if (listener != null)
                habilidade.setOnClickListener { listener.onBadgeClick(hab, position, habilidade) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Badge {

        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.service_badge,
                parent,
                false
            )

        return Badge(itemView)
    }

    override fun onBindViewHolder(
        holder: Badge,
        position: Int
    ) {
        holder.bind(list!![position], listener, position)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    interface OnBadgeClickListener {
        fun onBadgeClick (badge: Map<String?, Any?>, position: Int, view: View)
    }
}