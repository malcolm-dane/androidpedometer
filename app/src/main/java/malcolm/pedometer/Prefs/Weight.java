

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
public class Weight extends EditMeasurments {

    public Weight(Context context) {
        super(context);
    }

    public Weight(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public Weight(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    protected void initPreferenceDetails() {
        mTitleResource = R.string.body_weight_setting_title;
        mMetricUnitsResource = R.string.kilograms;
        mImperialUnitsResource = R.string.pounds;
    }
}

