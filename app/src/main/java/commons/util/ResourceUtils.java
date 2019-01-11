package commons.util;

import android.content.Context;

public class ResourceUtils {
    public static String getStringFrom(Context context, int resourceId){
        return context.getResources().getString(resourceId);
    }
}
