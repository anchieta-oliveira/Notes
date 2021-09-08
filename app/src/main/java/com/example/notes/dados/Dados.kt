package com.example.notes.dados

data class Nota(
    var titulo:String,
    var conteudo:String,
    )
/*
class NovaNota(){
    var titulo:String ="OI"
    var conteudo:String="Ola pessoal"

    fun novaNota():Nota = Nota(titulo, conteudo)

}

fun nota(block: NovaNota.() -> Unit): Nota = NovaNota().apply(block).novaNota()

fun dadosNotas():MutableList<Nota> = mutableListOf(
    nota {
        titulo = "Oi"
        conteudo = "oiiiii tudo bem?"
    },
    nota {
        titulo = "Anchieta"
        conteudo = "Como voce está"
    }
)
*/