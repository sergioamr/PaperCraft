package cordproject.lol.papercraft;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import java.util.List;

/*
 * A simple demo of receiving custom intents.
 * action1 is registered statically in the manifest file and action2 is dynamically registered 
 * in the mainActivity code.
 * 
 * The variables ACTION1 and ACTION2 are declared in the MainActivity as well.
 */

public class FlicktekClipReceiver extends BroadcastReceiver {
    public static final String ACTION_URI_SEND = "com.flicktek.clip.ACTION_URI_SEND";
    public static final String ACTION_URI_GESTURE = "com.flicktek.clip.ACTION_GESTURE";
    public static final String ACTION_URI_WAKEUP = "com.flicktek.clip.ACTION_WAKEUP";
    public static final String ACTION_URI_BACK = "com.flicktek.clip.ACTION_BACK";

    public static final String ACTION_CLIP = "com.flicktek.clip";

    public static final String EXTRA_URI = "com.flicktek.clip.EXTRA_URI";

    public static final String EXTRA_GESTURE_ENTER = "ENTER";
    public static final String EXTRA_GESTURE_HOME = "HOME";
    public static final String EXTRA_GESTURE_UP = "UP";
    public static final String EXTRA_GESTURE_DOWN = "DOWN";
    public static final String EXTRA_GESTURE_UNKNOWN = "UNKNOWN";

    public interface MyGestureListener {
        public void onGestureReceived(String gesture);
    }

    public static MyGestureListener mListener;

    public static void setCustomGestures(MyGestureListener listener) {
        mListener = listener;
    }

    public static void connectClip(Activity activity) {
        openBroadcastIntent(activity, ACTION_URI_WAKEUP, "Connect");
    }

    public static void closeApplication(Activity activity) {
        openBroadcastIntent(activity, ACTION_URI_BACK, "");
    }

    public static void openBroadcastIntent(Context context, String action, String uri) {
        context.sendBroadcast(new Intent(action).putExtra(EXTRA_URI, uri));
    }

    public static void openIntent(Activity activity, String intentString,
                                  int requestCode) {
        Intent intent = new Intent(intentString);

        // verify that the intent resolves
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;

        if (isIntentSafe) {
            activity.startActivityForResult(intent, requestCode);
        } else {
            Toast.makeText(activity, "Application was not installed...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            switch (intent.getAction()) {
                case ACTION_URI_GESTURE: {
                    String extra = new String("Empty");
                    extra = intent.getStringExtra(EXTRA_URI);
                    if (mListener != null) {
                        mListener.onGestureReceived(extra);
                    }

                    //Toast.makeText(context, "Gesture. " + extra, Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    Toast.makeText(context, "Flicktek Received " + intent.getAction(), Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (Exception e) {

        }
    }
}
