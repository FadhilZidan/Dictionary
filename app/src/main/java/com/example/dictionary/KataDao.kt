package com.example.dictionary

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface KataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun tambah(kata: Kata)

    @Delete
    suspend fun hapus(kata: Kata)

    @Query("SELECT * FROM kata ORDER BY id ASC")
    fun ambilSemua(): Flow<List<Kata>>

    @Query("SELECT * FROM kata WHERE indonesia LIKE '%' || :kunci || '%' OR inggris LIKE '%' || :kunci || '%' ORDER BY id ASC")
    suspend fun cari(kunci: String): List<Kata>
}