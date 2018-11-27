package io.shortway.notes.navigation;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import io.shortway.notes.R;
import io.shortway.notes.databinding.ActivityNavBinding;
import io.shortway.notes.domain.model.Event;
import io.shortway.notes.ui.SnackBarContent;

/**
 * Our only Activity. It hosts the navigation graph.
 */
public class NavActivity extends AppCompatActivity implements NavController.OnNavigatedListener {

    private ActivityNavBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_nav);


        setSupportActionBar(binding.toolbar);
        final NavViewModel navViewModel = ViewModelProviders.of(this).get(NavViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setNavViewModel(navViewModel);
        // Observe the title to set.
        navViewModel.getTitle().observe(this, new Observer<String>() {
            // Suppressing the warning about a possible NPE from getSupportActionBar().
            // We want it to throw when getSupportActionBar() returns null.
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onChanged(String title) {
                getSupportActionBar().setTitle(title);
            }
        });

        // Observe the Snackbar to show.
        navViewModel.onSnackBarEvent().observe(this, new Observer<Event<SnackBarContent>>() {
            @Override
            public void onChanged(Event<SnackBarContent> snackBarContentEvent) {
                SnackBarContent snackBarContent = snackBarContentEvent.getContentIfNotHandled();
                if(snackBarContent != null){
                    showSnackBar(snackBarContent);
                }
            }
        });

        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnNavigatedListener(this);
        NavigationUI.setupActionBarWithNavController(this, navController);
        /*
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navViewModel.dispatchOnFabClick(view);
            }
        });
        */
    }

    @Override
    public void onNavigated(@NonNull NavController controller, @NonNull NavDestination destination) {
        NavChromeSettings navChromeSettings =
                NavChromeSettings.fromBundle(destination.getDefaultArguments(), this);
        applyNavChromeSettings(navChromeSettings);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }

    private void applyNavChromeSettings(NavChromeSettings navChromeSettings){
        int fabImgRes = navChromeSettings.getFabImgRes();
        if(fabImgRes != 0){
            binding.fab.setImageResource(fabImgRes);
            if(binding.fab.isOrWillBeHidden()){
                binding.fab.show();
            } else {
                // As per the Material Design guidelines, the FAB disappears and reappears to show
                // that the FAB is not part of the underlying (Fragment) content.
                binding.fab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                    @Override
                    public void onHidden(FloatingActionButton fab) {
                        super.onHidden(fab);
                        binding.fab.show();

                    }
                });
            }
        } else {
            binding.fab.hide();
        }
    }

    private void showSnackBar(SnackBarContent snackBarContent){
        Snackbar snackbar =
                Snackbar.make(binding.fab, snackBarContent.getMessage(), snackBarContent.getDuration());
        View.OnClickListener listener = snackBarContent.getActionListener();
        String actionText = snackBarContent.getActionText();
        if(!TextUtils.isEmpty(actionText) && listener != null){
            snackbar.setAction(actionText, listener);
        }
        snackbar.show();
    }

}
