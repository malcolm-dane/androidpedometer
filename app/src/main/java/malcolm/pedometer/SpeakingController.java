

package malcolm.pedometer;

import java.util.ArrayList;

/**
 * Call all listening objects repeatedly.
 * The interval is defined by the user settings.
 *
 * @author Malcolm Dane
 */
public class SpeakingController implements StepListener {

    PSettings mSettings;
    Helpers mHelpers;
    boolean mShouldSpeak;
    float mInterval;
    long mLastSpeakTime;

    public SpeakingController(PSettings settings, Helpers helpers) {
        mLastSpeakTime = System.currentTimeMillis();
        mSettings = settings;
        mHelpers = helpers;
        reloadSettings();
    }

    public void reloadSettings() {
        mShouldSpeak = mSettings.shouldSpeak();
        mInterval = mSettings.getSpeakingInterval();
    }

    public void onStep() {
        long now = System.currentTimeMillis();
        long delta = now - mLastSpeakTime;

        if (delta / 60000.0 >= mInterval) {
            mLastSpeakTime = now;
            notifyListeners();
        }
    }

    public void passValue() {
        // not used
    }


    public interface Listener {
        public void speak();
    }

    private ArrayList<Listener> mListeners = new ArrayList<Listener>();

    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void notifyListeners() {
        mHelpers.ding();
        for (Listener listener : mListeners) {
            listener.speak();
        }
    }



    public boolean isSpeaking() {
        return mHelpers.isSpeakingNow();
    }
}

