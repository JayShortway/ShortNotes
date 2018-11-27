package io.shortway.notes.domain.model;

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 * (<a href="https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150">
 *     source</a>)
 */
public class Event<T> {

    private T content;
    private boolean hasBeenHandled = false;

    public Event(T content) {
        this.content = content;
    }


    /**
     * Returns the content and prevents its use again.
     */
    public T getContentIfNotHandled() {
        if (hasBeenHandled) {
            return null;
        } else {
            hasBeenHandled = true;
            return content;
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    T peekContent(){
        return content;
    }

}
