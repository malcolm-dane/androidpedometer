

package malcolm.pedometer;


/**
 * Calculates and displays the distance walked.
 *
 * @author Malcolm Dane
 */
public class BroadcastNotifier implements StepListener, SpeakingController.Listener {

    public interface Listener {
        public void valueChanged(float value);

        public void passValue();
    }

    private Listener mListener;

    float mDistance = 0;

    PSettings mSettings;
    Helpers mHelpers;

    boolean mIsMetric;
    float mStepLength;

    public BroadcastNotifier(Listener listener, PSettings settings, Helpers helpers) {
        mListener = listener;
        mHelpers = helpers;
        mSettings = settings;
        reloadSettings();
    }

    public void setDistance(float distance) {
        mDistance = distance;
        notifyListener();
    }

    public void reloadSettings() {
        mIsMetric = mSettings.isMetric();
        mStepLength = mSettings.getStepLength();
        notifyListener();
    }

    public void onStep() {

        if (mIsMetric) {
            mDistance += (float) (// kilometers
                    mStepLength // centimeters
                            / 100000.0); // centimeters/kilometer
        } else {
            mDistance += (float) (// miles
                    mStepLength // inches
                            / 63360.0); // inches/mile
        }

        notifyListener();
    }

    private void notifyListener() {
        mListener.valueChanged(mDistance);
    }

    public void passValue() {
        // Callback of StepListener - Not implemented
    }

    public void speak() {
        if (mSettings.shouldTellDistance()) {
            if (mDistance >= .001f) {
                mHelpers.say(("" + (mDistance + 0.000001f)).substring(0, 4) + (mIsMetric ? " kilometers" : " miles"));
                // TODO: format numbers (no "." at the end)
            }
        }
    }


}

