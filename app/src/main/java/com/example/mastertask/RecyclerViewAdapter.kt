package com.example.mastertask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter (private val list: List<User>) :
    RecyclerView.Adapter<RecyclerViewAdapter.Card>()
{
    inner class Card(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView
        val endereco: TextView
        val telefone: TextView
        val estrelas: TextView
        init {
            nome = view.findViewById<View>(R.id.nameProfissional) as TextView
            endereco = view.findViewById<View>(R.id.location) as TextView
            telefone = view.findViewById<View>(R.id.phone) as TextView
            estrelas = view.findViewById<View>(R.id.stars) as TextView
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Card {

        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.fragment_home,
                parent,
                false
            )

        return Card(itemView)
    }

    override fun onBindViewHolder(
        holder: Card,
        position: Int
    ) {
        holder.nome.text = list[position].name
        holder.endereco.text = list[position].location
        holder.telefone.text = list[position].phone
        holder.estrelas.text = list[position].stars.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}