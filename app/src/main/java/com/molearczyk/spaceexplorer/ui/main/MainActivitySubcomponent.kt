package com.molearczyk.spaceexplorer.ui.main

import com.molearczyk.spaceexplorer.ImageLoadingModule
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [ImageLoadingModule::class])
interface MainActivitySubcomponent : AndroidInjector<MainActivity> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<MainActivity>
}