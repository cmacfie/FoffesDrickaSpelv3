package minigames;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.christoffer.foffesdrickaspelv3.R;

import java.util.ArrayList;
import java.util.Random;

public class DrinksActivity extends MiniGame {

    private TextView tw;
    private Random r;
    private ArrayList<String> players;
    private int counter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinks);

        tw = (TextView)findViewById(R.id.infoText);;
        r = new Random();
        players = AddPlayersActivity.getPlayers();

        int playerNum = r.nextInt(players.size());
        String[] m = new String[2];
        m[0] = "drinks";
        m[1] = "hands out";
        int j = r.nextInt(2);
        tw.setText(players.get(playerNum) + " " + m[j] + " " + getNumOfDrinks() + "!");
        setBackgroundImage("drinks", ((ImageView) findViewById(R.id.infoImageDrinks)));
        setBackgroundImage("woodpanel", (ImageView)findViewById(R.id.imageView));
    }

    public void onButtonClick_nextRandomGame(View v) throws ClassNotFoundException {
        startActivity(new Intent(this, PickAGameActivity.class));
        finish();
    }


    public void onClick_removeInfoBox(View v){
        ((ImageView)findViewById(R.id.infoImageDrinks)).setVisibility(View.GONE);
    }
}
