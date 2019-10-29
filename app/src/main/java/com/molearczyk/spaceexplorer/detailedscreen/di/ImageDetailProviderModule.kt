package com.molearczyk.spaceexplorer.detailedscreen.di

import com.molearczyk.spaceexplorer.basics.getGalleryEvent
import com.molearczyk.spaceexplorer.detailedscreen.ImageDetailActivity
import com.molearczyk.spaceexplorer.detailedscreen.ImageDetailsView
import com.molearczyk.spaceexplorer.explorationscreen.models.GalleryEntryEvent
import dagger.Module
import dagger.Provides

@Module
class ImageDetailProviderModule {


    @Provides
    fun provideImageDetailView(activity: ImageDetailActivity): ImageDetailsView = activity

    @Provides
    fun provideGalleryEvent(activity: ImageDetailActivity): GalleryEntryEvent = activity.intent.getGalleryEvent()

}