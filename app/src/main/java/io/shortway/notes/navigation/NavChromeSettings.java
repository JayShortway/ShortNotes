package io.shortway.notes.navigation;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import io.shortway.notes.BuildConfig;

/**
 * This class holds the settings that a {@link androidx.navigation.NavDestination NavDestination}
 * wants to have applied to the app's navigation chrome, e.g. the {@code FAB}'s icon.
 * <br/><br/>
 * These settings should be defined in the navigation graph XML file, under the respective
 * destination. Currently, this class only supports {@code fabImgRes}.
 * Example:
 * <pre>
 *     &#60;fragment
 *          ... &#62;
 *          &#60;argument
 *              android:name="fabImgRes"
 *              android:defaultValue="@drawable/ic_add_white_24dp" /&#62;
 *      &#60;/fragment&#62;
 * </pre>
 * Instantiate this class by calling {@link #fromBundle(Bundle, Context)}, with the Bundle from
 * {@link NavDestination#getDefaultArguments() getDefaultArguments()}, inside
 * {@link androidx.navigation.NavController.OnNavigatedListener#onNavigated(NavController, NavDestination)
 * onNavigated()}.
 * <br/><br/>
 * (This class is basically a reskin of whatever the Safe Args plugin generates, such as
 * {@link NoteDetailsFragmentArgs}).
 */
public class NavChromeSettings {
    private int fabImgRes = 0;

    private NavChromeSettings() {
    }

    /**
     * Instantiate this class from a Bundle of
     * {@link NavDestination#getDefaultArguments() default arguments}.
     * @param bundle The Bundle from {@link NavDestination#getDefaultArguments()}.
     * @param context An Android Context.
     *
     * @return A new instance of this class.
     */
    @NonNull
    public static NavChromeSettings fromBundle(Bundle bundle, Context context) {
        NavChromeSettings result = new NavChromeSettings();
        bundle.setClassLoader(NavChromeSettings.class.getClassLoader());
        if (bundle.containsKey("fabImgRes")) {
            // When we reference a resource in the navigation graph, it is saved to the Bundle as
            // a String. So, we should translate that to an identifier we can use.
            String fabImgResString = bundle.getString("fabImgRes");
            result.fabImgRes = getResourceIdentifier(context, fabImgResString);
        }
        return result;
    }

    /**
     * The resource that should be set as the
     * {@link com.google.android.material.floatingactionbutton.FloatingActionButton FAB}'s image.
     * @return An identifier for a drawable resource.
     */
    @DrawableRes
    public int getFabImgRes() {
        return fabImgRes;
    }

    @NonNull
    public Bundle toBundle() {
        Bundle __outBundle = new Bundle();
        __outBundle.putInt("fabImgRes", this.fabImgRes);
        return __outBundle;
    }

    //

    /**
     * Get a resource identifier from a fully qualified resource name.
     * <br/><br/>
     * TODO come up with a more efficient way to set resources for the NavChrome.
     * @param context An Android Context.
     * @param resString A fully qualified resource name in the form {@code package:type/entry}.
     * @return The resource identifier.
     */
    private static int getResourceIdentifier(@Nullable Context context, String resString){
        if(context != null){
            int endIndex = resString.lastIndexOf(".");
            int middleIndex = resString.lastIndexOf("/", endIndex);
            int beginIndex = resString.lastIndexOf("/", middleIndex - 1);
            if(beginIndex > -1 && middleIndex > -1 && endIndex > -1){
                String type = resString.substring(beginIndex + 1, middleIndex);
                // When type is 'drawable-anydpi-v21', we only need 'drawable'.
                int dashIndex = type.indexOf("-");
                type = dashIndex > -1 ? type.substring(0, dashIndex) : type;
                String name = resString.substring(middleIndex + 1, endIndex);
                return context.getResources().getIdentifier(name, type, BuildConfig.APPLICATION_ID);
            } else {
                throw new RuntimeException("Resource string could not be parsed: " + resString);
            }
        } else {
            throw new RuntimeException(
                    "Context is null, but is needed to get a resource identifier for " + resString);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        NavChromeSettings that = (NavChromeSettings) object;
        if (fabImgRes != that.fabImgRes) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + fabImgRes;
        return result;
    }

    @Override
    public String toString() {
        return "NavChromeSettings{"
                + "fabImgRes=" + fabImgRes
                + "}";
    }

    public static class Builder {
        private int fabImgRes = io.shortway.notes.R.drawable.ic_edit_white_24dp;

        public Builder(NavChromeSettings original) {
            this.fabImgRes = original.fabImgRes;
        }

        public Builder() {
        }

        @NonNull
        public NavChromeSettings build() {
            NavChromeSettings result = new NavChromeSettings();
            result.fabImgRes = this.fabImgRes;
            return result;
        }

        @NonNull
        public Builder setFabImgRes(int fabImgRes) {
            this.fabImgRes = fabImgRes;
            return this;
        }

        public int getFabImgRes() {
            return fabImgRes;
        }
    }
}
