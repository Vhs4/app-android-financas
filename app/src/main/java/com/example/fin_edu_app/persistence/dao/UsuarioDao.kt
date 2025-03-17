package com.example.fin_edu_app.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fin_edu_app.persistence.model.Usuario

@Dao
interface UsuarioDao {

    @Insert
    public fun inserirPontuacao(usuario: Usuario)

    @Update
    public fun atualizarPontuacao(usuario: Usuario)

    @Query("SELECT * FROM tb_usuario WHERE id = :id")
    public fun buscarPontuacao(id: Long): Usuario

    @Query("SELECT * FROM tb_usuario")
    public fun buscarTodosUsuarios(): List<Usuario>


}