import java.util.*;

/**
 * Abstract Subject class for the Observer design pattern
 * Manages a list of observers and provides methods to notify them
 */
public abstract class Subject {
    private List<Observer> observers = new ArrayList<>();

    /**
     * Attach an observer to the subject
     * 
     * @param observer The observer to attach
     */
    public void attach(Observer observer) {
        observers.add(observer);
    }

    /**
     * Detach an observer from the subject
     * 
     * @param observer The observer to detach
     */
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notify all attached observers of an event
     * 
     * @param eventType The type of event that occurred
     * @param data Additional data about the event
     */
    protected void notifyObservers(String eventType, Map<String, Object> data) {
        for (Observer observer : observers) {
            observer.update(eventType, data);
        }
    }
}