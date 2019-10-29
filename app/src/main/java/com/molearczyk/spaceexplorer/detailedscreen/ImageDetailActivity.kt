package com.molearczyk.spaceexplorer.detailedscreen

import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.doOnNextLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.transition.TransitionManager
import com.molearczyk.spaceexplorer.R
import com.molearczyk.spaceexplorer.basics.gone
import com.molearczyk.spaceexplorer.basics.invisible
import com.molearczyk.spaceexplorer.basics.show
import com.molearczyk.spaceexplorer.detailedscreen.models.Description
import com.molearczyk.spaceexplorer.imageloading.ImageLoader
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_image_detail.*
import kotlinx.android.synthetic.main.layout_error_state.*
import okhttp3.HttpUrl
import javax.inject.Inject
import kotlin.math.max


class ImageDetailActivity : AppCompatActivity(), ImageDetailsView, HasAndroidInjector {

    @Inject
    lateinit var presenter: ImageDetailPresenter

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    private val handler = Handler()

    private val hideSystemUiRunnable = Runnable {
        fullscreenImageView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    private val showSystemUiRunnable = Runnable {
        supportActionBar?.show()
        contentDescriptionLinearLayout.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)
        setupToolbar(toolbar)

        presenter.fetchImageDetail()

        fullscreenImageView.setOnClickListener { presenter.onFullImageClick() }
        imageTitleTextView.text = presenter.resolveTitle()

        imageDescriptionTextView.doOnNextLayout { _ ->
            imageDescriptionTextView.run {
                when (val descriptionMode = presenter.resolveDescriptionSizing(paint, computeAvailableTextSpace())) {
                    is Description.NonExpandable -> {
                        showNonExpandableDescription(descriptionMode.text)
                    }
                    is Description.Expandable -> {
                        showExpandableDescription(descriptionMode.shortText)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        presenter.onCleanup()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                android.R.id.home -> {
                    supportFinishAfterTransition()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    override fun showImage(url: HttpUrl) {
        retryContentInclude.gone()
        imageLoader.loadFullImageInto(url, fullscreenImageView)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    override fun showInternetAccessError() {
        retryButton.show()
        retryButton.setOnClickListener {
            presenter.retryOnClick()
        }
        noContentPromptView.setText(R.string.error_no_internet_description)
        noContentPromptView.show()
    }

    override fun showGenericError() {
        retryButton.show()
        retryButton.setOnClickListener {
            presenter.retryOnClick()
        }
        noContentPromptView.show()
        noContentPromptView.setText(R.string.error_generic_description)
    }

    override fun hideSystemUi() {
        supportActionBar?.hide()
        contentDescriptionLinearLayout.gone()

        handler.removeCallbacks(showSystemUiRunnable)
        handler.postDelayed(hideSystemUiRunnable, UI_ANIMATION_DELAY)
    }

    override fun showSystemUi() {
        // Show the system bar
        fullscreenImageView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        // Schedule a runnable to display UI elements after a delay
        handler.removeCallbacks(hideSystemUiRunnable)
        handler.postDelayed(showSystemUiRunnable, UI_ANIMATION_DELAY)
    }

    override fun transitionToFullDescription(fullDescription: CharSequence) {
        TransitionManager.beginDelayedTransition(rootConstraintLayout)
        val set = ConstraintSet()
        set.clone(rootConstraintLayout)
        set.setDimensionRatio(R.id.contentWrapperFrameLayout, null)
        set.setMargin(R.id.contentWrapperFrameLayout, ConstraintSet.TOP, resources.getDimensionPixelSize(R.dimen.activity_vertical_margin))
        set.connect(R.id.contentWrapperFrameLayout, ConstraintSet.TOP, R.id.toolbarWrapperAppBarLayout, ConstraintSet.BOTTOM)
        set.applyTo(rootConstraintLayout)
        contentDescriptionLinearLayout.updateLayoutParams {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        imageDescriptionTextView.text = fullDescription
        if (retryContentInclude.isVisible) {
            retryContentInclude.invisible()
        }
    }

    override fun transitionToShortDescription(shortDescription: CharSequence) {
        TransitionManager.beginDelayedTransition(rootConstraintLayout)
        val set = ConstraintSet()
        set.clone(rootConstraintLayout)
        set.setDimensionRatio(R.id.contentWrapperFrameLayout, getString(R.string.image_description_ratio))
        set.setMargin(R.id.contentWrapperFrameLayout, ConstraintSet.TOP, 0)
        set.clear(R.id.contentWrapperFrameLayout, ConstraintSet.TOP)
        set.applyTo(rootConstraintLayout)
        contentDescriptionLinearLayout.updateLayoutParams {
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        imageDescriptionTextView.text = shortDescription
        if (retryContentInclude.isInvisible) {
            retryContentInclude.show()
        }
    }

    /**
     * For any case that text is longer than available space, show to a user a truncated version of description + option to expand on click.
     */
    private fun showExpandableDescription(shortDescription: CharSequence) {
        imageTitleTextView.run {
            setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.selector_arrow, 0)
            setOnClickListener {
                isActivated = !isActivated
                presenter.onExpandClick()
            }
        }
        imageDescriptionTextView.run {
            text = shortDescription
        }
        imageDescriptionTextView.setOnClickListener(null)
    }

    private fun showNonExpandableDescription(description: CharSequence) {
        imageDescriptionTextView.text = description
        imageTitleTextView.run {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            setOnClickListener(null)
        }
    }

    private fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    private fun TextView.computeAvailableTextSpace() =
            width.toFloat() * max((height / lineHeight) - 1f/*for extra padding*/, 1f)


    companion object {

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300L
    }
}

