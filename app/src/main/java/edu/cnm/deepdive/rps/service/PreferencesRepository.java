/*
 *  Copyright 2023 CNM Ingenuity, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package edu.cnm.deepdive.rps.service;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;
import dagger.hilt.android.qualifiers.ApplicationContext;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implements a {@link LiveData}-based repository of preferences. It does not provide any preference
 * mutation methods, and is of most use when all preferences are being modified through user
 * interaction with an instance of a {@link androidx.preference.PreferenceFragmentCompat} subclass.
 */
@Singleton
public class PreferencesRepository {

  private final MutableLiveData<SharedPreferences> preferences;
  private final SharedPreferences prefs;

  @Inject
  PreferencesRepository(@ApplicationContext Context context) {
    prefs = PreferenceManager.getDefaultSharedPreferences(context);
    preferences = new MutableLiveData<>(prefs);
    prefs.registerOnSharedPreferenceChangeListener((prefs, key) -> preferences.postValue(prefs));
  }

  /**
   * Returns {@link LiveData} of all shared preferences that have been set in application code or by
   * the user in a {@link androidx.preference.PreferenceFragmentCompat} subclass instance. A
   * viewmodel should then use methods of {@link androidx.lifecycle.Transformations} (or similar
   * mechanisms) to transform the entire {@link SharedPreferences} instance in the {@link LiveData}
   * returned by this method to {@link LiveData} fields for individual preferences.
   */
  public LiveData<SharedPreferences> getPreferences() {
    return preferences;
  }

  /**
   * Allows pass-through read access to a specified (by key) value from the underlying
   * {@link SharedPreferences}.
   *
   * @param key Permission {@link String} lookup key. 
   * @param defaultValue Value to return if specified permission has not been set.
   * @return Permission value (or {@code defaultValue}).
   * @param <T> Permission type.
   */
  public <T> T get(String key, T defaultValue) {
    //noinspection unchecked
    T result = (T) prefs.getAll().get(key);
    return (result != null) ? result : defaultValue;
  }

}
