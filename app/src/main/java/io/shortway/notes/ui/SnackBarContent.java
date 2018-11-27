package io.shortway.notes.ui;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A data holder class to hold the contents for a SnackBar message. Can be used by a Fragment to
 * let the Activity know what kind of SnackBar it should display.
 */
public class SnackBarContent {

    private String message;
    private int duration;
    private String actionText;
    private View.OnClickListener actionListener;

    /**
     * Construct a SnackBarContent with an action.
     * @param message The message to be displayed in a Snackbar.
     * @param duration The duration in seconds the Snackbar should be displayed.
     * @param actionText The text of the action button.
     * @param actionListener The listener to call when the action is clicked.
     */
    public SnackBarContent(@NonNull String message, int duration, @Nullable String actionText,
                           @Nullable View.OnClickListener actionListener) {
        this.message = message;
        this.duration = duration;
        this.actionText = actionText;
        this.actionListener = actionListener;
    }

    /**
     * Construct a SnackBarContent without an action.
     * @param message The message to be displayed in a Snackbar.
     * @param duration The duration in seconds the Snackbar should be displayed.
     */
    public SnackBarContent(@NonNull String message, int duration) {
        this.message = message;
        this.duration = duration;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    public int getDuration(){
        return duration;
    }

    @Nullable
    public String getActionText() {
        return actionText;
    }

    @Nullable
    public View.OnClickListener getActionListener() {
        return actionListener;
    }
}
