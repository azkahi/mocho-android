package com.araara.mocho;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Log;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
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
        getWindow().setSharedElementEnterTransition(enterTransition());
        getWindow().setSharedElementReturnTransition(returnTransition());
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

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //We are passing Bundle to activity, these lines will animate when we laucnh activity


                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(GameActivity.this, Pair.create(view, "selectedMonster"));

                Transition transition =  TransitionInflater.from(GameActivity.this).
                        inflateTransition(R.transition.card_exit);

                Intent intent = new Intent(GameActivity.this,DetailGameActivity.class);
                //intent.putExtra("cover", monsterList[i].getName());
                //ViewGroup mRoot = (ViewGroup) findViewById(R.id.scene_root);

                //Scene mAScene = Scene.getSceneForLayout(mRoot, R.layout.activity_game, GameActivity.this);
                //Scene mAnotherScene = Scene.getSceneForLayout(mRoot, R.layout.activity_detail_game, GameActivity.this);
                //TransitionManager.go(mAnotherScene, transition);
                startActivity(intent, options.toBundle());


            }
        });
    }

    private Transition enterTransition() {
        ChangeBounds bounds = new ChangeBounds();
        bounds.setDuration(2000);

        return bounds;
    }

    private Transition returnTransition() {
        ChangeBounds bounds = new ChangeBounds();
        bounds.setInterpolator(new DecelerateInterpolator());
        bounds.setDuration(2000);

        return bounds;
    }
}
