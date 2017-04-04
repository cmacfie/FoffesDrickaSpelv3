package minigames;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christoffer.foffesdrickaspelv3.R;

import java.util.ArrayList;

import DragAndDropListView.dragndroplist.DragNDropCursorAdapter;
import DragAndDropListView.dragndroplist.DragNDropListView;

public class SettingsActivity extends MyAppCompat {

    private static ArrayList<Game> gameList;
    private int numOfActiveGame;
    private ListView listView;
    MyCustomAdapter dataAdapter = null;
    private boolean newGame;
    private static int minDrink = 4, maxDrink = 7;
    private EditText minValueText, maxValueText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Bundle bundle = getIntent().getExtras();
        newGame = bundle.getBoolean("newGame");
        minDrink = 4;
        maxDrink = 7;
        displayListView();
        displayDrinkNumbers();
        numOfActiveGame = gameList.size();
    }

    private void displayDrinkNumbers(){
        minValueText = (EditText)findViewById(R.id.minValue);
        maxValueText = (EditText)findViewById(R.id.maxValue);
        minValueText.setText("" + minDrink);
        maxValueText.setText("" + maxDrink);
    }



    private void displayListView() {
        if(newGame) {
            createGameList();
        }

        dataAdapter = new MyCustomAdapter(this, R.layout.game_info, gameList);

        listView = (ListView)findViewById(R.id.gamesListView);

        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text

                    Game game = (Game) parent.getItemAtPosition(position);
                    CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox1);
                    if(numOfActiveGame > 0 || !cb.isChecked()) {
                        if(cb.isChecked()){
                            numOfActiveGame--;
                        } else {
                            numOfActiveGame++;
                        }
                        if(numOfActiveGame > 0) {
                            String s = "";
                            if(!cb.isChecked()){
                                s = "enabled.";
                            } else {
                                s = "disabled.";
                            }
                            Toast.makeText(SettingsActivity.this, game.toString() + " is now " + s, Toast.LENGTH_SHORT).show();
                            cb.setChecked(!cb.isChecked());
                            game.setActive(cb.isChecked());
                        } else {
                            Toast.makeText(SettingsActivity.this, "At least one game has to be activate", Toast.LENGTH_SHORT).show();
                        }
                    }
            }
        });
    }

    public void onClick_editPlayers(View v){
        Intent intent = new Intent(this, AddPlayersActivity.class);
        intent.putExtra("newGame", false);
        startActivity(intent);
    }

    private static void createGameList(){
        gameList = new ArrayList<Game>();
        gameList.add(new Game("drinks", PickAGameActivity.DRINKS));
        gameList.add(new Game("wordguess", PickAGameActivity.WORDGUESS));
        gameList.add(new Game("react", PickAGameActivity.REACT));
        gameList.add(new Game("rhyme", PickAGameActivity.RHYMES));
        gameList.add(new Game("theme", PickAGameActivity.THEMES));
        gameList.add(new Game("alphabet", PickAGameActivity.ALPHABET));
        gameList.add(new Game("question", PickAGameActivity.QUESTION));
        gameList.add(new Game("story", PickAGameActivity.STORY));
        gameList.add(new Game("hangman", PickAGameActivity.HANGMAN));
    }

    public static ArrayList<Integer> getActiveGames(){
        ArrayList<Integer> activeGames = new ArrayList<>();
        if(gameList == null){
            createGameList();
        }
        for(Game game : gameList){
            if(game.isActive()){
                activeGames.add(game.getGameType());
            }
        }
        if(activeGames.size() == 0){
            createGameList();
        }
        return activeGames;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            if(minValueText.getText().length() > 0){
                minDrink = Integer.parseInt(minValueText.getText().toString());
            }
            if(maxValueText.getText().length() > 0){
                maxDrink = Integer.parseInt(maxValueText.getText().toString());
            }

            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("resumeable", !newGame);
            intent.putExtra("source", "settings");
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public static class Game {

        private boolean active;
        private String name;
        private int gameType;

        public Game(String name, int gameType){
            active = true;
            this.name = name;
            this.gameType = gameType;
        }


        public int getGameType(){
            return gameType;
        }

        public String getName() {
            return name;
        }

        public void setActive(boolean b) {
            active = b;
        }

        public boolean isActive() {
            return active;
        }

        @Override
        public String toString() {
            return Character.toUpperCase(name.charAt(0)) + name.substring(1, name.length());
        }
    }


    private class MyCustomAdapter extends ArrayAdapter<Game> {

        public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Game> gamesList) {
            super(context, textViewResourceId, gamesList);
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.game_info, null);


                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Game game = (Game) cb.getTag();
                        if(numOfActiveGame > 0 || !cb.isChecked()) {
                            if(cb.isChecked()){
                                numOfActiveGame--;
                            } else {
                                numOfActiveGame++;
                            }
                            if(numOfActiveGame > 0) {
                                String s = "";
                                if(cb.isChecked()){
                                    s = "enabled.";
                                } else {
                                    s = "disabled.";
                                }
                                Toast.makeText(SettingsActivity.this, game.toString() + " is now " + s, Toast.LENGTH_SHORT).show();
                                game.setActive(cb.isChecked());
                            } else {
                                Toast.makeText(SettingsActivity.this, "At least one game has to be activate", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }


            Game game = gameList.get(position);
            holder.name.setText(game.toString());
            holder.name.setChecked(game.isActive());
            holder.name.setTag(game);

            ImageView background = (ImageView)convertView.findViewById(R.id.checkboxImage);
            setCheckboxBackground(background, game);

            return convertView;
        }
    }

    private void setCheckboxBackground(ImageView imageView, Game gameType){
        String s = "pattern_";
        switch(gameType.getGameType()){
            case PickAGameActivity.ALPHABET:
                s += "alphabet";
                break;
            case PickAGameActivity.DRINKS:
                s += "drinks";
                break;
            case PickAGameActivity.HANGMAN:
                s += "hangman";
                break;
            case PickAGameActivity.QUESTION:
                s += "questions";
                break;
            case PickAGameActivity.REACT:
                s += "react";
                break;
            case PickAGameActivity.RHYMES:
                s += "rhyme";
                break;
            case PickAGameActivity.STORY:
                s += "story";
                break;
            case PickAGameActivity.THEMES:
                s += "theme";
                break;
            case PickAGameActivity.WORDGUESS:
                s += "wordguess";
                break;
        }
        setBackgroundImage(s, imageView);
    }


    public static int getMinDrink(){
        return minDrink;
    }

    public static int getMaxDrink(){
        return maxDrink;
    }


}
