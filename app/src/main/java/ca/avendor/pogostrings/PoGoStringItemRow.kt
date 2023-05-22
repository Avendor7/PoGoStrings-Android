package ca.avendor.pogostrings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.buildAnnotatedString
import kotlinx.coroutines.launch

@Composable
fun PoGoStringItemRow(
    item: PoGoString,
    snackbarHostState: SnackbarHostState,
) {

    val clipboardManager = LocalClipboardManager.current

    // Function to copy the text to the clipboard and show a toast notification
    fun copyTextToClipboard(text: String) {
        val annotatedString = buildAnnotatedString {
            append(text)
        }
        clipboardManager.setText(annotatedString)
    }

    val scope = rememberCoroutineScope()
    Card(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
    )
    {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,

            ) {
                Text(item.pogoStringItem)
                Button(
                    onClick = {
                        copyTextToClipboard(item.pogoStringItem)
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Copied To Clipboard"
                            )
                        }
                    },
                    Modifier.widthIn(100.dp),
                ) {
                    Text("Copy")
                }
            }
        }


    }
}