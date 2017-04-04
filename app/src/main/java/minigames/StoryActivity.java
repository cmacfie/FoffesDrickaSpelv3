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

public class StoryActivity extends EmptyMiniGame {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        setBackgroundImage("story");
        init();
    }

}
