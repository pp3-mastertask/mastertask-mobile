package com.example.mastertask.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Data.CardServiceInfo
import com.example.mastertask.R


class CardViewAdapterServices (private val list: List<CardServiceInfo>,
                               private val listener : OnItemClickListener):
    RecyclerView.Adapter<CardViewAdapterServices.Card>()
{
    lateinit var context : Context
    interface OnItemClickListener {
        fun onItemClick(item: CardServiceInfo?)
    }

    inner class Card(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView
        val endereco: TextView
        val telefone: TextView
        val estrelas: TextView

        val recyclerView : RecyclerView
        init {
            nome = view.findViewById<View>(R.id.nameProfissional) as TextView
            endereco = view.findViewById<View>(R.id.location) as TextView
            telefone = view.findViewById<View>(R.id.phone) as TextView
            estrelas = view.findViewById<View>(R.id.stars) as TextView
            recyclerView = view.findViewById<View>(R.id.recycler_view_services) as RecyclerView
        }

        fun bind(item: CardServiceInfo, listener: OnItemClickListener) {
            nome.text = item.nome
            endereco.text = item.endereco
            telefone.text = item.contato
            estrelas.text = (item.somaAvaliacoes!!.div(item.numServicosFeitos!!)).toString()

            val layoutManager: RecyclerView.LayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.layoutManager = layoutManager

            itemView.setOnClickListener { listener.onItemClick(item) }

            val badgeViewAdapter = BadgeViewAdapter(item.habilidades)
            recyclerView.adapter = badgeViewAdapter
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Card {

        context = parent.context

        val itemView: View = LayoutInflater
            .from(context)
            .inflate(
                R.layout.card_servico,
                parent,
                false
            )

        return Card(itemView)
    }

    override fun onBindViewHolder(
        holder: Card,
        position: Int
    ) {
        holder.bind(list[position], listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
