package minigames;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.christoffer.foffesdrickaspelv3.R;

import java.io.File;
import java.io.InputStream;

/**
 * Created by Christoffer on 2016-03-18.
 */
public abstract class MyAppCompat extends AppCompatActivity {

    protected Timer timer;

    public void onClick_hideImageInfo(View v){
        v.setVisibility(View.GONE);
    }

    public void setBackgroundImage(String fileName, ImageView imageView){

        int imageID = 0;

        if(fileName.length() > 0) {
            imageID = getResources().getIdentifier(fileName, "raw", getPackageName());
        }
        if(imageID > 0){
                InputStream is = getResources().openRawResource(imageID);
                Bitmap imageBitmap = BackgroundSetter.decodeFile(is);
                imageView.setImageBitmap(imageBitmap);
        }else {
            imageView.setImageResource(0);
        }
    }

    public void onClick_startTimer(View v){
        timer.startTimer();
    }

    public void onClick_increaseTimer(View v){
        timer.increaseTimer();
    }

    public void onClick_decreaseTimer(View v){
        timer.decreaseTimer();
    }

    public boolean onClick_resetTimer(View v){
        return timer.resetTimer();
    }

    public void setTimer(MiniGame miniGame) {
        ImageView nextTimerImage = (ImageView) findViewById(R.id.nextTimerImage);
        ImageView pauseButtonImage = (ImageView) findViewById(R.id.pauseButtonImage);
        ImageView decreaseButtonImage = (ImageView) findViewById(R.id.decreaseButtonImage);
        ImageView increaseButtonImage = (ImageView) findViewById(R.id.increaseButtonImage);
        TextView timerTextView = (TextView) findViewById(R.id.timerTextView);

        timer = new Timer(miniGame, timerTextView, pauseButtonImage, nextTimerImage, increaseButtonImage, decreaseButtonImage);
    }


    public void onClick_nextGame(View v) throws ClassNotFoundException {
        startActivity(new Intent(this, PickAGameActivity.class));
        finish();
    }

}
