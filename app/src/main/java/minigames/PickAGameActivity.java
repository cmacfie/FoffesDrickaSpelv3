package minigames;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.christoffer.foffesdrickaspelv3.R;

import java.util.ArrayList;
import java.util.Random;

public class PickAGameActivity extends MyAppCompat {

    private DataClass dataClass;
    /** Motsols håll i cirkeln */
    public static final int THEMES = 0, REACT = 1, DRINKS = 2, WORDGUESS = 3, ALPHABET = 4, STORY = 5, QUESTION = 6, RHYMES = 7, HANGMAN = 8;
    public static final int PLAYERS = -1, SAOL = -2;

    private SquareImageView wheel;
    private float spinTime, spinDegreeCounter, rotation;
    private int spinTimeCounter, pieSlices;
    private SpinnHandler handler;
    private Thread wheelThread, miniGameThread;
    private boolean threadIsRunning;
    private MyWheelSpinRunner wheelRunner;
    private MyStartNewMiniGameRunner miniGameRunner;
    private MediaPlayer mp;
    private SquareFrameLayout pieSliceHolder;
    private ArrayList<Integer> gameTypes;

    //private fl Fi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_agame);
        dataClass = new DataClass(this);

        wheel = (SquareImageView)findViewById(R.id.gamewheel);
        pieSliceHolder = (SquareFrameLayout)findViewById(R.id.pieSliceHolder);
        handler = new SpinnHandler();
        threadIsRunning = false;
        spinTime = 0;
        pieSlices = 16;


        gameTypes = SettingsActivity.getActiveGames();


