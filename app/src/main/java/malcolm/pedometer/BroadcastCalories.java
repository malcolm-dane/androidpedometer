

package malcolm.pedometer;


/**
 * Calculates and displays the approximate calories.
 *
 * @author Malcolm Dane
 */
public class BroadcastCalories implements StepListener, SpeakingController.Listener {

    public interface Listener {
        public void valueChanged(float value);

        public void passValue();
    }

    private Listener mListener;

    private static double METRIC_RUNNING_FACTOR = 1.02784823;
    private static double IMPERIAL_RUNNING_FACTOR = 0.75031498;

    private static double METRIC_WALKING_FACTOR = 0.708;
    private static double IMPERIAL_WALKING_FACTOR = 0.517;

    private double mCalories = 0;

    PSettings mSettings;
    Helpers mHelpers;

    boolean mIsMetric;
    boolean mIsRunning;
    float mStepLength;
    float mBodyWeight;

    public BroadcastCalories(Listener listener, PSettings settings, Helpers helpers) {
        mListener = listener;
        mHelpers = helpers;
        mSettings = settings;
        reloadSettings();
    }

    public void setCalories(float calories) {
        mCalories = calories;
        notifyListener();
    }

    public void reloadSettings() {
        mIsMetric = mSettings.isMetric();
        mIsRunning = mSettings.isRunning();
        mStepLength = mSettings.getStepLength();
        mBodyWeight = mSettings.getWeight();
        notifyListener();
    }

    public void resetValues() {
        mCalories = 0;
    }

    public void isMetric(boolean isMetric) {
        mIsMetric = isMetric;
    }

    public void setStepLength(float stepLength) {
        mStepLength = stepLength;
    }

    public void onStep() {

        if (mIsMetric) {
            mCalories +=
                    (mBodyWeight * (mIsRunning ? METRIC_RUNNING_FACTOR : METRIC_WALKING_FACTOR))
                            // Distance:
                            * mStepLength // centimeters
                            / 100000.0; // centimeters/kilometer
        } else {
            mCalories +=
                    (mBodyWeight * (mIsRunning ? IMPERIAL_RUNNING_FACTOR : IMPERIAL_WALKING_FACTOR))
                            // Distance:
                            * mStepLength // inches
                            / 63360.0; // inches/mile
        }

        notifyListener();
    }

    private void notifyListener() {
        mListener.valueChanged((float) mCalories);
    }
    //Java's annoying intefaces and polymorphism.
    public void passValue() {

    }

    //Should we tell our user the calories burned?
    public void speak() {
        if (mSettings.shouldTellCalories()) {
            if (mCalories > 0) {
                mHelpers.say("" + (int) mCalories + " calories burned");
                mHelpers.say("burn more");
            }
        }

    }


}

