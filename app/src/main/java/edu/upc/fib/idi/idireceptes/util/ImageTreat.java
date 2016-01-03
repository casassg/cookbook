package edu.upc.fib.idi.idireceptes.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import edu.upc.fib.idi.idireceptes.R;
import edu.upc.fib.idi.idireceptes.model.Recepta;

/**
 * Created by casassg on 31/12/15.
 *
 * @author casassg
 */
public final class ImageTreat {

    public static final String TAG = ImageTreat.class.getSimpleName();

    public static String getAbsolutePathImage(Recepta recepta) {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, recepta.getImageFilename());
        return image.getAbsolutePath();
    }

    public static void setImage(Recepta recepta, ImageView imageView, boolean needsToBeTinted, boolean small) {
        String path = getAbsolutePathImage(recepta);
        setImage(imageView, needsToBeTinted, path, small);

    }

    public static void setImage(ImageView imageView, boolean needsToBeTinted, String path, boolean small) {
        if (hasImage(path)) {
            BitmapFactory.Options ops = new BitmapFactory.Options();
            ops.inSampleSize = 1;
            if (small) {
                ops.inSampleSize = 3;
            }
            Bitmap bm = BitmapFactory.decodeFile(path, ops);
            imageView.setImageBitmap(bm);
            if (needsToBeTinted)
                imageView.setColorFilter(R.color.primary_material_dark, PorterDuff.Mode.OVERLAY);

        } else {
            imageView.setImageResource(R.drawable.dummy);
        }
    }

    public static boolean hasImage(String path) {
        File image = new File(path);
        return image.exists();
    }


    public static Bitmap getBitmap(String path) {

        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 120000; // 1.2MP
            in = new FileInputStream(new File(path));

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }

            Bitmap b = null;
            in = new FileInputStream(new File(path));
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();
            return b;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public static String saveImageRecepta(Recepta recepta, String photoPath) {
        if (photoPath != null) {
            File f = new File(photoPath);
            if (f.exists()) {
                String name = recepta.getImageFilename();
                return renameFile(name, photoPath);
            }
        }
        return "";
    }

    public static String renameFile(String name, String path) {
        File from = new File(path);
        File directory = from.getParentFile();
        File to = new File(directory, name);
        if (!from.renameTo(to)) {
            Log.e(TAG, "Could not rename file");
        }
        return to.getAbsolutePath();
    }

    public static boolean hasImage(Recepta mItem) {
        String path = getAbsolutePathImage(mItem);
        return hasImage(path);
    }
}
