package com.example.notes.helpers

import android.provider.BaseColumns

object NotaDBContract {
    object NotaEntry:BaseColumns{
        const val TABLE_NAME = "Notas"
        const val COLUMN_TITULO_NOTA = "TituloNota"
        const val COLUMN_CONTEUDO_NOTA = "ConteudoNota"

    }
}


