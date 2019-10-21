package com.molearczyk.spaceexplorer.ui

import android.content.Context
import com.molearczyk.spaceexplorer.SpaceExplorerApp
import com.molearczyk.spaceexplorer.network.NetworkModule
import com.molearczyk.spaceexplorer.ui.imagedetail.ImageDetailActivityModule
import com.molearczyk.spaceexplorer.ui.main.MainActivityModule
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Component(
        dependencies = [],
        modules = [AppModule::class, NetworkModule::class, MainActivityModule::class, ImageDetailActivityModule::class, AndroidInjectionModule::class]
)
@Singleton
interface SpaceExplorerAppComponent {

    fun inject(app: SpaceExplorerApp)

}


@Module(includes = [])
class AppModule(private val app: SpaceExplorerApp) {

    @Provides
    fun provideAppContext(): Context = app.applicationContext

}


