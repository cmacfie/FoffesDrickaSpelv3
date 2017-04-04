package minigames;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.widget.TextView;

import com.example.christoffer.foffesdrickaspelv3.R;

import java.util.ArrayList;

public class WordShuffleActivity extends MiniGame {

    private TextView topText, bottomText;
    private ArrayList<String> words;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_shuffle);


    }

    private void initiate(){
        topText = (TextView)findViewById(R.id.topText);
        bottomText = (TextView)findViewById(R.id.bottomText);
    }

    private class myHandler extends Handler {

    }

}
