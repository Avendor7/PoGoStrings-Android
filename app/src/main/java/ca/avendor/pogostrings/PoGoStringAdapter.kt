package ca.avendor.pogostrings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.stringitem.view.*
import kotlinx.android.synthetic.main.stringitem.*

class PoGoStringAdapter(
    private val strings: MutableList<PoGoString>
) : RecyclerView.Adapter<PoGoStringAdapter.PoGoStringViewHolder>(){

    class PoGoStringViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoGoStringViewHolder {
        return PoGoStringViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.stringitem,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PoGoStringViewHolder, position: Int) {

        val curStringItem = strings[position]
        Log.d("tag", curStringItem.toString())
        /*holder.username.text = curStringItem.tvStringItem
        holder.app.text = curStringItem.app
        holder.password.text = curStringItem.password

        holder.copy.setOnClickListener() {
            val clipboardManager =
                holder.itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", "1234567890")
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(holder.itemView.context, "Text copied to clipboard", Toast.LENGTH_LONG)
                .show()
        }
*/
        holder.itemView.apply {
            tvStringItem.text =curStringItem.string

        }
    }

    override fun getItemCount(): Int {
        return strings.size
    }

    fun addPogoString(stringItem: PoGoString) {
        strings.add(stringItem)
        notifyItemInserted(strings.size - 1)

    }
}