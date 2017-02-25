package com.araara.mocho.game;

import com.araara.mocho.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Azka Hanif Imtiyaz on 2/24/2017.
 */

public class DataModel {
    public static String[] monsters = {
            "Puta",
            "Ina"
    };

    public static int[] initHP = {
            100, 100
    };

    public static int[] initSP = {
            100, 100
    };

    public static int[] initAtk = {
            10, 20
    };

    public static int[] initDef = {
            20, 10
    };

    public static int[] initRec = {
            5, 10
    };

    public static int cover[] = {
            R.drawable.puta,
            R.drawable.ina
    };

    public static int background[] ={
            R.drawable.bg,
            R.drawable.bg
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
            for (int i = 0; i < ArrayMonster.length(); i++) {
                int id = ArrayMonster.getJSONObject(i).getInt("id");
                String name = ArrayMonster.getJSONObject(i).getString("name");
                int idxInit = getIdxMonster(name);
                int addedAtk = ArrayMonster.getJSONObject(i).getInt("addedAtk") - initAtk[idxInit];
                int addedDef = ArrayMonster.getJSONObject(i).getInt("addedDef") - initDef[idxInit];
                int addedRec = ArrayMonster.getJSONObject(i).getInt("addedRec") - initRec[idxInit];
                int addedHP = ArrayMonster.getJSONObject(i).getInt("addedHP") - initHP[idxInit];
                int addedSP = ArrayMonster.getJSONObject(i).getInt("addedSP") - initSP[idxInit];
                int exp = ArrayMonster.getJSONObject(i).getInt("exp");
                int hunger = ArrayMonster.getJSONObject(i).getInt("hunger");
                monsterList[i] = new Monster(name, name, name, hunger,
                        addedHP, addedSP, exp, addedAtk, addedDef, addedRec, id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return monsterList;
    }

    public static int getIdxMonster (String monstername) {
        if (monstername.equals("Puta")) {
            return 0;
        } else if (monstername.equals("Ina")) {
            return 1;
        } else {
            return -1;
        }
    }
}
