package com.araara.mocho;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;

import com.araara.mocho.game.DataModel;
import com.araara.mocho.game.Monster;
import com.araara.mocho.game.MonsterAdapter;

import org.json.JSONArray;
import org.json.JSONException;

public class GameActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Monster[] monsterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        sharedPreferences = getSharedPreferences("MONSTERS", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("OwnedMonster", intent.getStringExtra("MONSTER"));
        editor.apply();

        monsterList = DataModel.parseMonster(intent.getStringExtra("MONSTER"));

        String temp = sharedPreferences.getString("OwnedMonster", "Nothing");
        Log.d("InsidePrefs", sharedPreferences.getAll().toString());
        Log.d("TestPrefs", temp);
        ListView lv = (ListView) findViewById(R.id.listView);
        MonsterAdapter adapter = new MonsterAdapter(GameActivity.this, monsterList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(GameActivity.this, DetailGameActivity.class);
                intent.putExtra("idxmonster", i);
                startActivity(intent);
            }
        });
    }
}
