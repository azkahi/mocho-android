package com.araara.mocho.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        if (convertview == null) {
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

        // Calculating current stats
        int level = monsterList[i].getLevel();
        int HP = monsterList[i].getTotalHP();
        int SP = monsterList[i].getTotalSP();
        int Atk = monsterList[i].getTotalAtk();
        int Def = monsterList[i].getTotalDef();
        int Rec = monsterList[i].getTotalRec();
        String desc = "Level: " + level + "\nHP: " + HP + " SP: " + SP + "\nAtk: " + Atk + " Def: " + Def + " Rec: " + Rec;

        // Setup Holder
        holder._txt1.setText("Hunger");
        holder._txt2.setText("EXP");

        holder._monsterdetail.setText(monsterList[i].getName());
        holder._monsterdesc.setText(desc);

        holder._hunger.setMax(500);
        holder._hunger.setProgress(monsterList[i].getHunger());

        holder._exp.setMax(100);
        holder._exp.setProgress(monsterList[i].getLevel());

        Picasso.with(context).load(DataModel.cover[i]).into(holder._cover);
        Picasso.with(context).load(R.drawable.bg).into(holder._background);
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
