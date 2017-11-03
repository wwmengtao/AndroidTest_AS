/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.android10.sample.data.database.xmlOps;

import com.fernandocejas.android10.sample.data.database.DbCache;
import com.fernandocejas.android10.sample.data.entity.UserEntityNT;
import com.fernandocejas.android10.sample.domain.interactor.GetUserListDetails;

import java.util.List;

import io.reactivex.Observable;

/**
 * ParseXml for retrieving data from the xml files.
 */
public interface ParseXml {
  Observable<List<UserEntityNT>> userEntityNTList(final GetUserListDetails.Params params, DbCache dbCache);
  Observable<UserEntityNT> userEntityNT(final GetUserListDetails.Params params, DbCache dbCache);
}
