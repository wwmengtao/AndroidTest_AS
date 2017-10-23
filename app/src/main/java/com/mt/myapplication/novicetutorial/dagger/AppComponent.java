package com.mt.myapplication.novicetutorial.dagger;

import com.mt.myapplication.novicetutorial.presenter.NoviceDetailPresenter;
import com.mt.myapplication.novicetutorial.presenter.NoviceGridPresenter;
import com.mt.myapplication.novicetutorial.view.activities.NoviceDetailActivity;
import com.mt.myapplication.novicetutorial.view.activities.NoviceGridActivity;
import com.mt.myapplication.novicetutorial.view.fragments.NoviceDetailFragment;
import com.mt.myapplication.novicetutorial.view.fragments.NoviceGridFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by mengtao1 on 2017/10/23.
 */

@Singleton
@Component(modules = {AppModule.class, PresenterModule.class})
public interface AppComponent {
    void inject(NoviceGridActivity target);
    void inject(NoviceDetailActivity target);
    void inject(NoviceGridFragment target);
    void inject(NoviceDetailFragment target);
    void inject(NoviceGridPresenter target);
    void inject(NoviceDetailPresenter target);
}
