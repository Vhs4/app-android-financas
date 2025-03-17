package com.example.fin_edu_app.persistence.repository

import android.content.Context
import com.example.fin_edu_app.persistence.database.FinEduDatabase
import com.example.fin_edu_app.persistence.model.Usuario

class UsuarioRepository(context: Context) {

    private val db = FinEduDatabase.getDatabase(context).finEduDao()

    fun buscarTodosUsuarios(): List<Usuario> {
        return db.buscarTodosUsuarios()
    }

    fun buscarPontuacao(id: Long): Usuario {
        return db.buscarPontuacao(id)
    }

    fun inserirPontuacao(usuario: Usuario) {
        db.inserirPontuacao(usuario)
    }
}