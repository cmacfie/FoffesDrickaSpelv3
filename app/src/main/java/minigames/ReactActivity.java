package minigames;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.christoffer.foffesdrickaspelv3.R;

import java.util.ArrayList;
import java.util.Random;

public class ReactActivity extends MiniGame {


    private TextView middleText;
    private RelativeLayout endLayout;
    private LinearLayout activeLayout;
    private Random r;
    private Button topButton, bottomButton;
    private ArrayList<String> players;
    private String topPlayer, bottomPlayer;
    private int buttonPresser;
    private Thread mainThread, signThread;
    private String[] colors = {"#16ffc265", "#16ff9696", "#16d7afe6", "#169e8886"};
    private boolean stop,buttonPressed;
    private int counter = 3, numOfDrinks;
    private BackgroundColorRunnable backgroundChanger;
    private SignRunner signRunner;
    private Handler buttonHandler, backgroundColorHandler, endGameHandler, signHandler;
    private SquareFrameLayout reactImageHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_react);
        stop = false;


        middleText = (TextView) findViewById(R.id.ReactMiddleText);
        activeLayout = (LinearLayout) findViewById(R.id.ActiveLayout);
        topButton = (Button) findViewById(R.id.ReactTopButton);
        bottomButton = (Button) findViewById(R.id.ReactBottomButton);


        Intent intent = getIntent();
        players = intent.getStringArrayListExtra("players");
        r = new Random();
        topPlayer = players.remove(r.nextInt(players.size()));
        bottomPlayer = players.remove(r.nextInt(players.size()));

        endLayout = (RelativeLayout)findViewById(R.id.EndLayout);

        middleText.setText("Start");
        topButton.setText(topPlayer);
        bottomButton.setText(bottomPlayer);

        buttonHandler = new ButtonHandler();
        backgroundColorHandler = new BackgroundColorHandler();
        endGameHandler = new EndGameHandler();
        signHandler = new SignHandler();
        reactImageHolder = (SquareFrameLayout) findViewById(R.id.reactImageHolder);

        setBackgroundImage("react", ((ImageView)findViewById(R.id.infoImageReact)));
        setBackgroundImage("woodpanel", (ImageView)findViewById(R.id.imageView));
        numOfDrinks = getNumOfDrinks();
    }



    public void onButtonClick_react(View v) {
        if(counter < 1 && buttonPressed == false) {
            setBackgroundImage("", (ImageView)findViewById(R.id.reactImage));
            buttonPressed = true;
            buttonPresser = v.getId();
            buttonHandler.sendEmptyMessage(0);


            EndGameRunnable r = new EndGameRunnable();
            (new Thread(r)).start();
        }
    }

    public void onButtonClick_reactStart(View v) {
            backgroundColorHandler.sendEmptyMessage(0);
            findViewById(R.id.StartLayout).setVisibility(View.GONE);
            counter = 3;


            reactImageHolder.setPivotX(reactImageHolder.getWidth() / 2);
            reactImageHolder.setPivotY(reactImageHolder.getWidth() / 2);

            backgroundChanger = new BackgroundColorRunnable();
            mainThread=new Thread(backgroundChanger);
            mainThread.start();

            signRunner = new SignRunner();
            signThread = new Thread(signRunner);
            signThread.start();
    }

    public void onButtonClick_nextReactGame(View v) throws ClassNotFoundException {
        startActivity(new Intent(this, PickAGameActivity.class));
        finish();
    }



    private class BackgroundColorRunnable implements Runnable {

        int waitTime;
        boolean running = true;

        public void terminate() {
            running = false;
        }
        @Override
        public void run() {
            while (running) {
                synchronized (this) {
                    try {
                        if (counter > 0) {
                            wait(1000);
                            counter--;
                            backgroundColorHandler.sendEmptyMessage(0);
                        } else {
                            backgroundColorHandler.sendEmptyMessage(0);
                            waitTime = r.nextInt(300);
                            wait(600 + waitTime);

                            if (r.nextInt(8) == 0) {
                                stop = true;
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                }

            }
        }
    }

    private class SignHandler extends Handler {


        private float rotation;
        private int rotationDegree = 5;

        public SignHandler(){
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            rotation = (rotation + rotationDegree)%360;
            reactImageHolder.setRotation(rotation);
        }
    }


    private class SignRunner implements Runnable {
        private boolean running = true;

        @Override
        public void run() {
            while(running){
                synchronized (this) {
                    if(counter < 0) {
                        try {
                            signHandler.sendEmptyMessage(0);
                            wait(15);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }


        public void terminate(){
            running = false;
        }
    }

    private class EndGameRunnable implements Runnable{
        private boolean running = true;

        public EndGameRunnable(){
            super();
        }
        @Override
        public void run() {
            while(running){
                synchronized (this) {
                    try{
                        wait(700);
                        endGameHandler.sendEmptyMessage(0);
                        running = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private class EndGameHandler extends Handler{
        public EndGameHandler(){
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            endLayout.setVisibility(View.VISIBLE);
            signRunner.terminate();
        }
    }

    private class ButtonHandler extends Handler{
        public ButtonHandler(){
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            middleText.setTextSize(50);
            if(stop) {
                setBackgroundImage("", (ImageView) findViewById(R.id.reactImage));
                switch (buttonPresser) {
                    case R.id.ReactTopButton:
                        middleText.setText(topPlayer + " won!\n " + bottomPlayer + " drinks " + numOfDrinks);
                        break;
                    case R.id.ReactBottomButton:
                        middleText.setText(bottomPlayer + " won!\n " + topPlayer + " drinks " + numOfDrinks);
                        break;
                }
            } else {
                switch(buttonPresser){
                    case R.id.ReactTopButton:
                        middleText.setText("Too early.\n " + topPlayer + " drinks " + numOfDrinks);
                        break;
                    case R.id.ReactBottomButton:
                        middleText.setText("Too early.\n " + bottomPlayer + " drinks " + numOfDrinks);
                        break;
                }
            }
        }
    }

    private class BackgroundColorHandler extends Handler {

        public BackgroundColorHandler(){
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(counter > 0){
                middleText.setText(""+counter);
            } else {
                if (counter == 0) {
                    middleText.setText("");
                    setBackgroundImage("stopsign", (ImageView) findViewById(R.id.reactImage));
                    counter--;
                }
            }
            if(stop){
                backgroundChanger.terminate();
                middleText.setText("");
                setBackgroundImage("gosign", (ImageView)findViewById(R.id.reactImage));
                activeLayout.setBackgroundColor(Color.parseColor("#9eff0000"));
            } else {
                activeLayout.setBackgroundColor(Color.parseColor(colors[r.nextInt(colors.length)]));
            }
        }
    }

}
