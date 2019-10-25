package com.molearczyk.spaceexplorer.imageloading

import dagger.Module
import dagger.Provides

@Module
class ImageLoadingModule {

    @Provides
    fun provideImageLoading(): ImageLoader = GlideImageLoader()

}