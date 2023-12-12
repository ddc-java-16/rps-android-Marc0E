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
package edu.cnm.deepdive.rps.controller;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.rps.R;

/**
 * Serves as a basic fragment host activity. This activity presents no UI elements of its own, but
 * hosts a {@link androidx.navigation.fragment.NavHostFragment} navigation controller, to present
 * (and navigate between) {@link androidx.fragment.app.Fragment} instances, with the navigation
 * controller wired to an {@link AppBarConfiguration} for automatic updates of the text displayed in
 * the appbar (aka action bar) when navigating between fragments.
 */
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setupActionBar();
  }

  /**
   * Treats a press on the "up" button (when displayed&mdash;typically as a left arrow in the action
   * bar) as equivalent to a "back" navigation. This results in popping the currently displayed
   * fragment off the stack, and returning to the previously displayed fragment.
   *
   * @return {@code true} to indicate that navigation was successful.
   */
  @Override
  public boolean onSupportNavigateUp() {
    getOnBackPressedDispatcher().onBackPressed();
    return true;
  }

  private void setupActionBar() {
    AppBarConfiguration config = new AppBarConfiguration.Builder(
        R.id.ecosystem_fragment, R.id.settings_fragment
    )
        .build();
    //noinspection DataFlowIssue
    NavController controller = ((NavHostFragment)
        getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();
    NavigationUI.setupActionBarWithNavController(this, controller, config);
  }

}