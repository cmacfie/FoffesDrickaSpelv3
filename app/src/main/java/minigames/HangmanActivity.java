package minigames;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.christoffer.foffesdrickaspelv3.R;

import java.util.ArrayList;
import java.util.Random;

public class HangmanActivity extends MiniGame {

    String secretWord;
    int progressCounter, drinkCounter;
    TextView secretWordTextView, wrongLettersTextView, drinkText, startTextView;
    StringBuilder shownWord, wrongLetters;
    LinearLayout keyboard;
    SquareImageView hangmanImageView;
    TextHandler textHandler;
    Random r;
    EndGameHandler endGameHandler;
    String player;
    private int correctLettersCounter;
    private final static int LETTERCONTAINED = 0, LETTERNOTCONTAINED = 1, ENDGAMEHANGED = 2, ENDGAMESURVIVE = 3;
    private char latestLetter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangman);
        secretWordTextView = (TextView) findViewById(R.id.hangmanTextview);
        wrongLettersTextView = (TextView) findViewById(R.id.hangmanWrongletters);
        hangmanImageView = (SquareImageView) findViewById(R.id.hangmanImageView);
        keyboard = (LinearLayout)findViewById(R.id.hangmanKeyboard);
        drinkText = (TextView) findViewById(R.id.drinkText);
        startTextView = (TextView) findViewById(R.id.starterTextView);
        textHandler = new TextHandler();
        endGameHandler = new EndGameHandler();
        progressCounter = 1;
        drinkCounter = 0;
        correctLettersCounter = 0;
        setBackgroundImage("paper",(ImageView)findViewById(R.id.paperImageView));
        setBackgroundImage("woodpanel",(ImageView)findViewById(R.id.background));
        ArrayList<String> players = AddPlayersActivity.getPlayers();
        r = new Random();
        player = players.get(r.nextInt((players.size())));
        startTextView.setText(player + " chooses a word");
    }

    private void init(){
        wrongLetters = new StringBuilder();
        shownWord = new StringBuilder();
        for(int i = 0; i < secretWord.length()-1; i++){
            shownWord.append("_ ");
        }
        shownWord.append("_");
        secretWordTextView.setText(shownWord.toString());
        keyboard.setVisibility(View.VISIBLE);
    }


    public void onClick_startHangmanGame(View v){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        startTextView.setVisibility(View.GONE);
        ((LinearLayout)findViewById(R.id.hangmanHolder)).setVisibility(View.VISIBLE);

        EditText et = (EditText) findViewById(R.id.hangmanEdittext);
        String text = et.getText().toString();
        if(text.length() > 0){
            secretWord = text.toUpperCase();
            init();
            secretWordTextView.setVisibility(View.VISIBLE);
            et.setVisibility(View.GONE);
            ((Button)findViewById(R.id.hangmanStartButton)).setVisibility(View.GONE);
        }
    }

    private class EndGameHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            keyboard.setVisibility(View.GONE);
            ((FrameLayout)findViewById(R.id.nextButtonHolder)).setVisibility(View.VISIBLE);
            switch(msg.what){
                case ENDGAMEHANGED:
                    StringBuilder sb = new StringBuilder();
                    for(int i = 0; i < secretWord.length()-1; i++){
                        sb.append(secretWord.charAt(i) + " ");
                    }
                    sb.append(secretWord.charAt(secretWord.length()-1));
                    secretWordTextView.setText(sb);
                    secretWordTextView.setTextColor(Color.RED);
                    break;
                case ENDGAMESURVIVE:
                    secretWordTextView.setTextColor(Color.GREEN);
                    startTextView.setVisibility(View.VISIBLE);
                    startTextView.setText(player + " drinks " + getNumOfDrinks());
                    ((LinearLayout)findViewById(R.id.hangmanHolder)).setVisibility(View.GONE);
                    break;
            }
        }
    }

    private class TextHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LETTERCONTAINED:
                    for(int i = 0; i < secretWord.length(); i++){
                        if(Character.compare(secretWord.charAt(i),latestLetter) == 0){
                            shownWord.setCharAt(i * 2, latestLetter);
                            correctLettersCounter++;
                        }
                    }
                    secretWordTextView.setText(shownWord.toString());
                    if(correctLettersCounter == secretWord.length()){
                        endGameHandler.sendEmptyMessage(ENDGAMESURVIVE);
                    }
                    break;
                case LETTERNOTCONTAINED:
                    wrongLetters.append(latestLetter);
                    drinkCounter++;
                    drinkText.setText("Winner hands out " + drinkCounter/2);
                    setBackgroundImage("hangmanpart" + progressCounter, hangmanImageView);
                    progressCounter++;
                    wrongLettersTextView.setText(wrongLetters);
                    if(progressCounter > 11) {
                        endGameHandler.sendEmptyMessage(ENDGAMEHANGED);
                        }
                    break;
            }
        }
    }

    public void onClick_keyboardButton(View v){
        Button b = (Button) v;
        char letter = new Character(b.getText().charAt(0));
        latestLetter = letter;
        b.setVisibility(View.GONE);
        if(secretWord.contains(letter + "")){
            textHandler.sendEmptyMessage(LETTERCONTAINED);
        } else {
            textHandler.sendEmptyMessage(LETTERNOTCONTAINED);
        }

    }
}
