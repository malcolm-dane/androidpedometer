

package malcolm.pedometer;

import java.util.ArrayList;

/**
 * The steps our user takes a minutes, tells pace. NOTE: I haven't ran tests to see if my soundpool interfers with this
 * If you find that annoying, or don't like it, just disable the class file. I plan to make it play musical notes on a scale
 * And put that data into a SQLIte DB so the user can see their dynamically generated "songs"
 *
 *
 * @author Malcolm Dane
 */
public class BroadcastPace implements StepListener, SpeakingController.Listener {

    public interface Listener {
        public void paceChanged(int value);

        public void passValue();
    }

    private ArrayList<Listener> mListeners = new ArrayList<Listener>();

    int mCounter = 0;

    private long mLastStepTime = 0;
    private long[] mLastStepDeltas = {-1, -1, -1, -1};
    private int mLastStepDeltasIndex = 0;
    private long mPace = 0;

    PSettings mSettings;
    Helpers mHelpers;

    /**
     * Pace as set by user.
     */
    int mDesiredPace;

    /**
     * I speak?
     */
    boolean mShouldTellFasterslower;

    /**
     * BAsed upon the last time spoken
     */
    private long mSpokenAt = 0;

    public BroadcastPace(PSettings settings, Helpers helpers) {
        mHelpers = helpers;
        mSettings = settings;
        mDesiredPace = mSettings.getDesiredPace();
        reloadSettings();
    }

    public void setPace(int pace) {
        mPace = pace;
        int avg = (int) (60 * 1000.0 / mPace);
        for (int i = 0; i < mLastStepDeltas.length; i++) {
            mLastStepDeltas[i] = avg;
        }
        notifyListener();
    }

    public void reloadSettings() {
        mShouldTellFasterslower =
                mSettings.shouldTellFasterslower()
                        && mSettings.getMaintainOption() == PSettings.M_PACE;
        notifyListener();
    }

    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void setDesiredPace(int desiredPace) {
        mDesiredPace = desiredPace;
    }

    public void onStep() {
        long thisStepTime = System.currentTimeMillis();
        mCounter++;

        // Calculate pace based on last x steps
        if (mLastStepTime > 0) {
            long delta = thisStepTime - mLastStepTime;

            mLastStepDeltas[mLastStepDeltasIndex] = delta;
            mLastStepDeltasIndex = (mLastStepDeltasIndex + 1) % mLastStepDeltas.length;

            long sum = 0;
            boolean isMeaningfull = true;
            for (int i = 0; i < mLastStepDeltas.length; i++) {
                if (mLastStepDeltas[i] < 0) {
                    isMeaningfull = false;
                    break;
                }
                sum += mLastStepDeltas[i];
            }
            if (isMeaningfull && sum > 0) {
                long avg = sum / mLastStepDeltas.length;
                mPace = 60 * 1000 / avg;

                // TODO: Duped from earlier build, exists in the notifier.
                if (mShouldTellFasterslower && !mHelpers.isSpeakingEnabled()) {
                    if (thisStepTime - mSpokenAt > 3000 && !mHelpers.isSpeakingNow()) {
                        float little = 0.10f;
                        float normal = 0.30f;
                        float much = 0.50f;

                        boolean spoken = true;
                        if (mPace < mDesiredPace * (1 - much)) {
                            mHelpers.say("You need to go much faster!");
                        } else if (mPace > mDesiredPace * (1 + much)) {
                            mHelpers.say("Too fast!!");
                        } else if (mPace < mDesiredPace * (1 - normal)) {
                            mHelpers.say("You can do it go faster!");
                        } else if (mPace > mDesiredPace * (1 + normal)) {
                            mHelpers.say("Pace yourself!");
                        } else if (mPace < mDesiredPace * (1 - little)) {
                            mHelpers.say("I think you can Go a bit faster!");
                        } else if (mPace > mDesiredPace * (1 + little)) {
                            mHelpers.say("You need to go a bit slower!");
                        } else {
                            spoken = false;
                        }
                        if (spoken) {
                            mSpokenAt = thisStepTime;
                        }
                    }
                }
            } else {
                mPace = -1;
            }
        }
        mLastStepTime = thisStepTime;
        notifyListener();
    }

    private void notifyListener() {
        for (Listener listener : mListeners) {
            listener.paceChanged((int) mPace);
        }
    }

    public void passValue() {
        // Not used
    }

    //-----------------------------------------------------
    // Speaking

    public void speak() {
        if (mSettings.shouldTellPace()) {
            if (mPace > 0) {
                mHelpers.say(mPace + " That is your steps per minute");
            }
        }
    }


}

