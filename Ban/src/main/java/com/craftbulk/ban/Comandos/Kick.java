/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.craftbulk.ban.Comandos;

import com.craftbulk.ban.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author diego
 */
public class Kick  implements CommandExecutor{
  private final Main plugin;
  
  public Kick(Main main)
  {
    this.plugin = main;
  }
  
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String str, String[] strings) {
            if(!(cs instanceof Player)){
           cs.sendMessage("Comando apenas in-game");
           return true;
       }
       Player p = (Player)cs;
       if(!(p.hasPermission("Ban.Kick"))){
           p.sendMessage(ChatColor.DARK_RED + "Você não tem permissão para utilizar este comando!");
           return true;
       }
       if(cmnd.getName().equalsIgnoreCase("Kick")){
           if(strings.length < 2){
           p.sendMessage(ChatColor.DARK_RED + "Uso correto: /Kick <Player> <Motivo>");
           return true;
           }
           Player alvo = Bukkit.getPlayer(strings[0]);
           if(alvo == null){
               p.sendMessage(ChatColor.DARK_RED + "O player " + strings[0] + " está offline no momento!");
               return true;
           }
           String msg = "";
           for (int i = 1; i < strings.length; i++) {
                if(i == strings.length-1){
                           msg = msg + strings[i];   
                       }else{
                         msg = msg + strings[i] + " ";     
                       }
           }
          alvo.kickPlayer(ChatColor.RED + "Você foi kikado!\nPor: " + p.getName() + " Motivo: " + msg);
    }
    return true; 
    }
}
  


