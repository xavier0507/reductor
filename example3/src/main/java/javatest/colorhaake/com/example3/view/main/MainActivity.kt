package javatest.colorhaake.com.example3.view.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import javatest.colorhaake.com.example3.R
import javatest.colorhaake.com.example3.Example3App
import javatest.colorhaake.com.example3.injection.component.ActivityComponent
import javatest.colorhaake.com.example3.injection.component.DaggerActivityComponent
import javatest.colorhaake.com.example3.injection.module.ActivityModule

import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainMvpView {

    @Inject
    lateinit var mMainPresenter: MainPresenter

    private val component: ActivityComponent by lazy {
        DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .applicationComponent(Example3App[this].component())
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        component.inject(this)

        mMainPresenter.attachView(this)
        mMainPresenter.viewReady(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        mMainPresenter.detachView()
    }


    /***** MVP View methods implementation  */

    override fun showMainPage(list: List<String>) {}
}
