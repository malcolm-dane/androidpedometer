package malcolm.pedometer;

import android.content.Context;
import android.media.SoundPool;
import malcolm.pedometer.StepListener;
/*
* New Feature that I hope to add sometime soon.
* It plays a noise on a sensor disturbance basically, but I hope to load mutliple samples
* and play a random sample on a stepdown. To construct a "song" so to speak when activity is heavy.
* Then store this in a SQLite DB.
* In the mean time, Goto PServ and comment out lines 128 and 129 to disable.
*
*
* */
public class StepNote implements StepListener {
    private Context mContext;
    private SoundPool mSoundPool;
    int piano;

    public StepNote(Context var1) {
        this.mContext = var1;
        this.mSoundPool = new SoundPool(1, 3, 0);
        this.mContext.getSystemService(Context.AUDIO_SERVICE);
        this.piano = this.mSoundPool.load(this.mContext, 2131034112, 0);
    }

    public void onStep() {
        this.mSoundPool.play(this.piano, 1, 1, 1, 1, 1);
    }

    public void passValue() {
    }
}

