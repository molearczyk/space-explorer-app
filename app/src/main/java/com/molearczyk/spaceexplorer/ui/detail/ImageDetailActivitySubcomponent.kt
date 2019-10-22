package com.molearczyk.spaceexplorer.ui.detail

import com.molearczyk.spaceexplorer.ImageLoadingModule
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [ImageLoadingModule::class])
interface ImageDetailActivitySubcomponent : AndroidInjector<ImageDetailActivity> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<ImageDetailActivity>
}