package com.araara.mocho.game;

import java.io.Serializable;

/**
 * Created by Azka Hanif Imtiyaz on 2/20/2017.
 */

public class Monster implements Serializable {
    private String name;
    private String type;
    private int subtype;
    private int hunger;
    private int HP;
    private int SP;
    private int exp;
    private int attack;
    private int defense;
    private int recovery;
    private int id;

    public int getId() {
        return id;
    }

    public Monster(String name, String type, int subtype, int hunger, int HP, int SP, int exp, int attack, int defense, int recovery, int id) {
        this.name = name;
        this.type = type;
        this.subtype = subtype;
        this.hunger = hunger;
        this.HP = HP;
        this.SP = SP;
        this.exp = exp;
        this.attack = attack;
        this.defense = defense;
        this.recovery = recovery;
        this.id = id;
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

    public int getSubtype() {
        return subtype;
    }

    public void setSubtype(int subtype) {
        this.subtype = subtype;
    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getSP() {
        return SP;
    }

    public void setSP(int SP) {
        this.SP = SP;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
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
