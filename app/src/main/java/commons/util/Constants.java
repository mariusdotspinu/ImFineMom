package commons.util;

import android.Manifest;

public abstract class Constants {
    public static final byte MAX_SIZE = 15;

    public static final byte NOTIFICATION_IDENTIFIER = 1;
    public static final byte SMS_SENT_NOTIFICATION_ID = 2;
    public static final byte CONTACT_REQ_CODE = 1;
    public static final byte SETTINGS_REQ_CODE = 2;
    public static final short DEFAULT_MILLIS_FUTURE = 1000;

    public static final String SETTINGS_CHANGED = "1";
    public static final String DEFAULT_TIMER_VALUE = "30";

    public static final String NOTIFICATION_CHANNEL = "NOTIFICATION_CHANNEL";
    public static final String DATE_GENERATION_PATTERN = "ddHHmmssSS";
    public static final String DEFAULT_SMS_APP = "sms_default_application";
    public static final String GET_ALL_CONTACTS = "MainActivity";
    public static final String ADD_CONTACT = "HierarchicalUri";
    public static final String DELETE_CONTACT = "DeleteContactCallback";
    public static final String UPDATE_CONTACT = "CheckChangeListener";

    public static final String GET = "GET";
    public static final String ADD = "ADD";
    public static final String DELETE = "DELETE";
    public static final String UPDATE = "UPDATE";

    public static final String KEY_GET = "contact$get";
    public static final String KEY_ADD = "contact$add";
    public static final String KEY_DELETE = "contact$delete";
    public static final String KEY_UPDATE = "contact$update";

    public static final String FIRST_STARTUP = "firstStartup";

    public static final String SNACKBAR_MESSAGE = "<font color=\"#ffffff\">%s</font>";

    public static String INTERVAL_PREF_KEY = "TIME_INTERVAL";

    public static final String DATABASE_NAME = "contact-database";

    public static final String[] PERMISSIONS = new String[]
            {Manifest.permission.READ_CONTACTS,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION};

}
