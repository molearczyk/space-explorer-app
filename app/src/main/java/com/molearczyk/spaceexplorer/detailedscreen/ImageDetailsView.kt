package com.molearczyk.spaceexplorer.detailedscreen

import okhttp3.HttpUrl

interface ImageDetailsView {

    fun showImage(url: HttpUrl)

    fun showInternetAccessError()

    fun showGenericError()

    fun hideSystemUi()

    fun showSystemUi()

    fun transitionToShortDescription(shortDescription: CharSequence)

    fun transitionToFullDescription(fullDescription: CharSequence)
}