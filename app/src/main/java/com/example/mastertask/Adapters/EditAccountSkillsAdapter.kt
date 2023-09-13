package com.example.mastertask.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Models.SkillModel
import com.example.mastertask.R

class EditAccountSkillsAdapter(private var lista_servicos: List<SkillModel>)
    : RecyclerView.Adapter<EditAccountSkillsAdapter.ServicesViewHolder>()
{
    inner class ServicesViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var serviceTextView: TextView = view.findViewById(R.id.lbSkillBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesViewHolder {
        val servicesView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.skill_badge, parent, false)

        return ServicesViewHolder(servicesView)
    }

    override fun onBindViewHolder(holder: ServicesViewHolder, position: Int) {
        val service = lista_servicos[position]
        holder.serviceTextView.text = service.nome
    }

    override fun getItemCount(): Int {
        return lista_servicos.size
    }
}