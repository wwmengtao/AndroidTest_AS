/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.mt.myapplication.novicetutorial.view.interfaces;


import java.util.Collection;
import com.mt.myapplication.novicetutorial.model.UserModel;
/**
 * Interface representing a View in a model view presenter (MVP) pattern.
 */
public interface NoviceGridView{

  void getUserList(Collection<UserModel> userModelCollection);

  /**
   * View a {@link UserModel} profile/details.
   *
   * @param userModel The user that will be shown.
   */
  void viewUser(UserModel userModel);
}
