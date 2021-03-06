package com.araara.mocho;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;


public class DetailMenu extends Fragment {
    private ImageButton trainButton;
    private ImageButton feedButton;
    private ImageButton unlockButton;
    private int subtype;
    private int hunger;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnMenuClickedListener mListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_menu, container, false);
        trainButton = (ImageButton) view.findViewById(R.id.btnTrain);
        feedButton = (ImageButton) view.findViewById(R.id.btnFeed);
        unlockButton = (ImageButton) view.findViewById(R.id.btnUnlock);
        subtype = getArguments().getInt("subtype");
        hunger = getArguments().getInt("hunger");
        if (subtype == 1) {
            unlockButton.setBackgroundColor(Color.RED);
        }
        if (hunger < 50) {
            trainButton.setBackgroundColor(Color.RED);
        }

        trainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hunger < 50) {
                    Toast.makeText(getActivity(), "Your monster is too weak to train. Feed it first!", Toast.LENGTH_SHORT).show();
                } else {
                    mListener.onMenuClicked("TRAIN");
                }
            }
        });
        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMenuClicked("FEED");
            }
        });
        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subtype == 1) {
                    Toast.makeText(getActivity(), "You have already unlocked this monster's subtype", Toast.LENGTH_SHORT).show();
                } else {
                    mListener.onMenuClicked("UNLOCK");
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnMenuClickedListener) context;
        } catch (ClassCastException e){
            throw new RuntimeException(context.toString()
                    + " must implement OnMenuClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMenuClickedListener {
        void onMenuClicked(String trainingType);
    }
}
