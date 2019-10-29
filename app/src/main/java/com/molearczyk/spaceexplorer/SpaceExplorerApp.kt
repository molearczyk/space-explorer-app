package com.molearczyk.spaceexplorer

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class SpaceExplorerApp : Application(), HasAndroidInjector {
    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>


    override fun onCreate() {
        super.onCreate()
        DaggerSpaceExplorerAppComponent.builder().appModule(AppModule(this)).build().inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return injector
    }

}
