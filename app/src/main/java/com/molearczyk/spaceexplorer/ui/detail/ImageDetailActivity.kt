package com.molearczyk.spaceexplorer.ui.detail

import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.molearczyk.spaceexplorer.getGalleryEvent
import com.molearczyk.spaceexplorer.gone
import com.molearczyk.spaceexplorer.show
import com.molearczyk.spaceexplorer.ui.ImageLoader
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_image_detail.*
import kotlinx.android.synthetic.main.activity_image_detail.toolbar
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.HttpUrl
import javax.inject.Inject


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
        setContentView(com.molearczyk.spaceexplorer.R.layout.activity_image_detail)
        setupToolbar(toolbar)
        fullscreenImageView.setOnClickListener { presenter.onFullImageClick() }

        presenter.initView(this)
        val event = intent.getGalleryEvent()
        presenter.fetchImageDetail(event)

        imageDescriptionTextView.text = presenter.resolveDescription(event)
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
        imageLoader.loadCenteredImageInto(url, fullscreenImageView)

    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    override fun showInternetAccessError() {
        retryButton.show()
        retryButton.setOnClickListener {
            presenter.retryOnClick()
        }
        noContentPromptView.setText(com.molearczyk.spaceexplorer.R.string.error_no_internet_description)
        noContentPromptView.show()
    }

    override fun showGenericError() {
        retryButton.show()
        retryButton.setOnClickListener {
            presenter.retryOnClick()
        }
        noContentPromptView.show()
        noContentPromptView.setText(com.molearczyk.spaceexplorer.R.string.error_generic_description)
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

    private fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }


    companion object {

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300L
    }
}

