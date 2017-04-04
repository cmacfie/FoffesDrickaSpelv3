package minigames;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Christoffer on 2016-02-16.
 */
public class DataClass {

    protected FileLoader fl;
    protected ArrayList<String> players, themes, alphabet, saol, rhymes, stories;
    protected Random r;
    public static final int SAOL = 0, THEMES = 1, ALPHABET = 2, PLAYERS = 3, RHYMES = 4, STORIES = 5;
    private Context context;

    public DataClass(Context context){
        this.context = context;
        fl = new FileLoader();
        r = new Random();
        players = AddPlayersActivity.getPlayers();
        saol = createList("saol.txt");
        alphabet = createList("alphabet.txt");
        themes = createList("themes.txt");
        rhymes = createList("rhymes.txt");
        stories = createList("stories.txt");
    }




    public ArrayList<String> getList(int listName){
        ArrayList<String> list = null;
        switch (listName) {
            case SAOL:
                list = saol;
                break;
            case THEMES:
                list = themes;
                break;
            case ALPHABET:
                list = alphabet;
                break;
            case PLAYERS:
                list = players;
                break;
            case RHYMES:
                list = rhymes;
                break;
            case STORIES:
                list = stories;
                break;
        }
        return list;
    }

    public void setList(ArrayList<String> list, int listName){
        switch (listName) {
            case SAOL:
                saol = list;
                break;
            case THEMES:
                themes = list;
                break;
            case ALPHABET:
                alphabet = list;
                break;
            case PLAYERS:
                players = list;
                break;
            case RHYMES:
                rhymes = list;
                break;
            case STORIES:
                stories = list;
                break;
        }
    }


/*
    public String getRandom(int listName){
        int randNum = 0;
        String word = "";
        switch (listName) {
            case SAOL:
                randNum = r.nextInt(saol.size());
                if (saol.size() == 0) {
                    updateList(SAOL);
                }
                word = saol.remove(randNum);
            break;
            case THEMES:
                randNum = r.nextInt(themes.size());
                if (themes.size() == 0) {
                    updateList(THEMES);
                }
                word = themes.remove(randNum);
            break;
            case ALPHABET:
                randNum = r.nextInt(alphabet.size());
                if (alphabet.size() == 0) {
                    updateList(ALPHABET);
                }
                word = alphabet.remove(randNum);
            break;
            case PLAYERS:
                randNum = r.nextInt(players.size());
                word = players.get(randNum);
                break;
        }
        return word;
    }

    public void updateList(int listName){
        if(listName == SAOL){
            saol = createList("saol.txt");
        } else if (listName == THEMES){
            themes = createList("theme.txt");
        } else if (listName == ALPHABET){
            alphabet = createList("alhabet.txt");
        }
    }
*/
    public ArrayList<String> createList(String s){
        return fl.createList(s);
    }

    private class FileLoader {

        public FileLoader(){};

        public ArrayList<String> createList(String fileName) {
            AssetManager am = context.getAssets();
            ArrayList<String> words = new ArrayList<>();
            try {
                InputStream stream = am.open(fileName);
                BufferedReader br = new BufferedReader(new InputStreamReader(stream));

                String line = br.readLine();

                while(line != null){
                    words.add(line);
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return words;
        }

    }

}
