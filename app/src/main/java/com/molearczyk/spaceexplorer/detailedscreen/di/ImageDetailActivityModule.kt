package com.molearczyk.spaceexplorer.detailedscreen.di

import com.molearczyk.spaceexplorer.detailedscreen.ImageDetailActivity
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [ImageDetailActivitySubcomponent::class])
abstract class ImageDetailActivityModule {

    @Binds
    @IntoMap
    @ClassKey(ImageDetailActivity::class)
    abstract fun bindImageDetailAndroidInjectorFactory(factory: ImageDetailActivitySubcomponent.Factory): AndroidInjector.Factory<*>
}
