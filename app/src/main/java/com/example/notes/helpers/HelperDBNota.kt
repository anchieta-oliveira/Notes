package com.example.notes.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.notes.dados.Nota
import java.lang.Exception

class HelperDBNota(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "NotaDB.db"
    }
    private val CREATE_TABLE =
        "CREATE TABLE ${NotaDBContract.NotaEntry.TABLE_NAME} ("+
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${NotaDBContract.NotaEntry.COLUMN_TITULO_NOTA} TEXT," +
                "${NotaDBContract.NotaEntry.COLUMN_CONTEUDO_NOTA} TEXT)"

    private val DROP_TABLE = "DROP TABLE IF EXISTS ${NotaDBContract.NotaEntry.TABLE_NAME}"


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(oldVersion != newVersion){
            db?.execSQL(DROP_TABLE)
        }
    }
    fun addNota(nota: Nota){
        val db = writableDatabase?:return
        var valores = ContentValues().apply {
            put(NotaDBContract.NotaEntry.COLUMN_TITULO_NOTA, nota.titulo)
            put(NotaDBContract.NotaEntry.COLUMN_CONTEUDO_NOTA, nota.conteudo)
        }

        db.insert(NotaDBContract.NotaEntry.TABLE_NAME, null, valores)
    }

    fun getNota(filtro:String? = null): MutableList<Nota> {
        val db = readableDatabase ?: return mutableListOf<Nota>()
        var where: String? = null
        var args: Array<String> = arrayOf()
        var sortOrder = "${BaseColumns._ID} DESC"
        var lista = mutableListOf<Nota>()

        if (filtro != null){
            where = "${NotaDBContract.NotaEntry.COLUMN_TITULO_NOTA} LIKE ?"
            args = arrayOf("%$filtro%")
        }

        var cursor =
            db.query(NotaDBContract.NotaEntry.TABLE_NAME, null, where, args, null, null, sortOrder)
        while (cursor.moveToNext()) {
            var nota = Nota(
                cursor.getString(cursor.getColumnIndexOrThrow(NotaDBContract.NotaEntry.COLUMN_TITULO_NOTA)),
                cursor.getString(cursor.getColumnIndexOrThrow(NotaDBContract.NotaEntry.COLUMN_CONTEUDO_NOTA)),
            )
            lista.add(nota)
        }
        db.close()
        return lista
    }

    fun editNota(titulo:String, nota: Nota){
        val db = writableDatabase?:return
        val sql = "UPDATE ${NotaDBContract.NotaEntry.TABLE_NAME} SET ${NotaDBContract.NotaEntry.COLUMN_TITULO_NOTA} = ?, ${NotaDBContract.NotaEntry.COLUMN_CONTEUDO_NOTA} = ? WHERE ${NotaDBContract.NotaEntry.COLUMN_TITULO_NOTA} = ? "
        val arg = arrayOf(nota.titulo, nota.conteudo,titulo)
        try {
            db.execSQL(sql, arg)
            db.close()
        } catch (e: Exception){e.printStackTrace()}
    }

    fun excluirNota(nota: Nota){
        val db = writableDatabase?:return
        val sql = "DELETE FROM ${NotaDBContract.NotaEntry.TABLE_NAME} WHERE ${NotaDBContract.NotaEntry.COLUMN_TITULO_NOTA} = ?"
        val arg = arrayOf(nota.titulo)
        try {
            db.execSQL(sql, arg)
            db.close()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}


