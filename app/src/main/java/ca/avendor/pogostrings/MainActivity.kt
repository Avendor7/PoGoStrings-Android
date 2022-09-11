package ca.avendor.pogostrings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    lateinit var pogoStringList: ArrayList<PoGoString>

    private lateinit var poGoStringAdapter: PoGoStringAdapter

    lateinit var rvStringItems: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //grab the item view
        rvStringItems = findViewById(R.id.rvStringItems)
        //load the data from the stored preferences
        loadData()
        //adapter things
        poGoStringAdapter = PoGoStringAdapter(pogoStringList)

        rvStringItems.adapter = poGoStringAdapter
        rvStringItems.layoutManager = LinearLayoutManager(this)

        //Add New button
        btnNewString.setOnClickListener {

            if (!etNewString.text.toString().isNullOrEmpty()){
                addItemToList(etNewString.text.toString())

                poGoStringAdapter.notifyDataSetChanged()
                saveData()
                Toast.makeText(this, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT)
                    .show()
            }else{
                Toast.makeText(this, "String empty. ", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        // slide to delete helper
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
    //save to shared preferences. Converts to Json in order to save
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
        val json = sharedPreferences.getString("task list", "[]")
        val type = object: TypeToken<ArrayList<PoGoString>>() {
        }.type

        if(json == null)
            pogoStringList = ArrayList()
        else
            pogoStringList = gson.fromJson(json, type)
    }
}