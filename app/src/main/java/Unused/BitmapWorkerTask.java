package Unused;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by Christoffer on 2016-03-18.
 */
public class BitmapWorkerTask extends AsyncTask<File, Void, Bitmap> {

    WeakReference<ImageView> imageViewReferences;
    final static int TARGET_IMAGE_VIEW_WIDTH = 1440;
    final static int TARGET_IMAGE_VIEW_HEIGHT = 2560;

    public BitmapWorkerTask(ImageView imageview){

    }

    @Override
    protected Bitmap doInBackground(File... params) {
        return decodeBitmapFromFile(params[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap){
        if(bitmap != null && imageViewReferences != null){
            ImageView viewImage = imageViewReferences.get();
            if(viewImage != null){
                viewImage.setImageBitmap(bitmap);
            }
        }
    }

    private int calculateInSampleSize(BitmapFactory.Options bmOptions){
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

    private Bitmap decodeBitmapFromFile(File imageFile){
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
        bmOptions.inSampleSize = calculateInSampleSize(bmOptions);
        bmOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
    }
}
