package minigames;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.christoffer.foffesdrickaspelv3.R;

import java.util.ArrayList;

public class GuessWordActivity extends MiniGame {

    private Handler handler;
    private String secretWord;
    private StringBuilder shownWord,hideWord;
    private int counter, pos, charsLeft;
    private char c;
    private ArrayList<String> words;
    private ArrayList<Integer> charPos;
    private boolean pauseFlag;
    private boolean threadActive;
    private Thread mainThread;
    private boolean firstStart;
    private String loserDrinks;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_word);
        counter = 0;
        charPos = new ArrayList<>();
        firstStart = true;

        setBackgroundImage("guessword", ((ImageView)findViewById(R.id.infoImageGuessword)));

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                bottomText.setText(loserDrinks + "\nAntal kombinationer: " + (1+combination()));
                if(threadActive){
                    topText.setText(shownWord);
                    startButton.setText("Pause");

                } else {
                    topText.setText(hideWord);
                    startButton.setText("Start");

                }
            }
        };


        topText = (TextView)findViewById(R.id.topText);
        bottomText = (TextView)findViewById(R.id.bottomText);
        startButton = (Button)findViewById(R.id.startButton);

        pauseFlag = false;
        threadActive = true;

        Intent intent = getIntent();
        players = intent.getStringArrayListExtra("players");

        //ArrayList<String> words = createList("saol.txt");
        int wordSize = r.nextInt(10) + 4;
        words = createList("wordSizes/" + wordSize + "words.txt");

        secretWord = words.remove(r.nextInt(words.size()));
        shownWord = new StringBuilder();
        hideWord = new StringBuilder();


        for(int i = 0; i < secretWord.length(); i++){
            charPos.add(i);
            if(i < secretWord.length()-1) {
                shownWord.append("_ ");
                hideWord.append("* ");

            } else{
                shownWord.append("_");
                hideWord.append("*");
            }
        }
        startButton.setText("Start");
        topText.setText(shownWord);
        loserDrinks = "Winner hands out " + getNumOfDrinks() + " sips!";
        bottomText.setText(loserDrinks);
        charsLeft = secretWord.length();
        setBackgroundImage("woodpanel", (ImageView)findViewById(R.id.imageView));
    }

    private int combination(){
        int count = 0;
        for(String word : words){
            boolean[] positions = new boolean[secretWord.length()];
            int index = 0;
            for(int i = 0; i < shownWord.length(); i+=2){
                if(shownWord.toString().charAt(i) == '_'){
                    positions[index] = true;

                }
                index++;
            }
            String compactShownWord = shownWord.toString().replace("_","");
            compactShownWord = compactShownWord.replace(" ","");
            char[] charArray = word.toCharArray();

            for(int i = 0; i < positions.length; i++){
                if(positions[i]){
                    charArray[i] = '%';
                }
            }
            String compactCompareWord = new String(charArray);
            compactCompareWord = compactCompareWord.replace("%", "");
            if(compactCompareWord.equals(compactShownWord)){
                Log.w("Compare", word + " vs " + secretWord);
                count++;
            }
        }
        return count;
    }


    public void onButtonClick_guessWordStart(View v) {
        handler.sendEmptyMessage(0);
        //topText.setText(shownWord);,
        if(!firstStart) {
            threadActive = !threadActive;
            if (threadActive) {
                pauseFlag = false;
                synchronized (mainThread) {
                    mainThread.notify();
                }
            } else {
                pauseFlag = true;
            }
            //handler.sendEmptyMessage(0);
        }

        if(firstStart) {
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    while (counter < secretWord.length()) {
                        if (pauseFlag) {
                            synchronized (mainThread) {
                                try {
                                    mainThread.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            synchronized (this) {
                                try {
                                    int i = r.nextInt(charPos.size());
                                    pos = charPos.remove(i);
                                    c = secretWord.charAt(pos);

                                    shownWord.setCharAt(pos * 2, c);
                                    counter++;

                                    int waitTime = 13 - charsLeft;
                                    wait(900 + (waitTime*100));

                                    charsLeft--;

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        handler.sendEmptyMessage(0);
                    }
                }
            };
            mainThread = new Thread(runnable);
            mainThread.start();

            firstStart = false;
        }
    }

    public void onButtonClick_nextGuessWordGame(View v) throws ClassNotFoundException {
        startActivity(new Intent(this, PickAGameActivity.class));
        finish();
    }


}
