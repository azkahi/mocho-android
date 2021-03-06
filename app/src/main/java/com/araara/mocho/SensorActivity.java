package com.araara.mocho;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class SensorActivity extends AppCompatActivity implements SensorMenuFragment.OnMenuClickedListener {
    private static final String TAG = "SensorActivity";
    int idx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        idx = getIntent().getIntExtra("idxmonster", 0);

        if (findViewById(R.id.fragment_container) != null) {
            Log.d(TAG, "onCreate: creating fragment");
            if (savedInstanceState != null) {
                return;
            }
            SensorMenuFragment menuFragment = new SensorMenuFragment();
            menuFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, menuFragment).commit();
        }
        Log.d(TAG, "onCreate: fragment container is null");
    }

    @Override
    public void onMenuClicked(String trainingType) {
        Fragment fragment;
        if (trainingType.equals("ATK")) {
            fragment = new GyroFragment();
        } else {
            fragment = new AccelerometerFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putInt("idxmonster", idx);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
