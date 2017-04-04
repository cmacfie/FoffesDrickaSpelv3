package minigames;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.christoffer.foffesdrickaspelv3.R;

import java.io.IOException;
import java.util.ArrayList;

public class RhymeActivity extends MiniGame {


    private int numOfDrinks;
    //private DataClass dataClass;
    private ArrayList<String> rhymes, players;
    private String starter;




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rhyme);

        setBackgroundImage("rhyme", ((ImageView)findViewById(R.id.infoImageRhyme)));

        topText = (TextView)findViewById(R.id.topText);
        bottomText = (TextView)findViewById(R.id.bottomText);
        nextButton = (Button)findViewById(R.id.nextButton);
        startButton = (Button)findViewById(R.id.startButton);

        //dataClass = getData();
        Intent intent = getIntent();
        rhymes = intent.getStringArrayListExtra("rhymes");
        players = intent.getStringArrayListExtra("players");


        numOfDrinks = getNumOfDrinks();

        starter = players.get(r.nextInt(players.size()));
        bottomText.setText(starter + " starts!");
        topText.setText("");
        setBackgroundImage("woodpanel", (ImageView) findViewById(R.id.imageView));
        //rhymes = createList("saol.txt");
        setTimer(this);
    }



    public void onButtonClick_rhymeStart(View v) throws IOException {
        if(rhymes.size() == 0){
            rhymes = createList("rhymes.txt");
        }

        topText.setVisibility(View.VISIBLE);
        topText.setText(rhymes.remove(r.nextInt(rhymes.size())));
        bottomText.setText(starter + " starts!" + "\nLoser drinks " + numOfDrinks + "!");
    }

    public void onButtonClick_nextRhymeGame(View v) throws ClassNotFoundException {
        //setList(rhymes, RHYMES);
        startActivity(new Intent(this, PickAGameActivity.class));
        finish();
    }






}
