package ca.avendor.pogostrings

import androidx.room.PrimaryKey

data class PoGoString (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val textString: String
)