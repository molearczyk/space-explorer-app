package com.molearczyk.spaceexplorer.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.molearczyk.spaceexplorer.R
import com.molearczyk.spaceexplorer.inflate

class GalleryAdapter(private val galleryItems:List<Any>) : RecyclerView.Adapter<GalleryAdapter.GalleryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryItemViewHolder = GalleryItemViewHolder(parent.inflate(R.layout.item_gallery_view_holder))

    override fun getItemCount(): Int = galleryItems.size

    override fun onBindViewHolder(holder: GalleryItemViewHolder, position: Int) {
        holder.bind(galleryItems[position])
    }

    class GalleryItemViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {

        fun bind(any: Any) {

        }


    }

}
