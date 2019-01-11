package UI.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

public class NotificationUtils {

    public static Notification buildStatusNotification(Context context, int iconId, String tickerText,
                                                 String contentTitle, String contentText,
                                                 PendingIntent pendingIntent, String channelId){

        return getNotificationBuilder(context, iconId, tickerText, contentTitle, contentText,
                pendingIntent, channelId).setOngoing(true).build();
    }

    public static Notification buildMessageStatusNotification(Context context, int iconId, String tickerText,
                                                              String contentTitle, String contentText,
                                                              PendingIntent pendingIntent, String channelId){
        return getNotificationBuilder(context, iconId, tickerText, contentTitle, contentText,
                pendingIntent, channelId).setOngoing(false).build();
    }

    private static NotificationCompat.Builder getNotificationBuilder(Context context, int iconId, String tickerText,
                                                                     String contentTitle, String contentText,
                                                                     PendingIntent pendingIntent, String channelId){
        return new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(iconId)
                .setTicker(tickerText)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(pendingIntent);
    }
}
