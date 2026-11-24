package com.example.dictionary

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Kata::class], version = 1, exportSchema = false)
abstract class KamusDatabase : RoomDatabase() {
    abstract fun kataDao(): KataDao

    companion object {
        @Volatile
        private var INSTANCE: KamusDatabase? = null

        fun getInstance(context: Context): KamusDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KamusDatabase::class.java,
                    "kamus.db"
                )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                database.kataDao().tambah(Kata(indonesia = "buku", inggris = "book"))
                                database.kataDao().tambah(Kata(indonesia = "kucing", inggris = "cat"))
                                database.kataDao().tambah(Kata(indonesia = "rumah", inggris = "house"))
                            }
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}