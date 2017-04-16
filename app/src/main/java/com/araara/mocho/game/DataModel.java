package com.araara.mocho.game;

import android.util.Log;

import com.araara.mocho.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Azka Hanif Imtiyaz on 2/24/2017.
 */

public class DataModel {
    private static final String TAG = "DataModel";
    public static int cover[] = {
            R.drawable.puta,
            R.drawable.ina
    };

    public static int idleanim[] ={
            R.drawable.putaidle,
            R.drawable.inaidle
    };

    public static Monster[] parseMonster (String Jarr) {
        Monster[] monsterList = new Monster[0];
        try {
            JSONArray ArrayMonster = new JSONArray(Jarr);
            monsterList = new Monster[ArrayMonster.length()];
            Log.d(TAG, "parseMonster: length: " + monsterList.length);
            for (int i = 0; i < ArrayMonster.length(); i++) {
                monsterList[i] = new Monster(ArrayMonster.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return monsterList;
    }

    public static String updateJSONdata (String Jarr, Monster mon, int id){
        String res = "";
        try {
            JSONArray ArrayMonster = new JSONArray(Jarr);
            JSONObject mJSON = ArrayMonster.getJSONObject(id);
            mJSON.put("hunger", mon.getHunger());
            mJSON.put("addedAtk", mon.getTotalAtk());
            mJSON.put("addedDef", mon.getTotalDef());
            res = ArrayMonster.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }
}
