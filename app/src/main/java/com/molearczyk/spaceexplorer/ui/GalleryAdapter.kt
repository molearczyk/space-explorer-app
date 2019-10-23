package com.molearczyk.spaceexplorer.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.molearczyk.spaceexplorer.R
import com.molearczyk.spaceexplorer.inflate
import com.molearczyk.spaceexplorer.network.models.GalleryEntry

typealias GalleryItemClickListener = (GalleryEntry) -> Unit
typealias GalleryNextPageListener = () -> Unit

class GalleryAdapter(private val clickListener: GalleryItemClickListener, private val imageLoader: ImageLoader, private val nextPageListener: GalleryNextPageListener, gridSpan: Int, context: Context) : RecyclerView.Adapter<GalleryAdapter.GalleryItemViewHolder>() {

    private val itemSize = context.resources.displayMetrics.widthPixels / gridSpan

    private val contentTracker: ListContentTracker = ListContentTracker(nextPageListener, gridSpan)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryItemViewHolder = GalleryItemViewHolder(parent.inflate(R.layout.item_gallery_view_holder).apply {
        this.updateLayoutParams {
            width = itemSize
            height = itemSize
        }
    })

    override fun getItemCount(): Int = contentTracker.size

    override fun onBindViewHolder(holder: GalleryItemViewHolder, position: Int) {
        holder.bind(contentTracker.getItem(position), clickListener, imageLoader)
    }

    fun setNewEntries(newEntries: List<GalleryEntry>) {
        contentTracker.setEntries(newEntries)
        notifyDataSetChanged()
    }

    fun appendEntries(newEntries: List<GalleryEntry>) {
        val startingIndex = itemCount
        contentTracker.appendEntries(newEntries)
        notifyItemRangeInserted(startingIndex, itemCount)
    }

    class GalleryItemViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {

        fun bind(record: GalleryEntry, clickListener: GalleryItemClickListener, imageLoader: ImageLoader) {
            itemView.setOnClickListener {
                clickListener(record)
            }
            imageLoader.loadCroppedImageInto(record.previewImage, itemView as ImageView)
        }

    }

}
