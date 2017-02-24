package com.araara.mocho;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class SensorMenuFragment extends Fragment {
    private ImageButton atkButton;
    private ImageButton defButton;
    private OnMenuClickedListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnMenuClickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnMenuClickedListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor_menu, container, false);
        atkButton = (ImageButton) view.findViewById(R.id.btnAtk);
        defButton = (ImageButton) view.findViewById(R.id.btnDef);

        atkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMenuClicked("ATK");
            }
        });
        defButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMenuClicked("DEF");
            }
        });

        return view;
    }

    public interface OnMenuClickedListener {
        void onMenuClicked(String trainingType);
    }
}
