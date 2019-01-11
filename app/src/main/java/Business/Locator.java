package Business;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Locator {
    private static SharedPreferences sharedPreferences = null;

    public static SharedPreferences getPreferences(Context context){
        if (sharedPreferences == null){
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sharedPreferences;
    }
}
