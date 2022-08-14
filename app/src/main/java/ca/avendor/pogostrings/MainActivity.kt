package ca.avendor.pogostrings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.stringitem.*
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {

    lateinit var pogoStringList: ArrayList<PoGoString>

    private lateinit var poGoStringAdapter: PoGoStringAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pogoStringList = ArrayList()
        pogoStringList.add(PoGoString("Hello world"))
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)

        // creating a variable for gson.
        val gson = Gson()

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        val json = sharedPreferences.getString("item", null)

        // below line is to get the type of our array list.
        val type: Type = object : TypeToken<ArrayList<PoGoString>>() {}.type

        // in below line we are getting data from gson
        // and saving it to our array list

        //NOTE code stops here. May need another library or approach for getting the arraylist in and out of the storage

        Log.d("arraylist", pogoStringList.toString())
        //pogoStringList = gson.fromJson<Any>(json, type) as ArrayList<PoGoString>

        // checking below if the array list is empty or not
        if (pogoStringList == null) {
            // if the array list is empty
            // creating a new array list.


        }
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

            // after saving data we are displaying a toast message.
            Toast.makeText(this, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT)
                .show()*/
            poGoStringAdapter.notifyDataSetChanged()
        }
    }
    private fun addItemToList(stringitem: String) {
        // in this method we are adding item to list and
        // notifying adapter that data has changed

        pogoStringList.add(PoGoString(stringitem))

    }
}