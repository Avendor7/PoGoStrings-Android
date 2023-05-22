package ca.avendor.pogostrings

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity()
data class PoGoString (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pogoStringItem: String
)