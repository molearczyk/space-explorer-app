package com.molearczyk.spaceexplorer.explorationscreen.di

import com.molearczyk.spaceexplorer.explorationscreen.MainActivity
import com.molearczyk.spaceexplorer.imageloading.ImageLoadingModule
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [ImageLoadingModule::class, MainActivityProviderModule::class])
interface MainActivitySubcomponent : AndroidInjector<MainActivity> {

    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<MainActivity>
}