package edu.upc.fib.idi.idireceptes.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import edu.upc.fib.idi.idireceptes.R;
import edu.upc.fib.idi.idireceptes.model.Recepta;

/**
 * Created by casassg on 31/12/15.
 *
 * @author casassg
 */
public final class ImageTreat {

    public static final String TAG = ImageTreat.class.getSimpleName();

    public ImageTreat(String filePath, ImageView imageView, int defaultWidth, int defaultHeight, boolean isTinted) {
        BitmapWorkerTask worker = new BitmapWorkerTask(imageView, defaultWidth, defaultHeight, isTinted);
        worker.execute(filePath);
    }

    public static String getAbsolutePathImage(Recepta recepta) {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, recepta.getImageFilename());
        return image.getAbsolutePath();
    }

    public static boolean hasImage(Recepta recepta) {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, recepta.getImageFilename());
        return image.exists();
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewWeakReference;
        private final int width;
        private final int height;
        private boolean tint;

        public BitmapWorkerTask(ImageView imageView, int width, int height, boolean tint) {
            this.tint = tint;
            int defHeight;
            int defWidth;
            defWidth = imageView.getWidth();
            if (defWidth == 0) {
                defWidth = width;
            }
            this.width = defWidth;
            defHeight = imageView.getHeight();
            if (defHeight == 0) {
                defHeight = height;
            }

            this.height = defHeight;
            imageViewWeakReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            if (params.length > 0) {
                String path = params[0];
                File f = new File(path);
                if (!f.exists()) {
                    return null;
                }
                try {
                    InputStream in = new FileInputStream(path);
                    BitmapFactory.Options ops = new BitmapFactory.Options();
                    ops.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(in, null, ops);
                    in.close();
                    int inWidth = ops.outWidth;
                    int inHeight = ops.outHeight;
                    in = new FileInputStream(path);
                    ops = new BitmapFactory.Options();
                    ops.inSampleSize = Math.max(inWidth / width, inHeight / height);


                    return BitmapFactory.decodeStream(in, null, ops);


                } catch (IOException e) {
                    Log.i(TAG, "No image found");
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            final ImageView imageView = imageViewWeakReference.get();
            if (bitmap != null) {
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                    if (tint)
                        imageView.setColorFilter(R.color.primary_material_dark);

                }
            } else {
                imageView.setImageResource(R.drawable.dummy);
            }
        }


    }
}
