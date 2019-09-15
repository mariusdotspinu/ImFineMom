package Business.Services.util;

import java.util.concurrent.TimeUnit;

public abstract class Utils {
public static long getMillisFrom(String minutes){
        return TimeUnit.MINUTES.toMillis(Long.parseLong(minutes));
        }
}
