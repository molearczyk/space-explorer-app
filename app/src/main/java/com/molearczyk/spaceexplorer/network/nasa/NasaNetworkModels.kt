package com.molearczyk.spaceexplorer.network.nasa

data class CollectionContainer(val collection: CollectionContent)

data class CollectionContent(val href: String,
                             val items: List<CollectionItem>,
                             val links: List<Link>?,
                             val metadata: Metadata,
                             val version: String)

data class CollectionItem(val data: List<DataItem>,
                          val href: String,
                          val links: List<Link>)

data class Link(val href: String,
                val rel: String,
                val render: String?,
                val prompt: String?)

data class Metadata(val total_hits: Int)

data class DataItem(val center: String?,
                    val date_created: String,
                    val description: String?,
                    val keywords: List<String>?,
                    val media_type: String,
                    val nasa_id: String,
                    val photographer: String?,
                    val title: String,
                    val location: String?,
                    val description_508: String?,
                    val secondary_creator: String?,
                    val album: List<String>?)
