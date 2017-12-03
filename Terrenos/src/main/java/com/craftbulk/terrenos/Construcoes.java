/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.craftbulk.terrenos;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 *
 * @author diego
 */
public class Construcoes {

    public void pequeno(Location loc) {
        //constroi cercas para um terreno pequeno em cima da area
        for (int i = 0; i < 15; i++) {
            Location loc1 = new Location(loc.getWorld(), loc.getX() + 7.0D - i, loc.getY(), loc.getZ() - 7.0D);
            loc1.getBlock().setType(Material.FENCE);
            Location loc2 = new Location(loc.getWorld(), loc.getX() + 7.0D, loc.getY(), loc.getZ() - 7.0D + i);
            loc2.getBlock().setType(Material.FENCE);
            Location loc3 = new Location(loc.getWorld(), loc.getX() - 7.0D, loc.getY(), loc.getZ() + 7.0D - i);
            loc3.getBlock().setType(Material.FENCE);
            Location loc4 = new Location(loc.getWorld(), loc.getX() - 7.0D + i, loc.getY(), loc.getZ() + 7.0D);
            loc4.getBlock().setType(Material.FENCE);
        }
    }

    public void medio(Location loc) {
        //constroi cercas para um terreno medio em cima da area
        for (int i = 0; i < 19; i++) {
            Location loc1 = new Location(loc.getWorld(), loc.getX() + 9.0D - i, loc.getY(), loc.getZ() - 9.0D);
            loc1.getBlock().setType(Material.FENCE);
            Location loc2 = new Location(loc.getWorld(), loc.getX() + 9.0D, loc.getY(), loc.getZ() - 9.0D + i);
            loc2.getBlock().setType(Material.FENCE);
            Location loc3 = new Location(loc.getWorld(), loc.getX() - 9.0D, loc.getY(), loc.getZ() + 9.0D - i);
            loc3.getBlock().setType(Material.FENCE);
            Location loc4 = new Location(loc.getWorld(), loc.getX() - 9.0D + i, loc.getY(), loc.getZ() + 9.0D);
            loc4.getBlock().setType(Material.FENCE);
        }2
    }

    public void grande(Location loc) {
        //constroi cercas para um terreno grande em cima da area
        for (int i = 0; i < 29; i++) {
            Location loc1 = new Location(loc.getWorld(), loc.getX() + 14.0D - i, loc.getY(), loc.getZ() - 14.0D);
            loc1.getBlock().setType(Material.FENCE);
            Location loc2 = new Location(loc.getWorld(), loc.getX() + 14.0D, loc.getY(), loc.getZ() - 14.0D + i);
            loc2.getBlock().setType(Material.FENCE);
            Location loc3 = new Location(loc.getWorld(), loc.getX() - 14.0D, loc.getY(), loc.getZ() + 14.0D - i);
            loc3.getBlock().setType(Material.FENCE);
            Location loc4 = new Location(loc.getWorld(), loc.getX() - 14.0D + i, loc.getY(), loc.getZ() + 14.0D);
            loc4.getBlock().setType(Material.FENCE);
        }
    }

}
