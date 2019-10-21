package com.molearczyk.spaceexplorer.ui.imagedetail

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.molearczyk.spaceexplorer.R
import com.molearczyk.spaceexplorer.gone
import com.molearczyk.spaceexplorer.show
import com.molearczyk.spaceexplorer.ui.main.GalleryRecordEvent
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_image_detail.*
import kotlinx.android.synthetic.main.activity_image_detail.toolbar
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.HttpUrl
import javax.inject.Inject

class ImageDetailActivity : AppCompatActivity(), ImageDetailsView {

    @Inject
    lateinit var presenter: ImageDetailPresenter

    private val handler = Handler()
    private val mHidePart2Runnable = Runnable {

        fullscreenImageView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val mShowPart2Runnable = Runnable {
        supportActionBar?.show()
        fullscreen_content_controls.visibility = View.VISIBLE
    }
    private var areSystemControlsVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        areSystemControlsVisible = true
        fullscreenImageView.setOnClickListener { toggle() }


        presenter.initView(this)
        val event = intent.getParcelableExtra<GalleryRecordEvent>("QQ")
        presenter.fetchImageDetail(event!!)

        imageDescriptionTextView.text = presenter.resolveDescription(event)
    }

    override fun onDestroy() {
        presenter.onCleanup()
        super.onDestroy()
    }

    override fun showImage(url: HttpUrl?) {
        Glide.with(fullscreenImageView)
                .load(url.toString())
                .centerInside()
                .into(fullscreenImageView)

    }

    override fun showInternetAccessError() {
        retryButton.show()//TODO add retry logic
        retryButton.setOnClickListener {

        }
        noContentPromptView.setText(R.string.error_no_internet_description)
        noContentPromptView.show()
    }

    override fun showGenericError() {
        retryButton.show()//TODO add retry logic
        noContentPromptView.show()
        noContentPromptView.setText(R.string.error_generic_description)
    }

    private fun toggle() {
        if (areSystemControlsVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        supportActionBar?.hide()
        fullscreen_content_controls.gone()
        areSystemControlsVisible = false

        handler.removeCallbacks(mShowPart2Runnable)
        handler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY)
    }

    private fun show() {
        // Show the system bar
        fullscreenImageView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        areSystemControlsVisible = true

        // Schedule a runnable to display UI elements after a delay
        handler.removeCallbacks(mHidePart2Runnable)
        handler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY)
    }


    companion object {

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300L
    }
}

