package com.mt.myapplication.novicetutorial.dagger;

import android.content.Context;


import com.mt.myapplication.novicetutorial.presenter.NoviceDetailPresenter;
import com.mt.myapplication.novicetutorial.presenter.NoviceGridPresenter;
import com.mt.myapplication.novicetutorial.presenter.Presenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mengtao1 on 2017/10/23.
 */

@Module
public class PresenterModule {
    @Provides
    @Singleton
    Presenter provideGridPresenter(Context context) {
        return new NoviceGridPresenter();
    }

    @Provides
    @Singleton
    Presenter provideDetailPresenter(Context context) {
        return new NoviceDetailPresenter(context);
    }
}
