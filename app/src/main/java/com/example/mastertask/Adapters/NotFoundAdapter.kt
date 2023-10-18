package com.example.mastertask.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.R
class NotFoundAdapter(private val text: String) : RecyclerView.Adapter<NotFoundAdapter.Message>() {
    inner class Message(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mensagem : TextView
        init {
            mensagem = itemView.findViewById(R.id.lb_message_not_found) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Message {
        val itemView : View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.card_not_found, parent, false)

        return Message(itemView)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: Message, position: Int) {
        holder.mensagem.text = text
    }
}