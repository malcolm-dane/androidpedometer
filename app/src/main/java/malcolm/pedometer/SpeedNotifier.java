
package malcolm.pedometer;


/**
 * Calculates and displays pace (steps / minute), handles input of othe users desired pace,
 * Then  notifies user if he/she has to go faster or slower.
 * <p>
 * Uses {@link BroadcastPace}, calculates speed as product of pace and step length.
 *
 * @author Malcolm Dane
 */
public class SpeedNotifier implements BroadcastPace.Listener, SpeakingController.Listener {

    public interface Listener {
        public void valueChanged(float value);

        public void passValue();
    }

    private Listener mListener;

    int mCounter = 0;
    float mSpeed = 0;

    boolean mIsMetric;
    float mStepLength;

    PSettings mSettings;
    Helpers mHelpers;

    /**
     * The user's speed
     */
    float mDesiredSpeed;

    /**
     * Should we speak?
     */
    boolean mShouldTellFasterslower;
    boolean mShouldTellSpeed;

    /**
     * When did we Speak last?
     */
    private long mSpokenAt = 0;

    public SpeedNotifier(Listener listener, PSettings settings, Helpers helpers) {
        mListener = listener;
        mHelpers = helpers;
        mSettings = settings;
        mDesiredSpeed = mSettings.getDesiredSpeed();
        reloadSettings();
    }

    public void setSpeed(float speed) {
        mSpeed = speed;
        notifyListener();
    }

    public void reloadSettings() {
        mIsMetric = mSettings.isMetric();
        mStepLength = mSettings.getStepLength();
        mShouldTellSpeed = mSettings.shouldTellSpeed();
        mShouldTellFasterslower =
                mSettings.shouldTellFasterslower()
                        && mSettings.getMaintainOption() == PSettings.M_SPEED;
        notifyListener();
    }

    public void setDesiredSpeed(float desiredSpeed) {
        mDesiredSpeed = desiredSpeed;
    }

    private void notifyListener() {
        mListener.valueChanged(mSpeed);
    }

    public void paceChanged(int value) {
        if (mIsMetric) {
            mSpeed = // KmPH
                    value * mStepLength // Centimers per minute
                            / 100000f * 60f; // Centimeters/kilometer
        } else {
            mSpeed = // miles / hour
                    value * mStepLength // Inches in a minute
                            / 63360f * 60f; // inches in a mile!
        }
        tellFasterSlower();
        notifyListener();
    }

    /**
     * Say slower/faster, if needed.
     */
    private void tellFasterSlower() {
        if (mShouldTellFasterslower && mHelpers.isSpeakingEnabled()) {
            long now = System.currentTimeMillis();
            if (now - mSpokenAt > 3000 && !mHelpers.isSpeakingNow()) {
                float little = 0.10f;
                float normal = 0.30f;
                float much = 0.50f;

                boolean spoken = true;
                if (mSpeed < mDesiredSpeed * (1 - much)) {
                    mHelpers.say("You need to go faster!");
                } else if (mSpeed > mDesiredSpeed * (1 + much)) {
                    mHelpers.say("Pace yourself!");
                } else if (mSpeed < mDesiredSpeed * (1 - normal)) {
                    mHelpers.say("Come on faster!");
                } else if (mSpeed > mDesiredSpeed * (1 + normal)) {
                    mHelpers.say("Slow it down");
                } else if (mSpeed < mDesiredSpeed * (1 - little)) {
                    mHelpers.say("I think you can go a little faster!");
                } else if (mSpeed > mDesiredSpeed * (1 + little)) {
                    mHelpers.say("It's ok to go a little slower!");
                } else {
                    spoken = false;
                }
                if (spoken) {
                    mSpokenAt = now;
                }
            }
        }
    }

    public void passValue() {
        // Not used
    }

    public void speak() {
        if (mSettings.shouldTellSpeed()) {
            if (mSpeed >= .01f) {
                mHelpers.say(("" + (mSpeed + 0.000001f)).substring(0, 4) + (mIsMetric ? " kilometers per hour" : " miles per hour"));
            }
        }

    }

}

