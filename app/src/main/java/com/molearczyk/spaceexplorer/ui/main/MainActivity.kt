package com.molearczyk.spaceexplorer.ui.main

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.molearczyk.spaceexplorer.*
import com.molearczyk.spaceexplorer.network.models.GalleryEntry
import com.molearczyk.spaceexplorer.ui.GalleryAdapter
import com.molearczyk.spaceexplorer.ui.ImageLoader
import com.molearczyk.spaceexplorer.ui.MainView
import com.molearczyk.spaceexplorer.ui.detail.ImageDetailActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), MainView {

    @Inject
    lateinit var presenter: MainPresenter

    @Inject
    lateinit var imageLoader: ImageLoader

    private val gridSpan: Int by lazyOnMainThread {
        resources.getInteger(R.integer.main_grid_span)
    }


    lateinit var adapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.app_name)

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
        presenter.onQuerySearch()
    }


    override fun onDestroy() {
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
                ActivityOptions.makeSceneTransitionAnimation(this, Pair(clickedView, "image_clicked")).toBundle())
    }

    override fun hidePromptViews() {
        retryButton.gone()
        noContentPromptView.gone()
    }

    override fun showInternetAccessError() {
        noContentPromptView.setText(R.string.error_no_internet_description)
        noContentPromptView.show()
        retryButton.show()
    }

    override fun showGenericError() {
        noContentPromptView.setText(R.string.error_generic_description)
        noContentPromptView.show()
        retryButton.show()
    }

    override fun showNoResultsWarning() {
        noContentPromptView.setText(R.string.warning_no_results)
        noContentPromptView.show()
    }

}
