package hr.algebra.nasa.adapter

import android.content.ContentUris
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.nasa.ITEM_POS
import hr.algebra.nasa.ItemPagerActivity
import hr.algebra.nasa.NASA_PROVIDER_CONTENT_URI
import hr.algebra.nasa.R
import hr.algebra.nasa.framework.startActivity
import hr.algebra.nasa.model.Item
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class ItemAdapter(
    private val context: Context,
    private val items: MutableList<Item>
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = items[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            context.startActivity<ItemPagerActivity>(
                ITEM_POS,
                position)
        }

        holder.itemView.setOnLongClickListener {
            deleteItem(position)
            true
        }

    }

    // "content://hr.algebra.nasa.provider/items
    //"content://hr.algebra.nasa.provider/items/22

    private fun deleteItem(position: Int) {
        val item = items[position]
        context.contentResolver.delete(
            ContentUris.withAppendedId(NASA_PROVIDER_CONTENT_URI, item._id!!),
            null,
            null
        )
        File(item.picturePath).delete()
        items.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.count()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val tvItem = itemView.findViewById<TextView>(R.id.tvItem)
        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)

        fun bind(item: Item){
            tvItem.text = item.title
            Picasso.get()
                .load(File(item.picturePath))
                .error(R.drawable.nasa)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)
        }

    }
}