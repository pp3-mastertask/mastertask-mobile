package com.example.mastertask.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.R
import com.example.mastertask.Models.User


class CardViewAdapter (private val list: List<User>) :
    RecyclerView.Adapter<CardViewAdapter.Card>()
{
    lateinit var context : Context

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
        val user : User = list[position]

        holder.nome.text = user.name
        holder.endereco.text = user.location
        holder.telefone.text = user.phone
        holder.estrelas.text = user.stars.toString()

        val badgeViewAdapter = BadgeViewAdapter(user.skills)
        holder.recyclerView.adapter = badgeViewAdapter
        badgeViewAdapter.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}