package com.molearczyk.spaceexplorer.ui.imagedetail

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface ImageDetailActivitySubcomponent : AndroidInjector<ImageDetailActivity> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<ImageDetailActivity>
}