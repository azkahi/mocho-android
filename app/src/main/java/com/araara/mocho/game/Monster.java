package com.araara.mocho.game;

/**
 * Created by Azka Hanif Imtiyaz on 2/20/2017.
 */

public class Monster {
    private String name;
    private String type;
    private String subtype;
    private int hunger;
    private int maxHP;
    private int maxSP;
    private int exp;
    private int offense;
    private int defense;
    private int recovery;

    public Monster(String name, String type, String subtype, int hunger, int maxHP, int maxSP, int exp, int offense, int defense, int recovery) {
        this.name = name;
        this.type = type;
        this.subtype = subtype;
        this.hunger = hunger;
        this.maxHP = maxHP;
        this.maxSP = maxSP;
        this.exp = exp;
        this.offense = offense;
        this.defense = defense;
        this.recovery = recovery;
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

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getMaxSP() {
        return maxSP;
    }

    public void setMaxSP(int maxSP) {
        this.maxSP = maxSP;
    }

    public int getExp() {
        return exp;
    }

    public void increaseExp(int exp) {
        this.exp = this.exp + exp;
        if
    }

    public int getOffense() {
        return offense;
    }

    public void setOffense(int offense) {
        this.offense = offense;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getRecovery() {
        return recovery;
    }

    public void setRecovery(int recovery) {
        this.recovery = recovery;
    }
}
