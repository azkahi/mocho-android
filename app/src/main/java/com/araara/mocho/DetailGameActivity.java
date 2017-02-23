package com.araara.mocho;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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

        Picasso.with(this).load(R.drawable.bg).into(bg);
        Picasso.with(this).load(R.drawable.puta).into(monster);
        monstername.setText(intent.getStringExtra("cover"));
    }
}
