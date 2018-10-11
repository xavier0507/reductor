package javatest.colorhaake.com.example3.view.main

import javatest.colorhaake.com.example3.model.AppState
import javatest.colorhaake.com.example3.reducer.main.MainActions
import javatest.colorhaake.com.example3.view.base.BasePresenter
import com.yheriatovych.reductor.Store

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class MainPresenter(
        private val state: Observable<AppState>,
        private val store: Store<AppState>,
        private val actions: MainActions
) : BasePresenter<MainMvpView>() {

    private val disposables = CompositeDisposable()
    private val searchImagesSubject = PublishSubject.create<Pair<String, Int>>()

    override fun attachView(mvpView: MainMvpView) {
        super.attachView(mvpView)
        register()
    }

    override fun detachView() {
        super.detachView()
        disposables.dispose()
    }

    private fun register() {
        state.map(AppState::searchImageData)
                .distinctUntilChanged()
                .subscribe { mvpView?.showMainPage(it) }
                .let(disposables::add)

        searchImagesSubject.sample(1, TimeUnit.SECONDS)
                .subscribe { (term, page) ->
                    store.dispatch(actions.searchImages(term, page))
                }
                .let(disposables::add)
    }

    fun viewReady() {
        store.dispatch(actions.viewReady())
    }

    fun searchImages(term: String, page: Int) {
        searchImagesSubject.onNext(Pair(term, page))
    }
}
