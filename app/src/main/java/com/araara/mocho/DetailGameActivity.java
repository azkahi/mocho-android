package com.araara.mocho;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_game);


        Intent intent = getIntent();
        CardView cardView = (CardView) findViewById(R.id.card_view);
        ImageView monster = (ImageView) findViewById(R.id.monster_details);
        TextView monstername = (TextView) findViewById(R.id.monster_name);
        ImageView bg = (ImageView) findViewById(R.id.cover_bg_details);
        TextView desc = (TextView) findViewById(R.id.txt_monster_desc);

        //These are lines helping Details_Card To Animate
        //===============================================
        AnimatorSet animationSet = new AnimatorSet();

        //Translating Details_Card in Y Scale
        ObjectAnimator card_y = ObjectAnimator.ofFloat(cardView, View.TRANSLATION_Y, 70);
        card_y.setDuration(2500);
        card_y.setRepeatMode(ValueAnimator.REVERSE);
        card_y.setRepeatCount(ValueAnimator.INFINITE);
        card_y.setInterpolator(new LinearInterpolator());

        //Translating Movie_Cover in Y Scale
        ObjectAnimator cover_y = ObjectAnimator.ofFloat(monster, View.TRANSLATION_Y, 30);
        cover_y.setDuration(3000);
        cover_y.setRepeatMode(ValueAnimator.REVERSE);
        cover_y.setRepeatCount(ValueAnimator.INFINITE);
        cover_y.setInterpolator(new LinearInterpolator());

        animationSet.playTogether(card_y,cover_y);
        animationSet.start();

        bg.setImageResource(R.drawable.bg);
        monster.setImageResource(R.drawable.puta);
        //Picasso.with(this).load(R.drawable.bg).into(bg);
        //Picasso.with(this).load(R.drawable.puta).into(monster);
        monstername.setText(intent.getStringExtra("cover"));
    }
}
