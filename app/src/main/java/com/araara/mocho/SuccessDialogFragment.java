package com.araara.mocho;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by ranggarmaste on 2/25/17.
 */

public class SuccessDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String message = bundle.getString("message");
        final String state = bundle.getString("state");
        final String fail = bundle.getString("fail");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (fail == null) {
            builder.setTitle("Success")
                    .setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (state == null) {
                                Intent intent = new Intent(getActivity(), GameActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }
                    });
            // Create the AlertDialog object and return it
        } else {
            builder.setTitle("Failed")
                    .setMessage("Oops, your monster is so full, it puked its entire food it ate. Your monster's hunger is now 0!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (state == null) {
                                Intent intent = new Intent(getActivity(), GameActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }
                    });
            // Create the AlertDialog object and return it
        }
        return builder.create();
    }
}
