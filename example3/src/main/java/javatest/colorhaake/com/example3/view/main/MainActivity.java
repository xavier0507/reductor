package javatest.colorhaake.com.example3.view.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javatest.colorhaake.com.example3.R;
import javatest.colorhaake.com.example3.Example3App;
import javatest.colorhaake.com.example3.injection.component.ActivityComponent;
import javatest.colorhaake.com.example3.injection.component.DaggerActivityComponent;
import javatest.colorhaake.com.example3.injection.module.ActivityModule;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainMvpView {

    public static final String TAG = MainActivity.class.getName();

    @Inject MainPresenter mMainPresenter;

    private ActivityComponent activityComponent;
    public ActivityComponent activityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(Example3App.get(this).component())
                    .build();
        }
        return activityComponent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityComponent().inject(this);

        mMainPresenter.attachView(this);
        mMainPresenter.viewReady(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMainPresenter.detachView();
    }


    /***** MVP View methods implementation *****/

    @Override
    public void showMainPage(List<String> list) { }
}
