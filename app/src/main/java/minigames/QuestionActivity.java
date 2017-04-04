package minigames;

import android.os.Bundle;

import com.example.christoffer.foffesdrickaspelv3.R;

/**
 * Created by Christoffer on 2016-04-15.
 */
public class QuestionActivity extends EmptyMiniGame {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questiongame);
        setBackgroundImage("question");
        init();
    }

}
