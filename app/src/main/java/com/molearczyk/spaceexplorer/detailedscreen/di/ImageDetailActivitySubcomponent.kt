package com.molearczyk.spaceexplorer.detailedscreen.di

import com.molearczyk.spaceexplorer.detailedscreen.ImageDetailActivity
import com.molearczyk.spaceexplorer.imageloading.ImageLoadingModule
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [ImageLoadingModule::class, ImageDetailProviderModule::class])
interface ImageDetailActivitySubcomponent : AndroidInjector<ImageDetailActivity> {

    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<ImageDetailActivity>

}
