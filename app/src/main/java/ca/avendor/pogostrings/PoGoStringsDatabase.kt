package ca.avendor.pogostrings

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PoGoString::class],
    version = 1
)

abstract class PoGoStringsDatabase: RoomDatabase() {

    abstract val dao: PoGoStringsDao
}