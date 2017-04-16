package com.araara.mocho;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.araara.mocho.game.DataModel;
import com.araara.mocho.game.Monster;
import com.squareup.picasso.Picasso;

public class DetailGameActivity extends AppCompatActivity implements DetailMenu.OnMenuClickedListener {
    public static final String TAG = "DetailGameActivtity";
    int idx = -1;
    SharedPreferences sharedPreferences;
    Monster[] monsterList;
    AnimationDrawable frameAnimation;
    String parseMonsterString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_game);

        sharedPreferences = getSharedPreferences("MONSTERS", Context.MODE_PRIVATE);
        Log.d("DetailGame", sharedPreferences.getAll().toString());
        parseMonsterString = sharedPreferences.getString("OwnedMonster", "NONE");
        if (!parseMonsterString.equals("NONE"))
            monsterList = DataModel.parseMonster(parseMonsterString);
        Intent intent = getIntent();
        idx = intent.getIntExtra("idxmonster", -1);

        if (idx != -1) {
            TextView monstername = (TextView) findViewById(R.id.monster_name);
            ImageView bg = (ImageView) findViewById(R.id.cover_bg_details);
            monstername.setText(monsterList[idx].getName());
            Picasso.with(DetailGameActivity.this).load(R.drawable.bg).into(bg);

            ImageView img = (ImageView) findViewById(R.id.monster_idle);
            img.setImageResource(DataModel.idleanim[monsterList[idx].getMonsterId()-1]);

            frameAnimation = (AnimationDrawable) img.getDrawable();
            frameAnimation.start();
        }

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            DetailMenu menuFragment = new DetailMenu();
            Bundle bundle = new Bundle();
            bundle.putInt("subtype", monsterList[idx].getSubtype());
            bundle.putInt("hunger", monsterList[idx].getHunger());
            menuFragment.setArguments(bundle);
            Log.d(TAG, "Loaded fragment");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, menuFragment).commit();
            Log.d(TAG, "FragmentLoading");
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

    @Override
    public void onMenuClicked(String menu) {
        if (menu.equals("TRAIN")) {
            Intent intent = new Intent(this, SensorActivity.class);
            intent.putExtra("idxmonster", idx);
            startActivity(intent);
        } else if (menu.equals("UNLOCK")) {
            Intent intent = new Intent(this, LocationServiceActivity.class);
            intent.putExtra("idxmonster", idx);
            startActivity(intent);
        } else if (menu.equals("FEED")) {
            Intent intent = new Intent(this, FeedActivity.class);
            intent.putExtra("idxmonster", idx);
            startActivity(intent);
        }
    }
}
