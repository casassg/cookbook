package edu.upc.fib.idi.idireceptes.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by casassg on 31/12/15.
 *
 * @author casassg
 */
public final class Permissions {

    public static final int PERMISSION_REQUES_WRITE_EXTERNAL = 42;


    public static void checkWritePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUES_WRITE_EXTERNAL
            );
        }
    }
}
