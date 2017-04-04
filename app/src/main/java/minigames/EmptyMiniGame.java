package minigames;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.christoffer.foffesdrickaspelv3.R;

/**
 * Created by Christoffer on 2016-04-15.
 */
public abstract class EmptyMiniGame extends MiniGame {

    private int numOfDrinks;
    private String starter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void init(){
        setBackgroundImage("woodpanel", (ImageView)findViewById(R.id.imageView));
        bottomText = (TextView)findViewById(R.id.bottomText);
        nextButton = (Button)findViewById(R.id.nextButton);
        Intent intent = getIntent();
        players = intent.getStringArrayListExtra("players");
        numOfDrinks =getNumOfDrinks();
        starter = players.get(r.nextInt(players.size()));
        bottomText.setText(starter + " starts!" + "\n Loser drinks " + numOfDrinks + "!");
    }

    public void setBackgroundImage(String imageFileName){
        setBackgroundImage(imageFileName, ((ImageView) findViewById(R.id.infoImageStory)));
    }
}
