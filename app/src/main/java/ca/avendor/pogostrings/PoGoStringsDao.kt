package ca.avendor.pogostrings

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PoGoStringsDao {
    @Upsert
    suspend fun upsertPoGoString(pogoString: PoGoString)

    @Delete
    suspend fun deletePoGoString(pogoString: PoGoString)

    @Query("SELECT * FROM pogostrings")
    fun getPogoStrings(): Flow<List<PoGoString>>
}