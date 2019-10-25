package com.molearczyk.spaceexplorer.ui.detail

sealed class Description {

    abstract val shortText: CharSequence
    abstract val longText: CharSequence


    data class NonExpandable(val text: CharSequence) : Description() {

        override val shortText: CharSequence
            get() = text
        override val longText: CharSequence
            get() = text


    }

    data class Expandable(override val shortText: CharSequence, override val longText: CharSequence) : Description()

}