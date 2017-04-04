package minigames;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.christoffer.foffesdrickaspelv3.R;

public class MainMenuActivity extends MyAppCompat {

    private boolean resumable;
    private int minDrink = 4, maxDrink = 7;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        resumable = false;

        setBackgroundImage("woodpanel", (ImageView)findViewById(R.id.backgroundImage));

        Bundle b = getIntent().getExtras();
        if(b != null) {
            resumable = b.getBoolean("resumeable");
            String source = b.getString("source");
            if(source != null && source.equals("settings")){
                minDrink = b.getInt("minDrink");
                maxDrink = b.getInt("maxDrink");
            }
            if (!resumable) {
                (findViewById(R.id.resumeGameHolder)).setVisibility(View.GONE);
            } else {
                (findViewById(R.id.resumeGameHolder)).setVisibility(View.VISIBLE);
            }
        }
    }

    public void onClick_newGame(View v){
        Intent intent = new Intent(this, AddPlayersActivity.class);
        intent.putExtra("newGame", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void onClick_resumeGame(View v){
        finish();
    }

    public void onClick_settings(View v){
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("newGame", !resumable);
        startActivity(intent);
        finish();
    }
}
