package com.example.mastertask.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Data.CardServiceInfo
import com.example.mastertask.Data.User
import com.example.mastertask.R
import com.squareup.picasso.Picasso

class CardViewAdapter(private val list: List<User>,
                      private val onCardClickListener: OnCardClickListener?
                  ) : RecyclerView.Adapter<CardViewAdapter.Card>() {

    lateinit var context: Context

    inner class Card(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView
        val endereco: TextView
        val telefone: TextView
        val estrelas: TextView
        val recyclerView: RecyclerView
        val imagem: ImageView

        init {
            nome = view.findViewById<View>(R.id.nameProfissional) as TextView
            endereco = view.findViewById<View>(R.id.location) as TextView
            telefone = view.findViewById<View>(R.id.phone) as TextView
            estrelas = view.findViewById<View>(R.id.stars) as TextView
            imagem = view.findViewById<View>(R.id.imgFotoPerfil) as ImageView
            recyclerView = view.findViewById<View>(R.id.recycler_view_services) as RecyclerView
        }

        fun bind(user: User, listener: OnCardClickListener?) {
            nome.text = user.nome
            endereco.text = user.endereco
            telefone.text = user.contato
            if (user.numServicosFeitos != 0L)
                estrelas.text = (user.somaAvaliacoes?.div(user.numServicosFeitos!!)).toString()
            else
                estrelas.text = "0"

            Picasso.get().load(user.imagem).into(imagem)

            val layoutManager: RecyclerView.LayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.layoutManager = layoutManager
            val badgeViewAdapter = BadgeViewAdapter(user.habilidades, null)
            recyclerView.adapter = badgeViewAdapter

            if (listener != null)
                itemView.setOnClickListener { listener.onCardClick(user) }
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
        holder.bind(list[position], onCardClickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnCardClickListener {
        fun onCardClick(user: User)
    }
}
