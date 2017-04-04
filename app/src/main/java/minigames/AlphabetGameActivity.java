package minigames;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.christoffer.foffesdrickaspelv3.R;

import java.util.ArrayList;

public class AlphabetGameActivity extends MiniGame {


    private int numOfDrinks;
    private ArrayList<String> players, alphabet;
    private String starter;
    private int letterCounter;
    private TextView letterTextView;
    private boolean privateFailMode = false;
    private String[] letters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","X","Y","Z","Å","Ä","Ö"};


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alphabet_game);

        topText = (TextView) findViewById(R.id.topText);
        bottomText = (TextView) findViewById(R.id.bottomText);

        Intent intent = getIntent();
        alphabet = intent.getStringArrayListExtra("alphabet");
        players = intent.getStringArrayListExtra("players");
        starter = players.get(r.nextInt(players.size()));
        bottomText.setText(starter + " starts!");
        topText.setText("");

        numOfDrinks = getNumOfDrinks();
        setBackgroundImage("woodpanel", (ImageView)findViewById(R.id.imageView));
        setBackgroundImage("alphabet", ((ImageView)findViewById(R.id.infoImageAlphabet)));
        setBackgroundImage("uparrow", ((ImageView)findViewById(R.id.nextLetterImage)));
        setBackgroundImage("uparrow", ((ImageView)findViewById(R.id.previousLetterImage)));
        setTimer(this);
        letterTextView = (TextView) findViewById(R.id.letterTextView);
        letterCounter = 0;
    }

    public void onClick_AlphabetResetTimer(View v){
        boolean inFailMode = onClick_resetTimer(v);
        if(!inFailMode && !privateFailMode) {
            letterCounter++;
            letterTextView.setText(letters[letterCounter % letters.length]);
        }
        privateFailMode = inFailMode;
    }

    public void onClick_nextLetter(View v){
        letterCounter++;
        letterTextView.setText(letters[letterCounter % letters.length]);

    }

    public void onClick_previousLetter(View v){
        if(letterCounter == 0){
            letterCounter = letters.length;
        } else {
            letterCounter--;
        }
        letterTextView.setText(letters[letterCounter % letters.length]);
    }



    public void onClick_alphabetStart(View v){
        if(alphabet.size() == 0){
            alphabet = createList("alphabet.txt");
        }
        ((TextView) findViewById(R.id.topText)).setVisibility(View.VISIBLE);
        topText.setText(alphabet.remove(r.nextInt(alphabet.size())));
        bottomText.setText(starter + " starts!" + "\n Loser drinks " + numOfDrinks + "!");
    }

    public void onClick_nextAlphabetGame(View v) throws ClassNotFoundException {
        //setList(alphabet, ALPHABET);
        startActivity(new Intent(this, PickAGameActivity.class));
        finish();
    }

}
