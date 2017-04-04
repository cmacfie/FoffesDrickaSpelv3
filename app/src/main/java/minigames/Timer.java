package minigames;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.christoffer.foffesdrickaspelv3.R;

/**
 * Created by Christoffer on 2016-04-06.
 */
public class Timer {


    private int counter;
    private int timerStartValue;
    private TimerHandler timerHandler;
    private ImageHandler imageHandler;
    private TimerRunnable r;
    private static final int UPDATETIMER = 0, ENDTIMER = 1;
    private static final int PAUSESIGN = 0, PLAYSIGN = 1, NEXTSIGN = 2, CHANGETEXTCOLOR = 4, INCREASE= 5, DECREASE = 6;
    private ImageView pauseButtonImage, nextButtonImage, increaseButtonImage, decreaseButtonImage;
    private TextView timerTextView;
    private Thread timerThread;
    private MiniGame game;
    private boolean failed;

    public Timer(MiniGame game, TextView timerTextView, ImageView pauseButtonImage, ImageView nextButtonImage, ImageView increaseButtonImage, ImageView decreaseButtonImage){
        this.pauseButtonImage = pauseButtonImage;
        this.nextButtonImage = nextButtonImage;
        this.increaseButtonImage = increaseButtonImage;
        this.decreaseButtonImage = decreaseButtonImage;
        this.timerTextView = timerTextView;
        this.game = game;
        timerHandler = new TimerHandler();
        imageHandler = new ImageHandler();
        counter = 15;
        timerStartValue = 15;
        failed = false;
        init();
    }


    private void init(){
        imageHandler.sendEmptyMessage(PLAYSIGN);
        imageHandler.sendEmptyMessage(NEXTSIGN);
        imageHandler.sendEmptyMessage(DECREASE);
        imageHandler.sendEmptyMessage(INCREASE);
        timerHandler.updateTimer();
    }

    public void startTimer(){
        if(timerThread == null) {
            r = new TimerRunnable();
            timerThread = new Thread(r);
            timerThread.start();
            imageHandler.sendEmptyMessage(PAUSESIGN);
        } else {
            pauseTimer();
        }
    }

    private void pauseTimer(){
        r.setTimerRunning(!r.isTimerRunning());
        if(r.isTimerRunning()){
            imageHandler.sendEmptyMessage(PAUSESIGN);
        } else {
            imageHandler.sendEmptyMessage(PLAYSIGN);
        }
    }

    public void increaseTimer() {
        if (r == null || (!r.isTimerRunning() && r.isThreadRunning())){
            counter++;
            timerStartValue = counter;
            timerHandler.sendEmptyMessage(UPDATETIMER);
        }
    }

    public void decreaseTimer(){
        if(counter > 1 && (r == null || (!r.isTimerRunning() && r.isThreadRunning()))){
            counter--;
            timerStartValue = counter;
            timerHandler.sendEmptyMessage(UPDATETIMER);
        }
    }

    public boolean resetTimer(){
        boolean inFailMode = failed;
        if(!inFailMode) {
            timerTextView.setTextColor(Color.WHITE);
            counter = timerStartValue;
            timerHandler.sendEmptyMessage(UPDATETIMER);
        } else {
            failed = false;
            timerThread = null;
            imageHandler.sendEmptyMessage(PLAYSIGN);
        }
        return inFailMode;
    }

    public boolean hasFailed(){
        return failed;
    }

    private class TimerHandler extends Handler {
        public TimerHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch(msg.what){
                case UPDATETIMER:
                    updateTimer();
                    break;
                case ENDTIMER:
                    endTimer();
                    break;
            }
        }

        private void endTimer(){
            timerTextView.setText("0");
            timerTextView.setTextColor(Color.RED);
        }

        private void updateTimer(){
            timerTextView.setText("" + counter);
        }
    }

    private class ImageHandler extends Handler {
        public ImageHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case PLAYSIGN:
                    game.setBackgroundImage("playsign", pauseButtonImage);
                    break;
                case PAUSESIGN:
                    game.setBackgroundImage("pausesign", pauseButtonImage);
                    break;
                case NEXTSIGN:
                    game.setBackgroundImage("nextsign", nextButtonImage);
                    break;
                case CHANGETEXTCOLOR:
                    if(timerThread == null){
                        timerTextView.setTextColor(Color.WHITE);
                    } else {
                        timerTextView.setTextColor(Color.RED);
                    }
                    break;
                case DECREASE:
                    game.setBackgroundImage("downarrow", decreaseButtonImage);
                    break;
                case INCREASE:
                    game.setBackgroundImage("uparrow", increaseButtonImage);
                    break;
            }
        }
    }

    private class TimerRunnable implements Runnable {

        private boolean threadRunning = true;
        private boolean timerRunning = true;

        public void terminate(){
            threadRunning = false;
        }

        public void setTimerRunning(boolean running){
            timerRunning = running;
        }

        public boolean isTimerRunning(){
            return timerRunning;
        }

        public boolean isThreadRunning(){
            return threadRunning;
        }

        @Override
        public void run() {
            while(threadRunning){
                synchronized (this){
                    try{
                        if(timerRunning){
                            if(counter > 1){
                                counter--;
                                timerHandler.sendEmptyMessage(UPDATETIMER);
                                wait(1000);
                            }else {
                                terminate();
                                failed = true;
                                timerHandler.sendEmptyMessage(ENDTIMER);
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
