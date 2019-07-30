

package malcolm.pedometer.Prefs;

import malcolm.pedometer.R;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

/**
 * An {@link EditTextPreference} that only allows float values.
 *
 * @author Malcolm Dane
 */
public class LengthOfStepPref extends EditMeasurments {

    public LengthOfStepPref(Context context) {
        super(context);
    }

    public LengthOfStepPref(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public LengthOfStepPref(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    protected void initPreferenceDetails() {
        mTitleResource = R.string.step_length_setting_title;
        mMetricUnitsResource = R.string.centimeters;
        mImperialUnitsResource = R.string.inches;
    }
}

