

package malcolm.pedometer;

/**
 * Interface implemented by classes that can handle notifications about steps.
 * These classes can be passed to StepDetector.
 *
 * @author Malcolm Dane
 */
public interface StepListener {
    public void onStep();

    public void passValue();
}

