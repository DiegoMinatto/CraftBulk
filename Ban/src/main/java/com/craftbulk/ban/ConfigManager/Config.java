/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.craftbulk.ban.ConfigManager;

import com.craftbulk.ban.Main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author diego
 */
public class Config {
   private Main plugin = null;
   private static FileConfiguration BanYml;
   private static File BanFile;
  
  public Config(Main main)
  {
    this.plugin = main;
  }   
    public static FileConfiguration getBanYml() {
        return BanYml;
    }
    
  public void inicializar(){
  BanFile = new File(plugin.getDataFolder() + "/Bans.yml");
  BanYml = YamlConfiguration.loadConfiguration(BanFile);
       try {
           BanYml.save(BanFile);
       } catch (IOException ex) {
           Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
       }
  }
  
  public static void salvar(){
      try {
           BanYml.save(BanFile);
       } catch (IOException ex) {
           Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
       }   
  }

}
