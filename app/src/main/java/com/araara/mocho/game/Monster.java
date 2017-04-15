package com.araara.mocho.game;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Azka Hanif Imtiyaz on 2/20/2017.
 */

public class Monster implements Serializable {
    private int id;
    private int monsterId;
    private String name;
    private String type;
    private int addedAtk;
    private int addedDef;
    private int addedRec;
    private int addedHP;
    private int addedSP;
    private int totalAtk;
    private int totalDef;
    private int totalRec;
    private int totalHP;
    private int totalSP;
    private int exp;
    private int level;
    private int hunger;
    private int subtype;

    public Monster(JSONObject ownedMonster) {
        try {
            JSONObject monster = ownedMonster.getJSONObject("Monster");
            id = ownedMonster.getInt("id");
            monsterId = monster.getInt("id");
            name = ownedMonster.getString("name");
            type = monster.getString("type");
            exp = ownedMonster.getInt("exp");
            level = exp / 100;
            addedAtk = ownedMonster.getInt("addedAtk");
            addedDef = ownedMonster.getInt("addedDef");
            addedHP = ownedMonster.getInt("addedHP");
            addedSP = ownedMonster.getInt("addedSP");
            addedRec = ownedMonster.getInt("addedRec");
            totalAtk = calculateStatus(ownedMonster, monster, "Atk", level);
            totalDef = calculateStatus(ownedMonster, monster, "Def", level);;
            totalRec = calculateStatus(ownedMonster, monster, "Rec", level);;
            totalHP = calculateStatus(ownedMonster, monster, "HP", level);;
            totalSP = calculateStatus(ownedMonster, monster, "SP", level);;
            hunger = ownedMonster.getInt("hunger");
            subtype = ownedMonster.getInt("subtype");
        } catch (Exception e) {

        }
    }

    public int calculateStatus(JSONObject ownedMonster, JSONObject monster, String status, int level) {
        try {
            return ownedMonster.getInt("added" + status)
                    + monster.getInt("init" + status)
                    + monster.getInt("incr" + status) * (level - 1);
        } catch (Exception e) {
            return 0;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonsterId() {
        return monsterId;
    }

    public void setMonsterId(int monsterId) {
        this.monsterId = monsterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotalAtk() {
        return totalAtk;
    }

    public void setTotalAtk(int totalAtk) {
        this.totalAtk = totalAtk;
    }

    public int getTotalDef() {
        return totalDef;
    }

    public void setTotalDef(int totalDef) {
        this.totalDef = totalDef;
    }

    public int getTotalRec() {
        return totalRec;
    }

    public int getAddedAtk() {
        return addedAtk;
    }

    public void setAddedAtk(int addedAtk) {
        this.addedAtk = addedAtk;
    }

    public int getAddedDef() {
        return addedDef;
    }

    public void setAddedDef(int addedDef) {
        this.addedDef = addedDef;
    }

    public int getAddedRec() {
        return addedRec;
    }

    public void setAddedRec(int addedRec) {
        this.addedRec = addedRec;
    }

    public int getAddedHP() {
        return addedHP;
    }

    public void setAddedHP(int addedHP) {
        this.addedHP = addedHP;
    }

    public int getAddedSP() {
        return addedSP;
    }

    public void setAddedSP(int addedSP) {
        this.addedSP = addedSP;
    }

    public void setTotalRec(int totalRec) {
        this.totalRec = totalRec;

    }

    public int getTotalHP() {
        return totalHP;
    }

    public void setTotalHP(int totalHP) {
        this.totalHP = totalHP;
    }

    public int getTotalSP() {
        return totalSP;
    }

    public void setTotalSP(int totalSP) {
        this.totalSP = totalSP;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public int getSubtype() {
        return subtype;
    }

    public void setSubtype(int subtype) {
        this.subtype = subtype;
    }
}
