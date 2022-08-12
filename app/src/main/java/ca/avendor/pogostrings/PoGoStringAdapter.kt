package ca.avendor.pogostrings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.stringitem.view.*
import kotlinx.android.synthetic.main.stringitem.*

class PoGoStringAdapter(
    private val strings: MutableList<PoGoString>
) : RecyclerView.Adapter<PoGoStringAdapter.PoGoStringViewHolder>(){

    class PoGoStringViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoGoStringViewHolder {
        copyTextToClipboard()
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

    private fun copyTextToClipboard() {
        val itemText = tvStringItem.text
        val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", itemText)
        clipboard.setPrimaryClip(clipData)
        //Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_LONG).show()
    }

}