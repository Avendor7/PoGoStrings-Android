package ca.avendor.pogostrings

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import ca.avendor.pogostrings.ui.theme.PoGoStringsTheme

class MainActivity : AppCompatActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            PoGoStringsDatabase::class.java,
            "pogostrings.db"
        ).build()
    }
    private val viewModel by viewModels<pogoStringViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return pogoStringViewModel(db.dao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            PoGoStringsTheme {
                // A surface container using the 'background' color from the theme

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val state by viewModel.state.collectAsState()
                    PoGoStringsApp(state = state, onEvent = viewModel::onEvent)
                }
            }

        }
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
        ExperimentalComposeUiApi::class
    )
    @Composable
    fun PoGoStringsApp(
        state: PoGoStringsState,
        onEvent: (PoGoStringsEvent) -> Unit
    ) {
        val snackbarHostState = remember { SnackbarHostState() }
        val keyboardController = LocalSoftwareKeyboardController.current
        val openDialog = remember { mutableStateOf(false) }

        val lazyListState = rememberLazyListState()

        Scaffold(
            topBar = {},
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { openDialog.value = true },
                    ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add PoGo String",
                        tint = Color.White,
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    snackbar = { data ->
                        Snackbar(
                            snackbarData = data,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                )
            },
            content = { innerPadding ->

                Column(
                    Modifier
                        .padding(top = 4.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    LazyColumn(
                        contentPadding = innerPadding,
                        state = lazyListState
                    ) {
                        items(
                            items = state.pogoStrings,
                            key = { item -> item.id },
                            itemContent = { item ->
                                val currentItem by rememberUpdatedState(item)
                                val dismissState = rememberDismissState(
                                    confirmValueChange = {
                                        if (it == DismissValue.DismissedToStart) {
                                            onEvent(PoGoStringsEvent.DeletePoGoString(currentItem))
                                            true
                                        } else false
                                    }
                                )

                                if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                                    onEvent(PoGoStringsEvent.DeletePoGoString(currentItem))
                                }

                                SwipeToDismiss(
                                    state = dismissState,
                                    directions = setOf(
                                        DismissDirection.EndToStart
                                    ),
                                    modifier = Modifier.padding(vertical = 1.dp),
                                    background = {
                                        val color by animateColorAsState(
                                            when (dismissState.targetValue) {
                                                DismissValue.Default -> MaterialTheme.colorScheme.background
                                                else -> Color.Red
                                            }, label = "blah"
                                        )
                                        val scale by animateFloatAsState(
                                            if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f,
                                            label = ""
                                        )
                                        val alignment = Alignment.CenterEnd
                                        val icon = Icons.Default.Delete
                                        Box(
                                            Modifier
                                                .fillMaxSize()
                                                .background(color)
                                                .padding(horizontal = Dp(20f)),
                                            contentAlignment = alignment
                                        ) {
                                            Icon(
                                                icon,
                                                contentDescription = "Delete Icon",
                                                modifier = Modifier.scale(scale)
                                            )
                                        }
                                    },
                                    dismissContent = {
                                        PoGoStringItemRow(
                                            currentItem,
                                            snackbarHostState
                                        )
                                    }
                                )
                            }

                        )
                    }
//                    Row(
//                        Modifier
//                            .fillMaxWidth()
//                            .padding(8.dp),
//                        verticalAlignment = Alignment.CenterVertically
//
//                    ) {
//
//                        TextField(
//                            modifier = Modifier.weight(1f), // Set weight to 1
//                            value = state.pogoStringItem,
//                            onValueChange = {
//                                onEvent(PoGoStringsEvent.SetPoGoStringItem(it))
//                            },
//                            label = { Text("New String") }
//                        )
//                        Spacer(modifier = Modifier.widthIn(8.dp))
//                        Button(
//
//                            onClick = {
//                                //viewModel.addString(newString.value.text)
//                                onEvent(PoGoStringsEvent.SavePoGoString)
//                                keyboardController?.hide()
//                                //save the list
//                                println("Added new string")
//
//                            }) {
//
//                            Text("Add New")
//                        }
//                    }
                }
            }
        )

        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onDismissRequest.
                    openDialog.value = false
                }
            ) {
                Surface(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    shape = MaterialTheme.shapes.extraLarge,
                    tonalElevation = AlertDialogDefaults.TonalElevation
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        TextField(
                            value = state.pogoStringItem,
                            onValueChange = {
                                onEvent(PoGoStringsEvent.SetPoGoStringItem(it))
                            },
                            label = { Text("New String") }
                        )
                        //Spacer(modifier = Modifier.height(24.dp))
                        TextButton(
                            onClick = {
                                onEvent(PoGoStringsEvent.SavePoGoString)
                                keyboardController?.hide()
                                //save the list
                                println("Added new string")
                                openDialog.value = false
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }

        }
    }
    @Preview(showBackground = true)
    @Composable
    fun PoGoStringsAppPreview() {
        PoGoStringsTheme {
            PoGoStringsApp(
                state = PoGoStringsState(
                    pogoStrings = listOf(
                        PoGoString(1, "Test1"),
                        PoGoString(2, "Test2"),
                        PoGoString(3, "Test3")
                    )
                ),
                onEvent = {}
            )
        }
    }

}