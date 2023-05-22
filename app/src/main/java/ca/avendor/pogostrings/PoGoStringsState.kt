package ca.avendor.pogostrings

data class PoGoStringsState(
    val pogoStrings: List<PoGoString> = emptyList(),
    val pogoStringItem: String = ""
)
