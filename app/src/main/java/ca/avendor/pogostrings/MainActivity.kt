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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
// Removed: import androidx.compose.material3.DismissDirection
// Removed: import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
// Removed: import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.SwipeToDismissBox // Added
import androidx.compose.material3.SwipeToDismissBoxValue // Added
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
// Removed: import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.rememberSwipeToDismissBoxState // Added
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp // Changed Dp(20f) to 20.dp
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
                    @Suppress("UNCHECKED_CAST")
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

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
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
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "PoGo Strings",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {},
                    actions = {},
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { openDialog.value = true },
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add PoGo String",
                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
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
            bottomBar = {},
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
                                // Use rememberSwipeToDismissBoxState
                                val swipeToDismissBoxState =
                                    rememberSwipeToDismissBoxState(
                                        confirmValueChange = { dismissValue ->
                                            // Check if the swipe is towards the start (left swipe)
                                            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                                onEvent(
                                                    PoGoStringsEvent.DeletePoGoString(
                                                        currentItem
                                                    )
                                                )
                                                true // Confirm the state change (item will be dismissed)
                                            } else {
                                                false // Don't confirm other state changes
                                            }
                                        }
                                        // Optional: positionalThreshold can be added here if needed
                                    )

                                // Use SwipeToDismissBox
                                SwipeToDismissBox(
                                    state = swipeToDismissBoxState,
                                    modifier = Modifier.padding(vertical = 1.dp),
                                    // Set swipe directions using boolean flags
                                    enableDismissFromStartToEnd = false, // Disable right swipe
                                    enableDismissFromEndToStart = true, // Enable left swipe
                                    // Use backgroundContent lambda
                                    backgroundContent = {
                                        val color by animateColorAsState(
                                            // Check targetValue from swipeToDismissBoxState
                                            when (swipeToDismissBoxState.targetValue) {
                                                SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.background
                                                SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.background // Should not be reachable
                                                SwipeToDismissBoxValue.EndToStart -> Color.Red
                                            },
                                            label = "DismissBackgroundColor" // Added label
                                        )
                                        val scale by animateFloatAsState(
                                            // Check targetValue from swipeToDismissBoxState
                                            if (swipeToDismissBoxState.targetValue == SwipeToDismissBoxValue.Settled) 0.75f else 1f,
                                            label = "DismissIconScale" // Added label
                                        )
                                        val alignment = Alignment.CenterEnd
                                        val icon = Icons.Default.Delete
                                        Box(
                                            Modifier
                                                .fillMaxSize()
                                                .background(color)
                                                .padding(horizontal = 20.dp), // Use dp directly
                                            contentAlignment = alignment
                                        ) {
                                            Icon(
                                                icon,
                                                contentDescription = "Delete Icon",
                                                modifier = Modifier.scale(scale)
                                            )
                                        }
                                    }
                                    // Main content lambda for the item itself
                                ) {
                                    PoGoStringItemRow(
                                        currentItem,
                                        snackbarHostState
                                    )
                                }
                            }
                        )
                    }
                }
            }
        )

        if (openDialog.value) {
            AlertDialog(
                modifier = Modifier.wrapContentHeight(unbounded = true),
                onDismissRequest = {
                    openDialog.value = false
                }
            ) {
                Surface(
                    modifier = Modifier.wrapContentWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    tonalElevation = AlertDialogDefaults.TonalElevation
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val focusRequester = remember { FocusRequester() }
                        TextField(
                            value = state.pogoStringItem,
                            maxLines = 3,
                            onValueChange = {
                                onEvent(PoGoStringsEvent.SetPoGoStringItem(it))
                            },
                            label = { Text("New String") },
                            modifier = Modifier.focusRequester(focusRequester)
                        )
                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                        }
                        TextButton(
                            onClick = {
                                onEvent(PoGoStringsEvent.SavePoGoString)
                                keyboardController?.hide()
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
}