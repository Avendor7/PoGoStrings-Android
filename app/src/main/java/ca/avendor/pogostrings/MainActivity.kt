package ca.avendor.pogostrings

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ca.avendor.pogostrings.ui.theme.PoGoStringsTheme
import androidx.compose.foundation.lazy.items

class MainActivity : AppCompatActivity() {
    private val viewModel = pogoStringViewModel()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent{
            PoGoStringsTheme{
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PoGoStringsApp(viewModel = viewModel)
                }
            }

        }
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    fun PoGoStringsApp(viewModel: pogoStringViewModel){
        val newString = remember { mutableStateOf(TextFieldValue()) }



        val pogoStringState = viewModel.pogoStringFlow.collectAsState();
        val lazyListState = rememberLazyListState()



        Column(
            Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ){
            LazyColumn(
                state = lazyListState
            ){
                items(
                    items = pogoStringState.value,
                    key = { item -> item.id },
                    itemContent = { item ->
                        val currentItem by rememberUpdatedState(item)
                        val dismissState = rememberDismissState(
                            confirmValueChange = {
                                if (it == DismissValue.DismissedToStart) {
                                    viewModel.removeString(currentItem)
                                    true
                                } else false
                            }
                        )

                        if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                            viewModel.removeString(item)
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
                                        DismissValue.Default -> Color.White
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
                                PoGoStringItemRow(item, pogoStringState, viewModel)
                            }
                        )
                    }

                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically

            ){
                TextField(

                    value = newString.value,
                    onValueChange = {newString.value = it},
                    label = { Text("New String") }
                )
                Spacer(modifier = Modifier.widthIn(8.dp))
                Button(

                    onClick = {
                    viewModel.addString(newString.value.text)
                    //save the list

                    println("Added new string")

                }) {

                    Text("Add New")
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PoGoStringsAppPreview() {
        PoGoStringsTheme {
            PoGoStringsApp(viewModel)
        }
    }

}