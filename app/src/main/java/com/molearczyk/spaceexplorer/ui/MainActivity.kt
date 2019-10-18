package com.molearczyk.spaceexplorer.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.molearczyk.spaceexplorer.R
import com.molearczyk.spaceexplorer.isLandscapeOrientation
import com.molearczyk.spaceexplorer.isNetworkError
import com.molearczyk.spaceexplorer.network.NasaImagesRepository
import com.molearczyk.spaceexplorer.network.NetworkModule
import com.molearczyk.spaceexplorer.network.models.ServerProperties
import com.molearczyk.spaceexplorer.show
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.app_name)
        val networkModule = NetworkModule()
        val serverProperties: ServerProperties = networkModule.provideServerProperties()
        val retrofit: Retrofit = networkModule.provideRetrofit(serverProperties)
        val nasaImageNetworkApi = networkModule.provideNasaImageNetworkApi(retrofit)

        val gridSpan = if (isLandscapeOrientation) {
            3
        } else {
            2
        }
        spaceImagesRecyclerView.layoutManager = GridLayoutManager(this, gridSpan, RecyclerView.VERTICAL, false)//(this, 2)
        NasaImagesRepository(nasaImageNetworkApi)
                .fetchImages()
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    spaceImagesRecyclerView.adapter = GalleryAdapter(it, {
                        startActivity(Intent(this, ImageDetailActivity::class.java))
                    }, gridSpan, this)
                }, {
                    if (it.isNetworkError()) {
                        Log.w("MainActivity", "Caught network error", it)
                        showInternetAccessError()
                    } else {
                        Log.e("MainActivity", "Caught generic error", it)
                        showGenericError()
                    }
                })


    }


    fun showInternetAccessError() {
        retryButton.show()//TODO add retry logic
        noContentPromptView.setText(R.string.error_no_internet_description)
        noContentPromptView.show()
    }

    fun showGenericError() {
        retryButton.show()//TODO add retry logic
        noContentPromptView.show()
        noContentPromptView.setText(R.string.error_generic_description)
    }


}


