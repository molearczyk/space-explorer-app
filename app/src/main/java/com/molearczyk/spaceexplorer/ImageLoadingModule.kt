package com.molearczyk.spaceexplorer

import com.molearczyk.spaceexplorer.ui.GlideImageLoader
import com.molearczyk.spaceexplorer.ui.ImageLoader
import dagger.Module
import dagger.Provides

@Module
class ImageLoadingModule {

    @Provides
    fun provideImageLoading(): ImageLoader = GlideImageLoader()

}