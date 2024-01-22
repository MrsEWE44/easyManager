package android.app;

public class AppOpsManager {

    public static final int MODE_ALLOWED = 0;
    public static final int MODE_IGNORED = 1;
    public static final int MODE_ERRORED = 2;
    public static final int MODE_DEFAULT = 3;
    public static final int MODE_FOREGROUND = 4;
    public static final int WATCH_FOREGROUND_CHANGES = 1 << 0;



    /** Access to coarse location information. */
    public static final String OPSTR_COARSE_LOCATION = "android:coarse_location";
    /** Access to fine location information. */
    public static final String OPSTR_FINE_LOCATION =
            "android:fine_location";
    /** Continually monitoring location data. */
    public static final String OPSTR_MONITOR_LOCATION
            = "android:monitor_location";
    /** Continually monitoring location data with a relatively high power request. */
    public static final String OPSTR_MONITOR_HIGH_POWER_LOCATION
            = "android:monitor_location_high_power";
    /** Access to {@link android.app.usage.UsageStatsManager}. */
    public static final String OPSTR_GET_USAGE_STATS
            = "android:get_usage_stats";
    /** Activate a VPN connection without user intervention. @hide */

    public static final String OPSTR_ACTIVATE_VPN
            = "android:activate_vpn";
    /** Allows an application to read the user's contacts data. */
    public static final String OPSTR_READ_CONTACTS
            = "android:read_contacts";
    /** Allows an application to write to the user's contacts data. */
    public static final String OPSTR_WRITE_CONTACTS
            = "android:write_contacts";
    /** Allows an application to read the user's call log. */
    public static final String OPSTR_READ_CALL_LOG
            = "android:read_call_log";
    /** Allows an application to write to the user's call log. */
    public static final String OPSTR_WRITE_CALL_LOG
            = "android:write_call_log";
    /** Allows an application to read the user's calendar data. */
    public static final String OPSTR_READ_CALENDAR
            = "android:read_calendar";
    /** Allows an application to write to the user's calendar data. */
    public static final String OPSTR_WRITE_CALENDAR
            = "android:write_calendar";
    /** Allows an application to initiate a phone call. */
    public static final String OPSTR_CALL_PHONE
            = "android:call_phone";
    /** Allows an application to read SMS messages. */
    public static final String OPSTR_READ_SMS
            = "android:read_sms";
    /** Allows an application to receive SMS messages. */
    public static final String OPSTR_RECEIVE_SMS
            = "android:receive_sms";
    /** Allows an application to receive MMS messages. */
    public static final String OPSTR_RECEIVE_MMS
            = "android:receive_mms";
    /** Allows an application to receive WAP push messages. */
    public static final String OPSTR_RECEIVE_WAP_PUSH
            = "android:receive_wap_push";
    /** Allows an application to send SMS messages. */
    public static final String OPSTR_SEND_SMS
            = "android:send_sms";
    /** Required to be able to access the camera device. */
    public static final String OPSTR_CAMERA
            = "android:camera";
    /** Required to be able to access the microphone device. */
    public static final String OPSTR_RECORD_AUDIO
            = "android:record_audio";
    /** Required to access phone state related information. */
    public static final String OPSTR_READ_PHONE_STATE
            = "android:read_phone_state";
    /** Required to access phone state related information. */
    public static final String OPSTR_ADD_VOICEMAIL
            = "android:add_voicemail";
    /** Access APIs for SIP calling over VOIP or WiFi */
    public static final String OPSTR_USE_SIP
            = "android:use_sip";
    /** Access APIs for diverting outgoing calls */
    public static final String OPSTR_PROCESS_OUTGOING_CALLS
            = "android:process_outgoing_calls";
    /** Use the fingerprint API. */
    public static final String OPSTR_USE_FINGERPRINT
            = "android:use_fingerprint";
    /** Access to body sensors such as heart rate, etc. */
    public static final String OPSTR_BODY_SENSORS
            = "android:body_sensors";
    /** Read previously received cell broadcast messages. */
    public static final String OPSTR_READ_CELL_BROADCASTS
            = "android:read_cell_broadcasts";
    /** Inject mock location into the system. */
    public static final String OPSTR_MOCK_LOCATION
            = "android:mock_location";
    /** Read external storage. */
    public static final String OPSTR_READ_EXTERNAL_STORAGE
            = "android:read_external_storage";
    /** Write external storage. */
    public static final String OPSTR_WRITE_EXTERNAL_STORAGE
            = "android:write_external_storage";
    /** Required to draw on top of other apps. */
    public static final String OPSTR_SYSTEM_ALERT_WINDOW
            = "android:system_alert_window";
    /** Required to write/modify/update system settingss. */
    public static final String OPSTR_WRITE_SETTINGS
            = "android:write_settings";

    public static final String OPSTR_GET_ACCOUNTS
            = "android:get_accounts";
    public static final String OPSTR_READ_PHONE_NUMBERS
            = "android:read_phone_numbers";
    /** Access to picture-in-picture. */
    public static final String OPSTR_PICTURE_IN_PICTURE
            = "android:picture_in_picture";

    public static final String OPSTR_INSTANT_APP_START_FOREGROUND
            = "android:instant_app_start_foreground";
    /** Answer incoming phone calls */
    public static final String OPSTR_ANSWER_PHONE_CALLS
            = "android:answer_phone_calls";

    public static final String OPSTR_ACCEPT_HANDOVER
            = "android:accept_handover";

    public static final String OPSTR_GPS = "android:gps";

