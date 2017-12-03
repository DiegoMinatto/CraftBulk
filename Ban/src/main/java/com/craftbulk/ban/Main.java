/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.craftbulk.ban;

import com.craftbulk.ban.Comandos.Ban;
import com.craftbulk.ban.Comandos.Kick;
import com.craftbulk.ban.Comandos.UnBan;
import com.craftbulk.ban.ConfigManager.Config;
import com.craftbulk.ban.Eventos.onPlayerJoin;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author diego
 */
public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Config cnfg = new Config(this);
        cnfg.inicializar();
        getServer().getPluginCommand("ban").setExecutor(new Ban(this));
        getServer().getPluginCommand("kick").setExecutor(new Kick(this));
        getServer().getPluginCommand("unban").setExecutor(new UnBan());
        getServer().getPluginManager().registerEvents(new onPlayerJoin(this), this);
        super.onEnable(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDisable() {
        super.onDisable(); //To change body of generated methods, choose Tools | Templates.
    }

 
    
    
    
}
