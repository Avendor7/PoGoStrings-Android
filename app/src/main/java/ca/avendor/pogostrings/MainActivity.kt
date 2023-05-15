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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity() {

    var pogoStringList: ArrayList<PoGoString> = ArrayList<PoGoString>()



    //private lateinit var poGoStringAdapter: PoGoStringAdapter

    //lateinit var rvStringItems: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            PoGoStringsTheme{
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PoGoStringsApp()
                }
            }

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PoGoStringsApp(){
        val newString = remember { mutableStateOf(TextFieldValue()) }


        getStringList()

        Column{
            LazyColumn(Modifier.weight(1f)) {
                items(pogoStringList.size) { item ->
                    val dismissState = rememberDismissState()
                    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                       // pogoStringList.removeAll(pogoStringList.filter { it.id == item })
                        pogoStringList.removeAt(item)
                        //save list
                        saveData()
                        getStringList()
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
                            Card (
                                Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                            )
                            {
                                Column(Modifier.padding(16.dp)){
                                    Row (
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ){
                                        Text(pogoStringList[item].id.toString() + " " + pogoStringList[item].item)
                                        Button(
                                            onClick = {},
                                            Modifier.widthIn(100.dp),
                                        ) {
                                            Text("Android")
                                        }
                                    }
                                }


                            }

                        }
                    )
                    Divider()
                }

            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,

            ){
                TextField(
                    value = newString.value,
                    onValueChange = {newString.value = it},
                    label = { Text("New String") }
                )
                Button(onClick = {
                    addToStringList(newString.value.text)
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
            PoGoStringsApp()
        }
    }

    private fun addToStringList(stringitem: String) {

        //val pogoStringList: ArrayList<PoGoString> = ArrayList<PoGoString>()
       // val pogoStringList = getStringList()
        if(pogoStringList.isEmpty()) {
            pogoStringList.add(PoGoString(0, stringitem))
        }else {
            pogoStringList.add(PoGoString(pogoStringList.last().id + 1, stringitem))
        }
        saveData()
    }
    private fun getStringList(): ArrayList<PoGoString> {

        //var pogoStringList: ArrayList<PoGoString> = ArrayList<PoGoString>()
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("strings", "[]")

        if(json == null)
            pogoStringList = ArrayList()
        else
            pogoStringList = gson.fromJson(json, object : TypeToken<ArrayList<PoGoString>>() {}.type)

    return pogoStringList
}

        //pogoStringList.add(PoGoString(stringitem))


    //save to shared preferences. Converts to Json in order to save
    private fun saveData() {

        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(pogoStringList)
        editor.putString("strings", json)
        editor.apply()

    }
//
//    private fun loadData() {
//        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
//        val gson = Gson()
//        val json = sharedPreferences.getString("task list", "[]")
//        val type = object: TypeToken<ArrayList<PoGoString>>() {
//        }.type
//
//        if(json == null)
//            pogoStringList = ArrayList()
//        else
//            pogoStringList = gson.fromJson(json, type)
//    }
}