        setBackgroundImage("woodpanel", ((ImageView)findViewById(R.id.pickAGameWallpaper)));
        //setBackgroundImage("gamewheel_v9", ((ImageView)findViewById(R.id.gamewheel)));
        mp = MediaPlayer.create(this, R.raw.lottowheelsound);
        setPieSlices();
    }

    private void setPieSlices(){
        int pivot = 1300 / 2;
        int numOfSlices = 16;
        int gameCounter = 0;
        for(int i = 0; i < numOfSlices; i++){
            String imageID = "pieSlice" + i;
            int resID = getResources().getIdentifier(imageID, "id", "com.example.christoffer.foffesdrickaspelv3");
            ImageView pieSlice = (ImageView) findViewById(resID);
            pieSlice.setVisibility(View.VISIBLE);
            setPieSlice(gameTypes.get(i % gameTypes.size()), pieSlice);
            pieSlice.setPivotX(pivot);
            pieSlice.setPivotY(pivot);
            float rotation = -(360 / (float)numOfSlices) * i;
            pieSlice.setRotation(rotation); //Add backwards
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("source", "piackagame");
            intent.putExtra("resumeable", true);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }



    public void onClick_rotateWheel(View v){
        if(threadIsRunning == false){
            spinTime = ((new Random()).nextFloat()+8)*360;
            mp.start();
            spinDegreeCounter = 0;
            spinTimeCounter = 0;
            threadIsRunning = true;
            wheelRunner = new MyWheelSpinRunner();
            wheelThread = new Thread(wheelRunner);
            wheelThread.start();
        }
    }

    private void rotateHolder(float degree) {
        pieSliceHolder.setPivotX(pieSliceHolder.getWidth() / 2);
        pieSliceHolder.setPivotY(pieSliceHolder.getWidth() / 2);
        if(spinDegreeCounter < spinTime) {
            spinDegreeCounter +=degree;
            rotation = (rotation + degree) % 360;
            pieSliceHolder.setRotation(rotation);
        } else {
            pieSliceHolder.setRotation((rotation + spinTime - spinDegreeCounter)%360);
            mp.stop();
            //threadIsRunning = false;          Ej möjligt att starta hjulet igen när det stannat.
            wheelRunner.terminate();
            miniGameRunner = new MyStartNewMiniGameRunner();
            Thread miniGameThread = new Thread(miniGameRunner);
            miniGameThread.start();
        }
    }

    private void rotate(float degree) {
        wheel.setPivotX(wheel.getWidth() / 2);
        wheel.setPivotY(wheel.getWidth() / 2);
        if(spinDegreeCounter < spinTime) {
            spinDegreeCounter +=degree;
            rotation = (rotation + degree) % 360;
            wheel.setRotation(rotation);
        } else {
            wheel.setRotation((rotation + spinTime - spinDegreeCounter)%360);
            mp.stop();
            //threadIsRunning = false;          Ej möjligt att starta hjulet igen när det stannat.
            wheelRunner.terminate();
            miniGameRunner = new MyStartNewMiniGameRunner();
            Thread miniGameThread = new Thread(miniGameRunner);
            miniGameThread.start();
        }
    }

    private class SpinnHandler extends Handler{

        public SpinnHandler(){
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            spinTimeCounter++;
            float f = (float) (16 - 16 * Math.cos(spinTimeCounter/(4*spinTime/360)));
            rotateHolder(f);
            //rotate(f);
        }

    }

    private class MyStartNewMiniGameRunner implements Runnable {

        private boolean running = true;

        @Override
        public void run() {
            while(running){
                synchronized (this){
                    try{
                        wait(1000);
                        startNewMiniGame();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void terminate(){
            running = false;
        }
    }

    private class MyWheelSpinRunner implements Runnable {
        private boolean running = true;
        @Override
        public void run() {
            while(running){

                synchronized (this) {
                    try {
                        handler.sendEmptyMessage(0);
                        wait(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public boolean isRunning(){
            return running;
        }


        public void terminate(){
            running = false;
        }
    }

    public void startNewMiniGame() {
        int degrees = Math.round(spinTime)%360;
        int gameNumber = gameTypes.get((degrees)/(360/(pieSlices))% gameTypes.size());
        Intent intent = null;
        switch(gameNumber) {
            case ALPHABET:
                intent = new Intent(this, AlphabetGameActivity.class);
                intent.putStringArrayListExtra("alphabet", dataClass.getList(DataClass.ALPHABET));
                break;
            case REACT:
                intent = new Intent(this, ReactActivity.class);
                break;
            case RHYMES:
                intent = new Intent(this, RhymeActivity.class);
                ArrayList<String> rhymes = dataClass.getList(DataClass.RHYMES);
                intent.putStringArrayListExtra("rhymes", rhymes);
                break;
            case STORY:
                intent = new Intent(this, StoryActivity.class);
                intent.putStringArrayListExtra("stories", dataClass.getList(DataClass.STORIES));
                break;
            case THEMES:
                intent = new Intent(this, ThemeActivity.class);
                intent.putStringArrayListExtra("themes", dataClass.getList(DataClass.THEMES));
                break;
            case WORDGUESS:
                intent = new Intent(this, GuessWordActivity.class);
                break;
            case QUESTION:
                intent = new Intent(this, QuestionActivity.class);
                break;
            case HANGMAN:
                intent = new Intent(this, HangmanActivity.class);
                break;
            default:  //Drinks
                intent = new Intent(this, DrinksActivity.class);
                break;
        }
        intent.putStringArrayListExtra("players", dataClass.getList(DataClass.PLAYERS));
        startActivity(intent);
        miniGameRunner.terminate();
        finish();
    }

    public void setList (ArrayList<String> list, int listName){
        switch(listName){
            case THEMES:
                dataClass.setList(list, dataClass.THEMES);
                break;
            case ALPHABET:
                dataClass.setList(list, dataClass.ALPHABET);
                break;
            case SAOL:
                dataClass.setList(list, dataClass.SAOL);
                break;
            case PLAYERS:
                dataClass.setList(list, dataClass.PLAYERS);
                break;
            case RHYMES:
                dataClass.setList(list, dataClass.RHYMES);
                break;
        }
    }



    private void setPieSlice(int gameType, ImageView pieSlice){
        String s = "pieslice16_";
        switch(gameType){
            case DRINKS:
                s += "drinks";
                break;
            case ALPHABET:
                s += "alphabet";
                break;
            case REACT:
                s += "react";
                break;
            case RHYMES:
                s+= "rhyme";
                break;
            case STORY:
                s += "story";
                break;
            case THEMES:
                s += "theme";
                break;
            case WORDGUESS:
                s += "wordguess";
                break;
            case QUESTION:
                s += "question";
                break;
            case HANGMAN:
                s += "hangman";
                break;
            default:
                s += "drinks";
                break;
        }
        setBackgroundImage(s, pieSlice);
    }

}
