package com.molearczyk.spaceexplorer

import android.content.Context
import com.molearczyk.spaceexplorer.detailedscreen.di.ImageDetailActivityModule
import com.molearczyk.spaceexplorer.explorationscreen.di.MainActivityModule
import com.molearczyk.spaceexplorer.network.NetworkModule
import com.molearczyk.spaceexplorer.schedulers.DefaultSchedulerProvider
import com.molearczyk.spaceexplorer.schedulers.SchedulerProvider
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Reusable
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

    @Provides
    @Reusable
    fun provideAppSchedulers(): SchedulerProvider = DefaultSchedulerProvider()

}


