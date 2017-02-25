package com.araara.mocho;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.araara.mocho.game.LocationData;

public class SubtypeActivity extends AppCompatActivity {
    private TextView tvDesc;
    private TextView tvTitle;
    private TextView tvBonus;
    private Button btnUnlock;
    private ImageView imgLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subtype);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvBonus = (TextView) findViewById(R.id.tvBonus);
        btnUnlock = (Button) findViewById(R.id.btnUnlock);
        imgLocation = (ImageView) findViewById(R.id.imgLocation);

        Intent intent = getIntent();
        String location = intent.getStringExtra("loc");
        for (int i = 0; i < LocationData.length; i++) {
            if (LocationData.names[i].equals(location)) {
                tvDesc.setText(LocationData.descs[i]);
                tvTitle.setText(LocationData.names[i]);
                tvBonus.setText(LocationData.attrs[i] + " +" + LocationData.amounts[i]);
                imgLocation.setImageResource(LocationData.images[i]);
            }
        }
    }
}
