package io.shortway.notes.navigation;

import android.view.View;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import io.shortway.notes.domain.model.Event;
import io.shortway.notes.ui.SnackBarContent;

/**
 * This ViewModel can be used for two-way communication between Fragments and between a Fragment
 * and the {@link NavActivity}.
 */
public class NavViewModel extends ViewModel {

    private MutableLiveData<String> titleLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> fabClickEvent = new MutableLiveData<>();
    private MutableLiveData<Event<SnackBarContent>> snackBarEvent = new MutableLiveData<>();

    /**
     * Set the title to be displayed in the ActionBar
     * ({@link androidx.appcompat.widget.Toolbar Toolbar}).
     * @param title The title to be displayed.
     */
    public void setTitle(String title){
        titleLiveData.setValue(title);
    }

    /**
     * {@link LiveData#observe(LifecycleOwner, Observer) Observe} this from the owner of the
     * ActionBar ({@link androidx.appcompat.widget.Toolbar Toolbar}). The
     * {@link androidx.lifecycle.Observer Observer} should make sure the ActionBar's title is
     * updated in its {@link androidx.lifecycle.Observer#onChanged(Object) onChanged(String)}
     * method.
     * @return A LiveData object holding the title to be displayed.
     */
    public LiveData<String> getTitle(){
        return titleLiveData;
    }

    /**
     * Call this from the owner of the
     * {@link com.google.android.material.floatingactionbutton.FloatingActionButton FAB} whenever it
     * is clicked.
     * @param view The View that is clicked. This argument exists so this method can be called
     *             by the DataBinding library directly, when defined in the layout XML.
     */
    public void dispatchOnFabClick(View view){
        fabClickEvent.setValue(new Event<Boolean>(true));
    }

    /**
     * {@link LiveData#observe(LifecycleOwner, Observer) Observe} this from any class that wants to
     * respond to {@link com.google.android.material.floatingactionbutton.FloatingActionButton FAB}
     * clicks.
     * @return A LiveData holding a Boolean that signifies whether the FAB was clicked (true) or
     * not (false).
     * <br/><br/>
     * (It can emit false when, for example, this LiveData is replayed following a
     * configuration change such as a device rotation. Generally, you shouldn't do anything in that
     * case.)
     */
    public LiveData<Boolean> onFabClick(){
        // We can transform the Event here, so our observers don't all have to check whether the
        // Event content is null. They can just check the Boolean value.
        return Transformations.map(fabClickEvent, new Function<Event<Boolean>, Boolean>() {
            @Override
            public Boolean apply(Event<Boolean> fabClick) {
                Boolean fabClicked = fabClick.getContentIfNotHandled();
                return fabClicked != null && fabClicked;
            }
        });
    }


    public void showSnackBar(SnackBarContent snackBarContent){
        snackBarEvent.setValue(new Event<SnackBarContent>(snackBarContent));
    }

    public LiveData<Event<SnackBarContent>> onSnackBarEvent(){
        return snackBarEvent;
    }


}
