package ca.avendor.pogostrings

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.avendor.pogostrings.ui.theme.PoGoStringsTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity() {

    //lateinit var pogoStringList: ArrayList<PoGoString>

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

//
//        setContentView(R.layout.activity_main)
//
//
//        //grab the item view
//        rvStringItems = findViewById(R.id.rvStringItems)
//        //load the data from the stored preferences
//        loadData()
//        //adapter things
//        poGoStringAdapter = PoGoStringAdapter(pogoStringList)
//
//        rvStringItems.adapter = poGoStringAdapter
//        rvStringItems.layoutManager = LinearLayoutManager(this)
//
//        //Add New button
//        btnNewString.setOnClickListener {
//
//            if (!etNewString.text.toString().isNullOrEmpty()){
//                addItemToList(etNewString.text.toString())
//
//                poGoStringAdapter.notifyDataSetChanged()
//                saveData()
//                Toast.makeText(this, "Saved new string. ", Toast.LENGTH_SHORT)
//                    .show()
//            }else{
//                Toast.makeText(this, "String empty. ", Toast.LENGTH_SHORT)
//                    .show()
//            }
//
//        }
//        // slide to delete helper
//        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                // this method is called
//                // when the item is moved.
//                return false
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                // this method is called when we swipe our item to right direction.
//                // on below line we are getting the item at a particular position.
//                val deletedItem: PoGoString =
//                    pogoStringList.get(viewHolder.adapterPosition)
//
//                // below line is to get the position
//                // of the item at that position.
//                val position = viewHolder.adapterPosition
//
//                // this method is called when item is swiped.
//                // below line is to remove item from our array list.
//                pogoStringList.removeAt(viewHolder.adapterPosition)
//
//                // below line is to notify our item is removed from adapter.
//                poGoStringAdapter.notifyItemRemoved(viewHolder.adapterPosition)
//                saveData()
//                // below line is to display our snackbar with action.
//                Snackbar.make(rvStringItems, "Deleted " + deletedItem.item, Snackbar.LENGTH_LONG)
//                    .setAction(
//                        "Undo",
//                        View.OnClickListener {
//                            // adding on click listener to our action of snack bar.
//                            // below line is to add our item to array list with a position.
//                            pogoStringList.add(position, deletedItem)
//
//                            // below line is to notify item is
//                            // added to our adapter class.
//                            poGoStringAdapter.notifyItemInserted(position)
//                            saveData()
//                        }).show()
//            }
//            // at last we are adding this
//            // to our recycler view.
//        }).attachToRecyclerView(rvStringItems)
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PoGoStringsApp(){
        val newString = remember { mutableStateOf(TextFieldValue()) }
        val stringListState = getStringList()


        Column{
            LazyColumn(Modifier.weight(1f)) {
                items(stringListState.size) { i ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text("$i")
                        Button(onClick = {
                            //Copy item to clipboard

                        }) {
                            Text("Android")
                        }
                    }
                    Divider()
                }

            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,

            ){
                TextField(
                    value = newString.value,
                    onValueChange = {newString.value = it},
                    label = { Text("New String") }
                )
                Button(onClick = {
                    //Save the newString.Value to the list
                    addToStringList(newString.value.text)
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

    private fun addToStringList(): String {
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("task list", "[]")
        val prefs = getSharedPreferences(PreferenceManager.PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(PreferenceManager.POGO_STRING_LIST, json)
        editor.apply()
    }
    private fun getStringList(): List<String> {
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("task list", "[]")
        val type = object: TypeToken<ArrayList<PoGoString>>() {
        }.type

        if(json == null)
            return ArrayList()
        else
            return gson.fromJson(json, type)
    }
//    private fun addItemToList(stringitem: String) {
//        // in this method we are adding item to list and
//        // notifying adapter that data has changed
//        etNewString.text.clear()
//        pogoStringList.add(PoGoString(stringitem))
//
//    }
//    //save to shared preferences. Converts to Json in order to save
//    private fun saveData() {
//        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        val gson = Gson()
//        val json = gson.toJson(pogoStringList)
//        editor.putString("task list", json)
//        editor.apply()
//
//    }
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