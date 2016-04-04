package grupp2.satansdemocracy;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by chris on 06/05/2014.
 * For Calligraphy.
 */
public class CalligraphyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/BaronNeue.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}