package com.example.mastertask.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Data.User
import com.example.mastertask.Fragments.SelectedService
import com.example.mastertask.R

class CardViewAdapter(private val list: List<User>,
                      private val onCardClickListener: OnCardClickListener
                  ) : RecyclerView.Adapter<CardViewAdapter.Card>() {

    lateinit var context: Context

    inner class Card(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView
        val endereco: TextView
        val telefone: TextView
        val estrelas: TextView
        val recyclerView: RecyclerView

        init {
            nome = view.findViewById(R.id.nameProfissional)
            endereco = view.findViewById(R.id.location)
            telefone = view.findViewById(R.id.phone)
            estrelas = view.findViewById(R.id.stars)
            recyclerView = view.findViewById(R.id.recycler_view_services)
            itemView.setOnClickListener{
                onCardClickListener.onCardClick(list[adapterPosition])
            }
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
        val user: User = list[position]
        holder.nome.text = user.nome
        holder.endereco.text = user.endereco
        holder.telefone.text = user.contato
        if (user.numServicosFeitos != 0L)
            holder.estrelas.text = (user.somaAvaliacoes?.div(user.numServicosFeitos!!)).toString()
        else
            holder.estrelas.text = "0"
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.recyclerView.layoutManager = layoutManager
        val badgeViewAdapter = BadgeViewAdapter(user.habilidades)
        holder.recyclerView.adapter = badgeViewAdapter
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnCardClickListener {
        fun onCardClick(user: User)
    }
}
