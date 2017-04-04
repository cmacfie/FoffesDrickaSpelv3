package minigames;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.christoffer.foffesdrickaspelv3.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Christoffer on 2016-02-10.
 */
public class ThemeActivity extends MiniGame {

    private int numOfDrinks;
    private ArrayList<String> themes;
    private String starter;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        topText = (TextView)findViewById(R.id.topText);
        bottomText = (TextView)findViewById(R.id.bottomText);
        nextButton = (Button)findViewById(R.id.nextButton);
        startButton = (Button)findViewById(R.id.startButton);

        setBackgroundImage("theme", ((ImageView)findViewById(R.id.infoImageTheme)));
        setBackgroundImage("woodpanel", (ImageView)findViewById(R.id.imageView));
        //dataClass = getData();

        Intent intent = getIntent();
        themes = intent.getStringArrayListExtra("themes");
        players = intent.getStringArrayListExtra("players");

        numOfDrinks =getNumOfDrinks();
        starter = players.get(r.nextInt(players.size()));
        bottomText.setText(starter + " starts!");
        topText.setText("");

        ImageView nextTimerImage = (ImageView) findViewById(R.id.nextTimerImage);
        ImageView pauseButtonImage = (ImageView) findViewById(R.id.pauseButtonImage);
        TextView timerTextView = (TextView) findViewById(R.id.timerTextView);

        setBackgroundImage("uparrow",((ImageView)findViewById(R.id.increaseButtonImage)));
        setBackgroundImage("downarrow",((ImageView)findViewById(R.id.decreaseButtonImage)));

        setTimer(this);

        //bottomText.setText("Kvar: " + themes.size());
    }




    public void onButtonClick_themeStart(View v) throws IOException {
        if(themes.size() == 0){
            themes = createList("themes.txt");
        }
        ((TextView) findViewById(R.id.topText)).setVisibility(View.VISIBLE);
        topText.setText(themes.remove(r.nextInt(themes.size())));
        bottomText.setText(starter + " starts!" + "\n Loser drinks " + numOfDrinks + "!");

    }

    public void onButtonClick_nextThemeGame(View v) throws ClassNotFoundException {
        //setList(themes, THEMES);
        startActivity(new Intent(this, PickAGameActivity.class));
        finish();
    }


}
