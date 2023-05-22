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
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun PoGoStringItemRow(
    item: PoGoString,
    snackbarHostState: SnackbarHostState,
) {
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
                        //TODO redo the copy to clipboard stuff, see chatgpt history
                        // show snackbar as a suspend function
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Snackbar"
                            )
                        }
                    },
                    Modifier.widthIn(100.dp),
                ) {
                    Text("Android")
                }
            }
        }


    }
}