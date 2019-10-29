package com.molearczyk.spaceexplorer.ui.main

import com.molearczyk.spaceexplorer.explorationscreen.MainPresenter
import com.molearczyk.spaceexplorer.explorationscreen.MainView
import com.molearczyk.spaceexplorer.network.NasaImagesRepository
import com.molearczyk.spaceexplorer.network.models.GalleryEntry
import com.molearczyk.spaceexplorer.network.nasa.*
import com.molearczyk.spaceexplorer.querytracking.QueryTracker
import com.molearczyk.spaceexplorer.schedulers.TestSchedulerProvider
import io.reactivex.Single
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MainPresenterTest {

    @Mock
    private lateinit var presenterView: MainView

    @Before
    fun setupMocks() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `when setup with correct one item then presenter parses and shows one correct item`() {
        /*Define a correct item*/
        val description = "description"
        val title = "title"
        val googleUrl = "https://google.com".toHttpUrl()
        val onlyExpectedItem = GalleryEntry(googleUrl, googleUrl, title, description)

        /*Prepare fake data */
        val item = DataItem(null, "", description, null, "image", "", null, title, null, null, null, null)
        val collectionItem = CollectionItem(listOf(item), googleUrl.toString(), listOf(Link(googleUrl.toString(), "preview", null, null)))
        val container = CollectionContainer(CollectionContent("", listOf(collectionItem), emptyList(), null, "1.0"))

        /*Setup a presenter*/
        val mainPresenter = MainPresenter(createFakeImageRepository(container), QueryTracker(), TestSchedulerProvider(), presenterView)

        //run the initial invocation
        mainPresenter.onInitializeData()

        Mockito.verify(presenterView).showNewImages(listOf(onlyExpectedItem))
    }

    @Test
    fun `when setup with one correct item and one corrupted item presenter parses and returns one correct item`() {
        val description = "description"
        val title = "title"
        val googleUrl = "https://google.com".toHttpUrl()

        val correctExpectedItem = GalleryEntry(googleUrl, googleUrl, title, description)

        //Prepare fake data - first correct, then faulty
        val correctFakeItem = DataItem(null, "", description, null, "image", "", null, title, null, null, null, null)
        val correctCollectionItem = CollectionItem(listOf(correctFakeItem), googleUrl.toString(), listOf(Link(googleUrl.toString(), "preview", null, null)))

        val brokenUrl = "brokenurl.com"
        val faultyFakeItem = DataItem(null, "", null, null, "error", "", null, "", null, null, null, null)
        val faultyCollectionItem = CollectionItem(listOf(faultyFakeItem), brokenUrl, listOf(Link(brokenUrl, "preview", null, null)))

        val fakeContainer = CollectionContainer(CollectionContent("", listOf(correctCollectionItem, faultyCollectionItem), emptyList(), null, "1.0"))
        val mainPresenter = MainPresenter(createFakeImageRepository(fakeContainer), QueryTracker(), TestSchedulerProvider(), presenterView)

        //run the initial invocation
        mainPresenter.onInitializeData()

        Mockito.verify(presenterView).showNewImages(listOf(correctExpectedItem))
    }

    private fun createFakeImageRepository(fakeContainer: CollectionContainer): NasaImagesRepository {
        return NasaImagesRepository(object : ImagesNasaNetworkApi {

            override fun search(keywords: String?, page: Int?): Single<CollectionContainer> = doNotCallThis()

            override fun fetchPopular(): Single<CollectionContainer> {
                return Single.just(fakeContainer)
            }

            override fun fetchImageLinkContainer(fullPath: HttpUrl): Single<List<String>> = doNotCallThis()

        })

    }

    inline fun doNotCallThis(): Nothing = throw IllegalArgumentException("Must not be called!")

}


