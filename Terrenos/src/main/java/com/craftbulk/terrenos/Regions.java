/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.craftbulk.terrenos;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.Iterator;
import static org.bukkit.Bukkit.getServer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author diego
 */
public class Regions {

    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        return (WorldGuardPlugin) plugin;
    }

    public boolean gerarRegion(Player player, Location locmin, Location locmax, String nomeregiao) {
        BlockVector min = new BlockVector(locmin.getX(), locmin.getY() + 256.0D, locmin.getZ());
        BlockVector max = new BlockVector(locmax.getX(), locmax.getY() - 256.0D, locmax.getZ());
        ProtectedCuboidRegion region = new ProtectedCuboidRegion(nomeregiao, new BlockVector(min), new BlockVector(max));
        DefaultDomain owners = new DefaultDomain();
        owners.addPlayer(getWorldGuard().wrapPlayer(player));

        region.setOwners(owners);

        getWorldGuard().getRegionManager(player.getWorld()).addRegion(region);
        try {
            getWorldGuard().getRegionManager(player.getWorld()).save();
            player.sendMessage(ChatColor.GOLD + "Parabéns!, você acaba de adiquirir um terreno com sucesso!");
        } catch (StorageException e) {
            player.sendMessage(ChatColor.DARK_RED + "Oops, algo não parece estar funcionando corretamente avise algum menbro da staff!");
            return true;
        }

        return true;
    }

    public boolean procimity(Location locmin, Location locmax, Player player) {
        String nomeregiao = "Verificacao";
        BlockVector min = new BlockVector(locmin.getX(), locmin.getY() + 256.0D, locmin.getZ());
        BlockVector max = new BlockVector(locmax.getX(), locmax.getY() - 256.0D, locmax.getZ());
        ProtectedCuboidRegion region = new ProtectedCuboidRegion(nomeregiao, new BlockVector(min), new BlockVector(max));
        RegionManager regionManager = getWorldGuard().getRegionManager(player.getWorld());
        ApplicableRegionSet set = regionManager.getApplicableRegions(region);
        Iterator localIterator = set.iterator();
        if (localIterator.hasNext()) {
            player.sendMessage(ChatColor.DARK_RED + "Oops, Já existe uma região neste local, tente comprar em uma localização mais distante!");
            return true;
        }

        return false;
    }

}
