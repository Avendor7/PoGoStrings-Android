package ca.avendor.pogostrings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.stringitem.*
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {

    lateinit var pogoStringList: ArrayList<PoGoString>

    private lateinit var poGoStringAdapter: PoGoStringAdapter

    lateinit var rvStringItems: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //pogoStringList = ArrayList()
        //pogoStringList.add(PoGoString("Hello world"))
        rvStringItems = findViewById(R.id.rvStringItems)
        loadData()
        poGoStringAdapter = PoGoStringAdapter(pogoStringList)

        rvStringItems.adapter = poGoStringAdapter
        rvStringItems.layoutManager = LinearLayoutManager(this)

        btnNewString.setOnClickListener {

            addItemToList(etNewString.text.toString())

            /*
            // method for saving the data in array list.
            // creating a variable for storing data in
            // shared preferences.
            val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)

            // creating a variable for editor to
            // store data in shared preferences.
            val editor = sharedPreferences.edit()

            // creating a new variable for gson.
            val gson = Gson()

            // getting data from gson and storing it in a string.
            val json: String = gson.toJson(pogoStringList)

            // below line is to save data in shared
            // prefs in the form of string.
            editor.putString("poGoStrings", json)

            // below line is to apply changes
            // and save data in shared prefs.
            editor.apply()

            // after saving data we are displaying a toast message.*/

            poGoStringAdapter.notifyDataSetChanged()
            saveData()
            Toast.makeText(this, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT)
                .show()

        }
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // this method is called
                // when the item is moved.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                val deletedItem: PoGoString =
                    pogoStringList.get(viewHolder.adapterPosition)

                // below line is to get the position
                // of the item at that position.
                val position = viewHolder.adapterPosition

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                pogoStringList.removeAt(viewHolder.adapterPosition)

                // below line is to notify our item is removed from adapter.
                poGoStringAdapter.notifyItemRemoved(viewHolder.adapterPosition)

                // below line is to display our snackbar with action.
                // below line is to display our snackbar with action.
                // below line is to display our snackbar with action.
                Snackbar.make(rvStringItems, "Deleted " + deletedItem.item, Snackbar.LENGTH_LONG)
                    .setAction(
                        "Undo",
                        View.OnClickListener {
                            // adding on click listener to our action of snack bar.
                            // below line is to add our item to array list with a position.
                            pogoStringList.add(position, deletedItem)

                            // below line is to notify item is
                            // added to our adapter class.
                            poGoStringAdapter.notifyItemInserted(position)
                        }).show()
            }
            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(rvStringItems)
    }
    private fun addItemToList(stringitem: String) {
        // in this method we are adding item to list and
        // notifying adapter that data has changed
        etNewString.text.clear()
        pogoStringList.add(PoGoString(stringitem))

    }
    private fun saveData() {
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(pogoStringList)
        editor.putString("task list", json)
        editor.apply()

    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("task list", "")
        val type = object: TypeToken<ArrayList<PoGoString>>() {
        }.type

        if(json == null)
            pogoStringList = ArrayList()
        else
            pogoStringList = gson.fromJson(json, type)
    }
}