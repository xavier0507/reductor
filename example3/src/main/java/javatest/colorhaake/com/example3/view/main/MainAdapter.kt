package javatest.colorhaake.com.example3.view.main

import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import javatest.colorhaake.com.example3.model.ImageData
import javatest.colorhaake.com.example3.model.Response

class MainAdapter : ListDelegationAdapter<List<Item<*>>>() {

    init {
        delegatesManager
                .addDelegate(ViewType.SEARCH_BAR, SearchBarDelegate())
                .addDelegate(ViewType.SEARCH_COUNT, SearchCountDelegate())
                .addDelegate(ViewType.SEARCH_IMAGES, SearchImagesDelegate())
    }

    fun updateSearchImageData(
            searchImageData: Response<ImageData>,
            onSearchTextChangeListener: (String) -> Unit
    ) {
        updateData(
                searchImageData,
                onSearchTextChangeListener
        )
    }

    private fun updateData(
            searchImageData: Response<ImageData>,
            onSearchTextChangeListener: (String) -> Unit
    ) {
        val searchBarInfo = object : Item<SearchBarViewInfo>(SearchBarViewInfo(onSearchTextChangeListener)) {
            override fun getViewType() = ViewType.SEARCH_BAR
        }

        val searchCountInfo = object : Item<Int>(searchImageData.totalHits) {
            override fun getViewType() = ViewType.SEARCH_COUNT
        }

        val searchImageDataList = searchImageData.hits.chunked(3).map {
            object : Item<List<ImageData>>(it) {
                override fun getViewType() = ViewType.SEARCH_IMAGES
            }
        }

        items = listOf(searchBarInfo, searchCountInfo) + searchImageDataList

        setItems(items)
        notifyDataSetChanged()
    }
}