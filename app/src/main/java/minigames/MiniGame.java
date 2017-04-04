package minigames;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.christoffer.foffesdrickaspelv3.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;


public abstract class MiniGame extends MyAppCompat{

    protected FileLoader fl;
    protected TextView bottomText;
    protected TextView topText;
    protected Button nextButton;
    protected Button startButton;
    protected ArrayList<String> players;
    private int maxDrinks = 7, minDrinks = 4;
    //protected ArrayList<String> themes;
    //protected ArrayList<String> alphabet;
    //protected ArrayList<String> saol;
    //DataClass dataClass;
    protected Random r;

    public MiniGame(){
        fl = new FileLoader();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rhyme);
        r = new Random();
        players = AddPlayersActivity.getPlayers();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("resumeable", true);
            intent.putExtra("source", "minigame");
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    public static int getNumOfDrinks(){
        Random r = new Random();
        return SettingsActivity.getMinDrink() + r.nextInt(SettingsActivity.getMaxDrink()-SettingsActivity.getMinDrink());
    }



    public ArrayList<String> createList(String s){
        return fl.createList(s);
    }

    private class FileLoader {

        public FileLoader(){};

        public ArrayList<String> createList(String fileName) {
            AssetManager am = getAssets();
            ArrayList<String> words = new ArrayList<>();
            try {
                InputStream stream = am.open(fileName);
                BufferedReader br = new BufferedReader(new InputStreamReader(stream));

                String rhyme = br.readLine();

                while(rhyme != null){
                    words.add(rhyme);
                    rhyme = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return words;
        }

    }



}
