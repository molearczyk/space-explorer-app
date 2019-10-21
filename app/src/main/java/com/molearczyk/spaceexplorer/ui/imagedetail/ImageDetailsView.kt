package com.molearczyk.spaceexplorer.ui.imagedetail

import okhttp3.HttpUrl

interface ImageDetailsView {

    fun showImage(url: HttpUrl?)

    fun showInternetAccessError()

    fun showGenericError()
}