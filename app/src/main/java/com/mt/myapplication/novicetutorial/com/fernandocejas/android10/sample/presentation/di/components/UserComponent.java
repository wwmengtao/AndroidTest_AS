/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.components;


import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.PerActivity;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.modules.ActivityModule;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.modules.UserModule;
import com.mt.myapplication.novicetutorial.view.fragments.NoviceDetailFragment;
import com.mt.myapplication.novicetutorial.view.fragments.NoviceGridFragment;
import com.mt.myapplication.novicetutorial.view.fragments.NoviceListFragment;

import dagger.Component;

/**
 * A scope {@link com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.PerActivity} component.
 * Injects user specific Fragments.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, UserModule.class})
public interface UserComponent extends ActivityComponent {
  void inject(NoviceGridFragment mNoviceGridFragment);
  void inject(NoviceListFragment mNoviceListFragment);
  void inject(NoviceDetailFragment mNoviceDetailFragment);

}
