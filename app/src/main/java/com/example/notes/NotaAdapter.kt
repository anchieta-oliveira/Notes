package com.example.notes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.dados.Nota


class NotaAdapter(private val dataNota: MutableList<Nota>, private val context: Context, val onClick: (Nota) -> Unit) : RecyclerView.Adapter<NotaAdapter.NotaViewHolder>() {

    inner class NotaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tituloNota = itemView.findViewById<TextView>(R.id.titulo_Nota)
        val conteudoNota = itemView.findViewById<TextView>(R.id.conteudoNota)


        fun binid(nota: Nota){
            tituloNota.text = nota.titulo
            conteudoNota.text = nota.conteudo
            with(itemView){
                setOnClickListener{
                    onClick.invoke(nota)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.nota_item, parent, false)
        return NotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        holder.binid(dataNota[position])
        //holder.tituloNota.text = dataNota[position].titulo
        //holder.conteudoNota.text = dataNota[position].conteudo
    }

    override fun getItemCount() = dataNota.size
}
