package com.araara.mocho.game;

import com.araara.mocho.R;

/**
 * Created by ranggarmaste on 2/25/17.
 */

public class LocationData {
    public static int length = 3;
    public static String[] names = {"Labtek V", "Kantin Bengkok", "Gerbang Depan"};
    public static String[] attrs = {"ATK", "DEF", "HP"};
    public static String[] descs = {
            "Only warriors who have extreme bravery can survive in this place.",
            "Once were a famous place for people to sit together. Now it's a shitty place.",
            "The entrance to the most historical university in the world."
    };
    public static int[] amounts = {20, 30, 100};
    public static int[] images = {R.drawable.labtek_v, R.drawable.bengkok, R.drawable.gerbang_itb};
}
