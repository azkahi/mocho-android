package com.araara.mocho;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.araara.mocho.game.DataModel;
import com.araara.mocho.game.Monster;
import com.squareup.picasso.Picasso;

public class DetailGameActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Monster[] monstersList;
    AnimationDrawable frameAnimation;
    String parseMonsterString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_game);

        sharedPreferences = getSharedPreferences("MONSTERS", Context.MODE_PRIVATE);

        Log.d("DetailGame", sharedPreferences.getAll().toString());

        parseMonsterString = sharedPreferences.getString("OwnedMonster", "NONE");
        if (!parseMonsterString.equals("NONE")) monstersList = DataModel.parseMonster(parseMonsterString);

        Intent intent = getIntent();
        int idx = intent.getIntExtra("idxmonster", -1);

        if (idx != -1) {
            int idxmonster = DataModel.getIdxMonster(monstersList[idx].getName());

            TextView monstername = (TextView) findViewById(R.id.monster_name);
            ImageView bg = (ImageView) findViewById(R.id.cover_bg_details);

            Log.d("Monster name", DataModel.monsters[idxmonster]);


            monstername.setText(DataModel.monsters[idxmonster]);

            Picasso.with(DetailGameActivity.this).load(DataModel.background[idxmonster]).into(bg);



            // Load the ImageView that will host the animation and
            // set its background to our AnimationDrawable XML resource.
            ImageView img = (ImageView) findViewById(R.id.monster_idle);
            img.setImageResource(DataModel.idleanim[idxmonster]);

            // Get the background, which has been compiled to an AnimationDrawable object.
            frameAnimation = (AnimationDrawable) img.getDrawable();

            // Start the animation (looped playback by default).
            frameAnimation.start();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent intent = new Intent(DetailGameActivity.this, GameActivity.class);
        intent.putExtra("MONSTER", parseMonsterString);
        startActivity(intent);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    }

}
