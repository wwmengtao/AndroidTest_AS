package com.mt.myapplication.novicetutorial.dagger;

import com.raywenderlich.android.deezfoodz.ui.food.FoodPresenterImpl;
import com.raywenderlich.android.deezfoodz.ui.foodz.FoodzActivity;
import com.raywenderlich.android.deezfoodz.ui.foodz.FoodzPresenterImpl;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by mengtao1 on 2017/10/23.
 */

@Singleton
@Component(modules = {AppModule.class, PresenterModule.class, NetworkModule.class})
public interface AppComponent {
    void inject(FoodzActivity target);
    void inject(FoodzPresenterImpl target);
    void inject(FoodPresenterImpl target);
}
