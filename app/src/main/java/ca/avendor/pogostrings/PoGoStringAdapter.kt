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

        val poGoString: PoGoString = strings[position]
        

        holder.itemView.btnCopyToClipboard.setOnClickListener(){
            Log.d("tag", poGoString.item)
            val clipboardManager =
                holder.itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", poGoString.item)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(holder.itemView.context, "Text copied to clipboard", Toast.LENGTH_LONG)
                .show()

        }
        /*holder.itemView.setOnLongClickListener() {
            Log.d("tag", poGoString.item)
            val clipboardManager =
                holder.itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", poGoString.item)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(holder.itemView.context, "Text copied to clipboard", Toast.LENGTH_LONG)
                .show()
            false
        }*/

        holder.itemView.apply {
            tvStringItem.text =poGoString.item

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