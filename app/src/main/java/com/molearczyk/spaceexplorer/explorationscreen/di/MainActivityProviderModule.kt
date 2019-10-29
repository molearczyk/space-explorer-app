package com.molearczyk.spaceexplorer.explorationscreen.di

import com.molearczyk.spaceexplorer.explorationscreen.MainActivity
import com.molearczyk.spaceexplorer.explorationscreen.MainView
import dagger.Module
import dagger.Provides

@Module
class MainActivityProviderModule {

    @Provides
    fun provideMainActivityView(activity: MainActivity): MainView = activity

}