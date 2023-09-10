package com.example.mastertask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BadgeViewAdapter (private val list: List<String>) :
    RecyclerView.Adapter<BadgeViewAdapter.Badge>()
{
    inner class Badge(view: View) : RecyclerView.ViewHolder(view) {
        var habilidade: TextView
        init {
            habilidade = view.findViewById<View>(R.id.lbServiceBadge) as TextView
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Badge {

        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.fragment_home,
                parent,
                false
            )

        return Badge(itemView)
    }

    override fun onBindViewHolder(
        holder: Badge,
        position: Int
    ) {
        holder.habilidade.text = list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}