/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.craftbulk.ban.Eventos;

import com.craftbulk.ban.ConfigManager.Config;
import com.craftbulk.ban.Main;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author diego
 */
public class onPlayerJoin implements Listener {
  private final Main plugin;
  
  public onPlayerJoin(Main main)
  {
    this.plugin = main;
  }
  @EventHandler
  public void onJoin(PlayerJoinEvent evt){
     Player p = evt.getPlayer();
      if(Config.getBanYml().contains(p.getName().toUpperCase())){
          if(Config.getBanYml().getBoolean(p.getName().toUpperCase() + ".Permanente") == true){
          if(Config.getBanYml().contains(p.getName().toUpperCase() + ".Reset") && Config.getBanYml().getBoolean(p.getName().toUpperCase() + ".Reset") == true){    
               String banidor = Config.getBanYml().getString(p.getName().toUpperCase() + ".Player");
                String motivo = Config.getBanYml().getString(p.getName().toUpperCase() + ".Motivo");
               Kick(p, ChatColor.RED + "Você praticou ações inadimissiveis, sua conta foi resetada e todo seu progresso foi perdido.\nVocê poderá criar uma nova conta.\nPor: " +banidor+" Motivo: " + motivo );
              //00
          }else{
              String banidor = Config.getBanYml().getString(p.getName().toUpperCase() + ".Player");
                 String motivo = Config.getBanYml().getString(p.getName().toUpperCase() + ".Motivo");
                 Kick(p, ChatColor.RED + "Você foi banido permanentemente.\nPor: " +banidor+" Motivo: " + motivo );
              //permanente
          }
          }else{  
              if(Config.getBanYml().contains(p.getName().toUpperCase() + ".DataUnban")){
                //unban dado  
              }else{
               String banidor = Config.getBanYml().getString(p.getName().toUpperCase() + ".Player");
                  String motivo = Config.getBanYml().getString(p.getName().toUpperCase() + ".Motivo");
                  String datatermino = Config.getBanYml().getString(p.getName().toUpperCase() + ".DataTermino");
                  DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss 'De' dd/MM/yyyy");
                  Date date = new Date();
                  String data = dateFormat.format(date); 
                  Date ban = null;
              try {
                  ban = dateFormat.parse(datatermino);
                  date = dateFormat.parse(data);
              } catch (ParseException ex) {
                  Logger.getLogger(onPlayerJoin.class.getName()).log(Level.SEVERE, null, ex);
              }
              if(date.after(ban)){
                  Config.getBanYml().set(p.getName().toUpperCase(), null);
                  Config.getBanYml().set(p.getName().toUpperCase() + ".DataUnban",  dateFormat.format(date));
                  Config.salvar();
              }else{
                  Kick(p, ChatColor.DARK_RED + "Você foi banido.\nPor: " +banidor +" Motivo: "  + motivo + "\nSeu ban acaba em: " + datatermino );
              }
                  //tempo
          }
          }      
              
      }
      
  }
  public void Kick(Player p,String mensagem){
      p.kickPlayer(mensagem);
  }
    
}
