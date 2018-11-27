package io.shortway.notes.navigation;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import io.shortway.notes.R;

/**
 * A base Fragment class to supply convenience methods to all Fragments in our app.
 */
abstract class BaseFragment extends Fragment {

    /**
     * Convenience method for easy usage of
     * {@link androidx.navigation.NavController#navigate(int) NavController#navigate(int)}, of which
     * the documentation is stated below.
     * <br/><br/>
     * Navigate to a destination from the current navigation graph. This supports both navigating
     * via an {@link NavDestination#getAction(int) action} and directly navigating to a destination.
     *
     * @param resId an {@link NavDestination#getAction(int) action} id or a destination id to
     *              navigate to
     */
    protected void navigate(@IdRes int resId){
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(resId);
    }

    /**
     * Convenience method for easy usage of
     * {@link NavController#popBackStack()}, of which
     * the documentation is stated below.
     * <br/><br/>
     * Attempts to pop the controller's back stack. Analogous to when the user presses
     * the system {@link android.view.KeyEvent#KEYCODE_BACK Back} button when the associated
     * navigation host has focus.
     *
     * @return true if the stack was popped, false otherwise
     */
    protected boolean popBackStack(){
        return Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .popBackStack();
    }

}
