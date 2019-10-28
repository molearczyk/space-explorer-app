package com.molearczyk.spaceexplorer.ui.main

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.molearczyk.spaceexplorer.*
import com.molearczyk.spaceexplorer.imageloading.ImageLoader
import com.molearczyk.spaceexplorer.network.models.GalleryEntry
import com.molearczyk.spaceexplorer.ui.detail.ImageDetailActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_error_state.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), MainView {

    @Inject
    lateinit var presenter: MainPresenter

    @Inject
    lateinit var imageLoader: ImageLoader

    private val gridSpan: Int by lazyOnMainThread {
        resources.getInteger(R.integer.main_grid_span)
    }


    private lateinit var adapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.app_name)
        toolbar.setTitleMargin(resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin_doubled), 0, 0, 0)

        presenter.initView(this)

        spaceImagesRecyclerView.layoutManager = GridLayoutManager(this, gridSpan, RecyclerView.VERTICAL, false)//(this, 2)
        adapter = GalleryAdapter(this::navigateToFullscreen, imageLoader, presenter::onNextPageRequested, gridSpan, this)
        spaceImagesRecyclerView.adapter = adapter

        retryButton.setOnClickListener {
            presenter.onRetryClicked()
        }

        searchInputLayout.editText!!.setOnEditorActionListener { view, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    presenter.onQuerySearch((view as EditText).editableText)
                    view.hideKeyboard()
                    true
                }
                else -> false
            }
        }
        searchInputLayout.setEndIconOnClickListener {
            searchInputLayout.editText!!.text.clear()
            presenter.onDefaultContent()
        }
        presenter.onDefaultContent()
    }


    override fun onDestroy() {
        searchInputLayout.clearOnEndIconChangedListeners()
        presenter.onCleanup()
        super.onDestroy()
    }

    override fun showNewImages(newImages: List<GalleryEntry>) {
        adapter.setNewEntries(newImages)
    }

    override fun appendImages(additionalImages: List<GalleryEntry>) {
        adapter.appendEntries(additionalImages)
    }

    override fun navigateToFullscreen(event: GalleryEntryEvent) {
        val clickedView = spaceImagesRecyclerView.layoutManager?.findViewByPosition(event.layoutPosition)!!
        startActivity(Intent(this@MainActivity, ImageDetailActivity::class.java).putGalleryEvent(event),
                ActivityOptions.makeScaleUpAnimation(clickedView, 0, clickedView.height / 2, clickedView.width, clickedView.height).toBundle())
    }

    override fun hidePromptViews() {
        retryButton.gone()
        noContentPromptView.gone()
    }

    override fun showInternetAccessError() {
        noContentPromptView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_error_outline_accent_24dp, 0, 0)
        noContentPromptView.setText(R.string.error_no_internet_description)
        noContentPromptView.show()
        retryButton.show()
    }

    override fun showGenericError() {
        noContentPromptView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_error_outline_accent_24dp, 0, 0)
        noContentPromptView.setText(R.string.error_generic_description)
        noContentPromptView.show()
        retryButton.show()
    }

    override fun showNoResultsWarning() {
        noContentPromptView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_sentiment_dissatisfied_white_24dp, 0, 0)
        noContentPromptView.setText(R.string.warning_no_results)
        noContentPromptView.show()
    }

}
