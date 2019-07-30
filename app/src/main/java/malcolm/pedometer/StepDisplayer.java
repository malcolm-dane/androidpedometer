

package malcolm.pedometer;

import java.util.ArrayList;

/**
 * Counts steps provided by StepDetector and passes the current
 * step count to the activity.
 */
public class StepDisplayer implements StepListener, SpeakingController.Listener {

    private int mCount = 0;
    PSettings mSettings;
    Helpers mHelpers;

    public StepDisplayer(PSettings settings, Helpers helpers) {
        mHelpers = helpers;
        mSettings = settings;
        notifyListener();
    }

    public void setUtils(Helpers helpers) {
        mHelpers = helpers;
    }

    public void setSteps(int steps) {
        mCount = steps;
        notifyListener();
    }

    public void onStep() {
        mCount++;
        notifyListener();
    }

    public void reloadSettings() {
        notifyListener();
    }

    public void passValue() {
    }


    //-----------------------------------------------------
    // Listener

    public interface Listener {
        public void stepsChanged(int value);

        public void passValue();
    }

    private ArrayList<Listener> mListeners = new ArrayList<Listener>();

    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void notifyListener() {
        for (Listener listener : mListeners) {
            listener.stepsChanged((int) mCount);
        }
    }

    //-----------------------------------------------------
    // Speaking

    public void speak() {
        if (mSettings.shouldTellSteps()) {
            if (mCount > 0) {
                mHelpers.say("" + mCount + " steps");
            }
        }
    }


}
