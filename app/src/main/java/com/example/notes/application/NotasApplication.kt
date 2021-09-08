package com.example.notes.application

import android.app.Application
import com.example.notes.helpers.HelperDBNota

class NotasApplication: Application() {
    var helperDBNota: HelperDBNota? = null
        private  set

    companion object{
        lateinit var instance: NotasApplication
    }

    override fun onCreate(){
        super.onCreate()
        instance = this
        helperDBNota = HelperDBNota(this)
    }

}