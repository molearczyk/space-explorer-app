package com.molearczyk.spaceexplorer.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.molearczyk.spaceexplorer.R
import com.molearczyk.spaceexplorer.inflate
import com.molearczyk.spaceexplorer.network.models.GalleryRecord

typealias GalleryItemClickListener = (GalleryRecord) -> Unit

class GalleryAdapter(private val pictures: List<GalleryRecord>, private val clickListener: GalleryItemClickListener, private val imageLoader: ImageLoader, gridSpan: Int, context: Context) : RecyclerView.Adapter<GalleryAdapter.GalleryItemViewHolder>() {

    private val itemSize = context.resources.displayMetrics.widthPixels / gridSpan

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryItemViewHolder = GalleryItemViewHolder(parent.inflate(R.layout.item_gallery_view_holder).apply {
        this.updateLayoutParams {
            width = itemSize
            height = itemSize
        }
    })

    override fun getItemCount(): Int = pictures.size

    override fun onBindViewHolder(holder: GalleryItemViewHolder, position: Int) {
        holder.bind(pictures[position], clickListener, imageLoader)
    }

    class GalleryItemViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {

        fun bind(record: GalleryRecord, clickListener: GalleryItemClickListener, imageLoader: ImageLoader) {
            itemView.setOnClickListener {
                clickListener(record)
            }
            imageLoader.loadCroppedImageInto(record.previewImage, itemView as ImageView)
        }

    }

}