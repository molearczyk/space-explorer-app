package com.molearczyk.spaceexplorer.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.molearczyk.spaceexplorer.R
import com.molearczyk.spaceexplorer.network.models.GalleryRecord
import com.molearczyk.spaceexplorer.show
import com.molearczyk.spaceexplorer.ui.GalleryAdapter
import com.molearczyk.spaceexplorer.ui.MainView
import com.molearczyk.spaceexplorer.ui.imagedetail.ImageDetailActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), MainView {

    @Inject
    lateinit var presenter: MainPresenter

    private val gridSpan: Int by lazy {
        resources.getInteger(R.integer.main_grid_span)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.app_name)

        presenter.initView(this)

        spaceImagesRecyclerView.layoutManager = GridLayoutManager(this, gridSpan, RecyclerView.VERTICAL, false)//(this, 2)

        searchInputLayout.editText!!.setOnEditorActionListener { view, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    presenter.onQuerySearch((view as EditText).editableText)
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

    override fun showImages(newImages: List<GalleryRecord>) {
        spaceImagesRecyclerView.adapter = GalleryAdapter(newImages, this::navigateToFullscreen, gridSpan, this)
    }

    override fun navigateToFullscreen(event: GalleryRecord) {
        startActivity(Intent(this@MainActivity, ImageDetailActivity::class.java).putExtra("QQ", GalleryRecordEvent(event.details.toString(), event.title, event.description)))
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

}
