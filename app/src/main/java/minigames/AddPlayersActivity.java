package minigames;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christoffer.foffesdrickaspelv3.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AddPlayersActivity extends MyAppCompat {

    private static ArrayAdapter<String> players;
    private ListView listView;
    private Context addPlayerContext;
    private boolean newGame;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplayers);
        addPlayerContext = this;

        bundle = getIntent().getExtras();



        if(bundle != null) {
            newGame = bundle.getBoolean("newGame");
            if (newGame) {
                players = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
            }
        }

        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(players);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String playerToRemove = players.getItem(position);

                Toast.makeText(addPlayerContext, playerToRemove + " was removed.", Toast.LENGTH_SHORT).show();
                players.remove(playerToRemove);
            }
        });

        TextView textView = (TextView)findViewById(R.id.addPlayerText);
        textView.setOnEditorActionListener(new MyEditorActionListener());
        setBackgroundImage("woodpanel", (ImageView)findViewById(R.id.imageView));
    }





    public void onButtonPress_addPlayer(View v){
        TextView tf = (TextView)findViewById(R.id.addPlayerText);
        String s = tf.getText().toString();
        if (players.getPosition(s) >= 0) {
            Toast myToast = Toast.makeText(
                    getApplicationContext(), "Name already exists", Toast.LENGTH_LONG);
            myToast.show();
        }  else if(s.length()>0) {
            players.add(s);
            Toast myToast = Toast.makeText(
                    getApplicationContext(), "Player " + s + " registered", Toast.LENGTH_LONG);
            myToast.show();
        }else {
            Toast myToast = Toast.makeText(
                    getApplicationContext(), "Please enter a name", Toast.LENGTH_LONG);
            myToast.show();
        }
        tf.setText("");
    }

    public void onButtonPress_done(View v){
        //Intent intent = new Intent(this, DrinksActivity.class);
        if(newGame) {
            if (players.getCount() >= 2) {
                Intent intent = new Intent(this, PickAGameActivity.class);
                startActivity(intent);
            } else {
                Toast noPlayersToast = Toast.makeText(getApplicationContext(), "Atleast 2 players", Toast.LENGTH_LONG);
                noPlayersToast.show();
            }
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static ArrayList<String> getPlayers(){
        ArrayList<String> newList = new ArrayList<>();
        if(players != null) {
            for (int i = 0; i < players.getCount(); i++) {
                newList.add(players.getItem(i));
            }
        }
        return newList;
    }


    private class MyEditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId== EditorInfo.IME_ACTION_NEXT  || actionId == EditorInfo.IME_ACTION_DONE){
                onButtonPress_addPlayer(null);
            }
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (!newGame)
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
