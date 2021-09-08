package com.example.notes

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.application.NotasApplication
import com.example.notes.dados.Nota
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recycleViewNotas = findViewById<RecyclerView>(R.id.recycleNote)
        val buttonAdd = findViewById<FloatingActionButton>(R.id.addNote)
        val verNotaPage = findViewById<LinearLayout>(R.id.ver_nota)
        val contPesq = findViewById<LinearLayout>(R.id.conteinerBuscar)
        val editTitulo = findViewById<EditText>(R.id.edit_titulo)
        val editConteudo = findViewById<EditText>(R.id.edit_ConteudoNota)
        val fecharButton = findViewById<ImageButton>(R.id.button_fechar)
        val excluirButton = findViewById<ImageButton>(R.id.button_excluir)
        val buttonPesq = findViewById<ImageButton>(R.id.button_Pesquisa)
        val barPesq = findViewById<EditText>(R.id.busca)


        atualizarRecycle()

        buttonPesq.setOnClickListener{
            if (contPesq.visibility == View.VISIBLE){
                fade(contPesq)
            }else{
                aparecer(contPesq)
                barPesq.setOnEditorActionListener(){ _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        atualizarRecycle(barPesq.text.toString())
                        fecharTeclado()
                        fade(contPesq)
                    }
                    true
                }
            }
        }

        buttonAdd.setOnClickListener {
            editTitulo.setText("")
            editConteudo.setText("")
            crossFade(verNotaPage, recycleViewNotas)
            intent.putExtra("Ação", "Add")

        }

        fecharButton.setOnClickListener {
            val notaVazia = Nota("", "")
            val notaClick = Nota(intent.getStringExtra("tituloAtual").toString(),
                intent.getStringExtra("conteudoAtual").toString())
            val notaEdit = Nota(editTitulo.text.toString(), editConteudo.text.toString())
            if (intent.getStringExtra("Ação") == "Edit") {
                if (notaClick != notaEdit){
                    NotasApplication.instance.helperDBNota?.editNota(notaClick.titulo, notaEdit)

                }
            }else {
                if (notaEdit != notaVazia ){
                    NotasApplication.instance.helperDBNota?.addNota(notaEdit)
                }
            }
            atualizarRecycle()
            crossFade(recycleViewNotas, verNotaPage)
        }

        excluirButton.setOnClickListener{
            val notaClick = Nota(intent.getStringExtra("tituloAtual").toString(),
                intent.getStringExtra("conteudoAtual").toString())
            NotasApplication.instance.helperDBNota?.excluirNota(notaClick)
            atualizarRecycle()
            crossFade(recycleViewNotas, verNotaPage)
        }
    }

    private fun atualizarRecycle(busca:String? = null) {
        val recycleViewNotas = findViewById<RecyclerView>(R.id.recycleNote)
        val verNotaPage = findViewById<LinearLayout>(R.id.ver_nota)
        val editTitulo = findViewById<EditText>(R.id.edit_titulo)
        val editConteudo = findViewById<EditText>(R.id.edit_ConteudoNota)
        val listaSQL = NotasApplication.instance.helperDBNota?.getNota()?: mutableListOf()

        Thread(Runnable {

            listaSQL.clear()
            listaSQL.addAll(NotasApplication.instance.helperDBNota?.getNota(busca)?: mutableListOf())
        }).start()

        var notaAdapter = NotaAdapter(listaSQL, this) { it ->
            crossFade(verNotaPage, recycleViewNotas)
            editTitulo.setText(it.titulo)
            editConteudo.setText(it.conteudo)
            intent.putExtra("tituloAtual", it.titulo)
            intent.putExtra("conteudoAtual", it.conteudo)
            intent.putExtra("Ação", "Edit")
        }

        runOnUiThread {
            recycleViewNotas.adapter = notaAdapter
        }
    }

    fun fecharTeclado(){
        val view: View? = currentFocus
        view?.let {
            val imm:InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    fun fade(view: View){
        var shortAnimationDuration: Int = 0
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        view.animate().alpha(0f)
            .setDuration(shortAnimationDuration.toLong())
            .setListener(object: AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.GONE}})
    }

    fun aparecer(view: View){
        var shortAnimationDuration: Int = 0
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        view.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate().alpha(1f)
                .setDuration(shortAnimationDuration.toLong())
                .setListener(null)
        }
    }

    fun crossFade(viewOn: View, viewOf: View){
        fade(viewOf)
        aparecer(viewOn)
    }
}