    public static final String OPSTR_VIBRATE = "android:vibrate";

    public static final String OPSTR_WIFI_SCAN = "android:wifi_scan";

    public static final String OPSTR_POST_NOTIFICATION = "android:post_notification";

    public static final String OPSTR_NEIGHBORING_CELLS = "android:neighboring_cells";

    public static final String OPSTR_WRITE_SMS = "android:write_sms";

    public static final String OPSTR_RECEIVE_EMERGENCY_BROADCAST =
            "android:receive_emergency_broadcast";

    public static final String OPSTR_READ_ICC_SMS = "android:read_icc_sms";

    public static final String OPSTR_WRITE_ICC_SMS = "android:write_icc_sms";

    public static final String OPSTR_ACCESS_NOTIFICATIONS = "android:access_notifications";

    public static final String OPSTR_PLAY_AUDIO = "android:play_audio";

    public static final String OPSTR_READ_CLIPBOARD = "android:read_clipboard";

    public static final String OPSTR_WRITE_CLIPBOARD = "android:write_clipboard";

    public static final String OPSTR_TAKE_MEDIA_BUTTONS = "android:take_media_buttons";

    public static final String OPSTR_TAKE_AUDIO_FOCUS = "android:take_audio_focus";

    public static final String OPSTR_AUDIO_MASTER_VOLUME = "android:audio_master_volume";

    public static final String OPSTR_AUDIO_VOICE_VOLUME = "android:audio_voice_volume";

    public static final String OPSTR_AUDIO_RING_VOLUME = "android:audio_ring_volume";

    public static final String OPSTR_AUDIO_MEDIA_VOLUME = "android:audio_media_volume";

    public static final String OPSTR_AUDIO_ALARM_VOLUME = "android:audio_alarm_volume";

    public static final String OPSTR_AUDIO_NOTIFICATION_VOLUME =
            "android:audio_notification_volume";

    public static final String OPSTR_AUDIO_BLUETOOTH_VOLUME = "android:audio_bluetooth_volume";

    public static final String OPSTR_WAKE_LOCK = "android:wake_lock";

    public static final String OPSTR_MUTE_MICROPHONE = "android:mute_microphone";

    public static final String OPSTR_TOAST_WINDOW = "android:toast_window";

    public static final String OPSTR_PROJECT_MEDIA = "android:project_media";

    public static final String OPSTR_WRITE_WALLPAPER = "android:write_wallpaper";

    public static final String OPSTR_ASSIST_STRUCTURE = "android:assist_structure";

    public static final String OPSTR_ASSIST_SCREENSHOT = "android:assist_screenshot";

    public static final String OPSTR_TURN_SCREEN_ON = "android:turn_screen_on";

    public static final String OPSTR_RUN_IN_BACKGROUND = "android:run_in_background";

    public static final String OPSTR_AUDIO_ACCESSIBILITY_VOLUME =
            "android:audio_accessibility_volume";

    public static final String OPSTR_REQUEST_INSTALL_PACKAGES = "android:request_install_packages";

    public static final String OPSTR_RUN_ANY_IN_BACKGROUND = "android:run_any_in_background";

    public static final String OPSTR_CHANGE_WIFI_STATE = "android:change_wifi_state";

    public static final String OPSTR_REQUEST_DELETE_PACKAGES = "android:request_delete_packages";

    public static final String OPSTR_BIND_ACCESSIBILITY_SERVICE =
            "android:bind_accessibility_service";

    public static final String OPSTR_MANAGE_IPSEC_TUNNELS = "android:manage_ipsec_tunnels";

    public static final String OPSTR_START_FOREGROUND = "android:start_foreground";

    public static final String OPSTR_BLUETOOTH_SCAN = "android:bluetooth_scan";


    public static final String OPSTR_USE_BIOMETRIC = "android:use_biometric";


    public static final String OPSTR_ACTIVITY_RECOGNITION = "android:activity_recognition";

    public static final String OPSTR_SMS_FINANCIAL_TRANSACTIONS =
            "android:sms_financial_transactions";

    public static final String OPSTR_READ_MEDIA_AUDIO = "android:read_media_audio";

    public static final String OPSTR_WRITE_MEDIA_AUDIO = "android:write_media_audio";

    public static final String OPSTR_READ_MEDIA_VIDEO = "android:read_media_video";

    public static final String OPSTR_WRITE_MEDIA_VIDEO = "android:write_media_video";

    public static final String OPSTR_READ_MEDIA_IMAGES = "android:read_media_images";

    public static final String OPSTR_WRITE_MEDIA_IMAGES = "android:write_media_images";

    public static final String OPSTR_LEGACY_STORAGE = "android:legacy_storage";


    public static final String OPSTR_ACCESS_ACCESSIBILITY = "android:access_accessibility";

    public static final String OPSTR_READ_DEVICE_IDENTIFIERS = "android:read_device_identifiers";

    public static int strOpToOp(String op) {
        return -1;
    }
    public static int permissionToOpCode(String permission){
        return -1;
    };

    public static String opToPermission(int op){
        return null;
    }

    public static String permissionToOp(String permission) {
        return null;
    }

    public int unsafeCheckOpNoThrow( String op, int uid, String packageName) {
        return -1;
    }

    public int checkOpNoThrow( String op, int uid, String packageName) {
        return -1;
    }

    public int unsafeCheckOp(String op, int uid, String packageName){
        return  -1;
    }

    public int checkOp( String op, int uid,  String packageName){
        return -1;
    }

}
