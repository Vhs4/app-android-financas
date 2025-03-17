package com.example.fin_edu_app.persistence.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fin_edu_app.persistence.dao.UsuarioDao
import com.example.fin_edu_app.persistence.model.Usuario

@Database(entities = [Usuario::class], version = 1)
abstract class FinEduDatabase : RoomDatabase() {

    abstract fun finEduDao() : UsuarioDao

    companion object {
        private lateinit var instanciaDb: FinEduDatabase

        fun getDatabase(context: Context): FinEduDatabase {
            val isNotInitialized = !::instanciaDb.isInitialized

            if(isNotInitialized) {
                instanciaDb = Room.databaseBuilder(
                    context,
                    FinEduDatabase::class.java,
                    "db_fin_edu"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instanciaDb
        }
    }
}