package ca.avendor.pogostrings

sealed interface PoGoStringsEvent {
    object SavePoGoString: PoGoStringsEvent
    data class SetPoGoStringItem(val pogoStringItem: String): PoGoStringsEvent

    data class DeletePoGoString(val pogoString: PoGoString): PoGoStringsEvent
}