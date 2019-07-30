

package malcolm.pedometer;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Activity for Pedometer settings.
 * Started when the user click Settings from the main menu.Need to change the depreciated.
 *
 * @author Malcolm Dane
 */
public class Settings extends PreferenceActivity {
    /**
     * This is called when we first make the activity.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
