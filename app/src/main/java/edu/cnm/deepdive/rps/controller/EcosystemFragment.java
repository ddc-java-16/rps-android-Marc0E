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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle.State;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.rps.R;
import edu.cnm.deepdive.rps.databinding.FragmentEcosystemBinding;
import edu.cnm.deepdive.rps.viewmodel.EcosystemViewModel;

/**
 * Presents a user interface comprising menu items in the action bar (and the overflow menu), as
 * well as a display of the terrain of the ecosystem simulation (implemented by
 * {@link edu.cnm.deepdive.rps.view.TerrainView}) and additional key measures of the simulation.
 */
@AndroidEntryPoint
public class EcosystemFragment extends Fragment implements MenuProvider {

  private FragmentEcosystemBinding binding;
  private EcosystemViewModel viewModel;
  private boolean running = false;
  private boolean absorbed = true;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    binding = FragmentEcosystemBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    LifecycleOwner owner = getViewLifecycleOwner();
    FragmentActivity activity = requireActivity();
    activity.addMenuProvider(this, owner, State.RESUMED);
    setupViewModel(owner, activity);
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

  @Override
  public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
    menuInflater.inflate(R.menu.ecosystem_options, menu);
  }

  @Override
  public void onPrepareMenu(@NonNull Menu menu) {
    MenuProvider.super.onPrepareMenu(menu);
    menu.findItem(R.id.pause).setVisible(running && !absorbed);
    menu.findItem(R.id.run).setVisible(!running && !absorbed);
  }

  @Override
  public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
    boolean handled = true;
    int itemId = menuItem.getItemId();
    if (itemId == R.id.run) {
      viewModel.run();
    } else if (itemId == R.id.pause) {
      viewModel.pause();
    } else if (itemId == R.id.restart) {
      viewModel.create();
    } else if (itemId == R.id.settings) {
      Navigation.findNavController(binding.getRoot())
          .navigate(EcosystemFragmentDirections.navigateToSettings());
    } else {
      handled = false;
    }
    return handled;
  }

  private void setupViewModel(LifecycleOwner owner, FragmentActivity activity) {
    viewModel = new ViewModelProvider(requireActivity()).get(EcosystemViewModel.class);
    getLifecycle().addObserver(viewModel);
    // When we observe that a new terrain is created, pass it to the TerrainView.
    viewModel
        .getTerrain()
        .observe(owner, (terrain) -> {
          binding.terrain.setTerrain(terrain);
          binding.terrain.invalidate();
        });
    // Any observed ecosystem updates indicate that the terrain content has changed; force redraw.
    viewModel
        .getEcosystem()
        .observe(owner, (ignored) -> binding.terrain.invalidate());
    // When the ecosystem moves into or out of the absorbed/absorbing state, the simulation has
    // either stopped (moved into the absorbing state) or restarted (new ecosystem, not in the
    // absorbing state); in either case, the menu mut be refreshed.
    viewModel
        .getAbsorbed()
        .observe(owner, (absorbed) -> {
          this.absorbed = absorbed;
          activity.invalidateMenu();
        });
    // When we observe the model starts running or is paused, the menu must be refreshed.
    viewModel
        .getRunning()
        .observe(owner, (running) -> {
          this.running = running;
          activity.invalidateMenu();
        });
    // When a new ecosystem is created, the LiveData<Integer> containing the number of breeds is
    // updated; this information must be passed to the TerrainView, so that it can build an array of
    // Paint instances (each with a different color), with a length equal to the number of breeds.
    viewModel
        .getInitialBreedCount()
        .observe(owner, (numBreeds) -> {
          binding.terrain.setNumBreeds(numBreeds);
          binding.terrain.invalidate();
        });

    // TODO Using the same viewModel as is used in the operations above, get a reference to the
    //  LiveData<Long> containing the iteration count of the ecosystem simulation (see the
    //  getIterationCount() method in EcosystemViewModel); observe that LiveData, and pass the value
    //  received by the observer to the corresponding text widgets in the fragment_ecosystem layout,
    //  to display the iteration count.

    // TODO Using the same viewModel as is used in the operations above, get a reference to the
    //  LiveData<Integer> containing the number of extant (surviving) breeds in the ecosystem
    //  simulation (see the getCurrentBreedCount() method in EcosystemViewModel); observe that
    //  LiveData, and pass the value received by the observer to the corresponding text widgets in
    //  the fragment_ecosystem layout, to display the breed count.

    // TODO Using the same viewModel as is used in the operations above, get a reference to the
    //  LiveData<int[]> containing the current population sizes of all of the breeds in the
    //  ecosystem (see the getPopulations() method in EcosystemViewModel); observe that LiveData,
    //  and pass the value received by the observer (after any necessary conversion from int[] to
    //  one or more Strings) to the corresponding text widgets in the fragment_ecosystem layout, to
    //  display the population counts.
  }

}
