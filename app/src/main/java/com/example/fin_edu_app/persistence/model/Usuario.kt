package com.example.fin_edu_app.persistence.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import net.datafaker.Faker

@Entity(tableName = "tb_usuario")
data class Usuario(
    @PrimaryKey (autoGenerate = true) var id: Long,
    @ColumnInfo(name = "nome") var nome: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "pontuacao") var pontuacao: Int) {

    companion object {
        val listaDeUsuarios: List<Usuario> = List(100) {
            val faker = Faker()
            Usuario(
                id = it.toLong(),
                nome = faker.name().fullName(),
                email = faker.internet().emailAddress(),
                pontuacao = (10..1000).random()
            )
        }
    }
}
