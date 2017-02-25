package com.araara.mocho.game;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.araara.mocho.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Azka Hanif Imtiyaz on 2/24/2017.
 */

public class MonsterAdapter extends BaseAdapter {
    private Context context;
    private Monster[] monsterList;
    private LayoutInflater inflater = null;


    public MonsterAdapter(Context con, Monster[] monsterList) {
        this.monsterList = monsterList;
        this.context = con;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return monsterList.length;
    }

    @Override
    public Object getItem(int i) {
        return monsterList[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertview, ViewGroup viewGroup) {
        ViewHolder holder;
        if(convertview == null) {

            convertview =inflater.inflate(R.layout.card_item_template,null);
            holder = new ViewHolder();

            holder._cover = (ImageView) convertview.findViewById(R.id.img_cover_d);
            holder._background = (ImageView) convertview.findViewById(R.id.img_background);
            holder._monsterdetail = (TextView) convertview.findViewById(R.id.txt_monster_details);
            holder._monsterdesc = (TextView) convertview.findViewById(R.id.txt_monster_desc);
            holder._hunger = (ProgressBar) convertview.findViewById(R.id.progressBar);
            holder._exp = (ProgressBar) convertview.findViewById(R.id.progressBar2);
            holder._txt1 = (TextView) convertview.findViewById(R.id.textView);
            holder._txt2 = (TextView) convertview.findViewById(R.id.textView2);

            convertview.setTag(holder);

        } else {
            holder = (ViewHolder) convertview.getTag();
        }

        int j = i;
        int idxInitMonster = 0;
        int idxMonster = 0;
            for (int k = 0; k < DataModel.monsters.length; k++){
                if (monsterList[j].getName().equals(DataModel.monsters[k])){
                    idxInitMonster = k;
                    idxMonster = j;
                    break;
                }
            }

        // Calculating current stats
        int HP = DataModel.initHP[idxInitMonster] + monsterList[idxMonster].getHP();
        int SP = DataModel.initSP[idxInitMonster] + monsterList[idxMonster].getSP();
        int Atk = DataModel.initAtk[idxInitMonster] + monsterList[idxMonster].getAttack();
        int Def = DataModel.initDef[idxInitMonster] + monsterList[idxMonster].getDefense();
        int Rec = DataModel.initRec[idxInitMonster] + monsterList[idxMonster].getRecovery();
        String desc = "HP: " + HP + " SP: " + SP + "\nAtk: " + Atk + " Def: " + Def + " Rec: " + Rec;

        // Setup Holder
        holder._txt1.setText("Hunger");
        holder._txt2.setText("EXP");
        holder._monsterdetail.setText(DataModel.monsters[idxInitMonster]);
        holder._monsterdesc.setText(desc);
        holder._hunger.setMax(500);
        holder._hunger.setProgress(monsterList[idxMonster].getHunger());
        holder._exp.setMax(100);
        holder._exp.setProgress(monsterList[idxMonster].getExp() % 100);
        //holder._cover.setImageResource(DataModel.cover[idxInitMonster]);
        Picasso.with(context).load(DataModel.cover[idxInitMonster]).into(holder._cover);
        //holder._background.setImageResource(DataModel.background[0]);
        Picasso.with(context).load(DataModel.background[0]).into(holder._background);



        return convertview;
    }

    public class ViewHolder {

        TextView _txt1;
        TextView _txt2;
        ImageView _cover;
        ImageView _background;
        TextView _monsterdetail;
        TextView _monsterdesc;
        ProgressBar _hunger;
        ProgressBar _exp;


    }
}
