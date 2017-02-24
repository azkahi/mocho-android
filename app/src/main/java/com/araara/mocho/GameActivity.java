package com.araara.mocho;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.araara.mocho.game.DataModel;
import com.araara.mocho.game.Monster;
import com.araara.mocho.game.MonsterAdapter;

import org.json.JSONArray;
import org.json.JSONException;

public class GameActivity extends AppCompatActivity {
    private Monster[] monsterList;
    private static final String TAG = "GameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        try {
            JSONArray ArrayMonster = new JSONArray(intent.getStringExtra(HomeActivity.EXTRA_MESSAGE));
            monsterList = new Monster[ArrayMonster.length()];
            for (int i = 0; i < ArrayMonster.length(); i++) {
                String name = ArrayMonster.getJSONObject(i).getString("name");
                int addedAtk = ArrayMonster.getJSONObject(i).getInt("addedAtk");
                int addedDef = ArrayMonster.getJSONObject(i).getInt("addedDef");
                int addedRec = ArrayMonster.getJSONObject(i).getInt("addedRec");
                int addedHP = ArrayMonster.getJSONObject(i).getInt("addedHP");
                int addedSP = ArrayMonster.getJSONObject(i).getInt("addedSP");
                int exp = ArrayMonster.getJSONObject(i).getInt("exp");
                int hunger = ArrayMonster.getJSONObject(i).getInt("hunger");
                Log.d(TAG, name);
                Log.d(TAG, "" + addedDef);
                monsterList[i] = new Monster(name, name, name, hunger,
                        addedHP, addedSP, exp, addedAtk, addedDef, addedRec);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListView lv = (ListView) findViewById(R.id.listView);
        MonsterAdapter adapter = new MonsterAdapter(GameActivity.this, monsterList);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //We are passing Bundle to activity, these lines will animate when we laucnh activity
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(GameActivity.this,
                        Pair.create(view,"selectedMonster")
                ).toBundle();

                Intent intent = new Intent(GameActivity.this,DetailGameActivity.class);
                intent.putExtra("cover", monsterList[i].getName());
                startActivity(intent);

            }
        });
    }
}
