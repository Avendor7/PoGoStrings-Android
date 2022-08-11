package ca.avendor.pogostrings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var poGoStringAdapter: PoGoStringAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        poGoStringAdapter = PoGoStringAdapter(mutableListOf())


        rvStringItems.adapter = poGoStringAdapter
        rvStringItems.layoutManager = LinearLayoutManager(this)

        btnNewString.setOnClickListener {
            val stringText = etNewString.text.toString()
            if(stringText.isNotEmpty()){
                val stringItem = PoGoString(stringText)
                poGoStringAdapter.addPogoString(stringItem)
                etNewString.text.clear()
            }
        }
    }
}