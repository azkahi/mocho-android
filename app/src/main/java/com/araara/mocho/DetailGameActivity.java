package com.araara.mocho;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailGameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSharedElementEnterTransition(enterTransition());
        getWindow().setSharedElementReturnTransition(returnTransition());
        setContentView(R.layout.activity_detail_game);

        //Intent intent = getIntent();
        CardView cardView = (CardView) findViewById(R.id.card_view);
        ImageView monster = (ImageView) findViewById(R.id.monster_details);
        TextView monstername = (TextView) findViewById(R.id.monster_name);
        ImageView bg = (ImageView) findViewById(R.id.cover_bg_details);
        TextView desc = (TextView) findViewById(R.id.txt_monster_desc);

        // Load the ImageView that will host the animation and
        // set its background to our AnimationDrawable XML resource.
        ImageView img = (ImageView)findViewById(R.id.monster_idle);
        img.setImageResource(R.drawable.inaidle);

        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getDrawable();

        // Start the animation (looped playback by default).
        frameAnimation.start();
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
