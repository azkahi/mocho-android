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
        if (!parseMonsterString.equals("NONE"))
            monstersList = DataModel.parseMonster(parseMonsterString);
        Intent intent = getIntent();
        idx = intent.getIntExtra("idxmonster", -1);

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

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            DetailMenu menuFragment = new DetailMenu();
            Bundle bundle = new Bundle();
            bundle.putInt("subtype", monstersList[idx].getSubtype());
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
//        Fragment fragment;
//        if (menu.equals("TRAIN")) {
//            fragment = new SensorMenuFragment();
//        } else if(menu.equals("FEED")) {
//            fragment = new AccelerometerFragment();
//        } else {
//            // Insert fragment map here
//            fragment = new GyroFragment();
//        }
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
