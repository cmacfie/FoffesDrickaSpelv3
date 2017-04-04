package minigames;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by Christoffer on 2016-03-18.
 */
public final class BackgroundSetter {


    final static int TARGET_IMAGE_VIEW_WIDTH = 1440;
    final static int TARGET_IMAGE_VIEW_HEIGHT = 2560;


    public static Bitmap decodeFile(InputStream is) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, o);
        o.inSampleSize = calculateInSampleSize(o);
        o.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is, null, o);
    }

    private static int calculateInSampleSize(BitmapFactory.Options bmOptions){
        final int photoWidth = bmOptions.outWidth;
        final int photoHeight = bmOptions.outHeight;
        int scaleFactor = 1;

        if(photoWidth > TARGET_IMAGE_VIEW_WIDTH || photoHeight > TARGET_IMAGE_VIEW_HEIGHT){
            final int halfPhotoWidth = photoWidth / 2;
            final int halfPhotoHeight = photoHeight / 2;
            while(halfPhotoWidth/scaleFactor > TARGET_IMAGE_VIEW_WIDTH || halfPhotoHeight/scaleFactor > TARGET_IMAGE_VIEW_HEIGHT){
                scaleFactor *= 2;
            }
        }
        return scaleFactor;
    }
/**
    public static Bitmap decodeBitmapFromFile(File imageFile){
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
        bmOptions.inSampleSize = calculateInSampleSize(bmOptions);
        bmOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
    }
 */
}
