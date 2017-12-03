/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.craftbulk.ban.Comandos;

import com.craftbulk.ban.ConfigManager.Config;
import com.craftbulk.ban.Main;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class Ban implements CommandExecutor {
  private final Main plugin;
  
  public Ban(Main main)
  {
    this.plugin = main;
  }
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String str, String[] args) {
       if(!(cs instanceof Player)){
           cs.sendMessage("Comando apenas in-game");
           return true;
       }
       Player p = (Player)cs;
       if(!(p.hasPermission("Ban.Ban"))){
           p.sendMessage(ChatColor.DARK_RED + "Você não tem permissão para utilizar este comando!");
           return true;
       }
       if(cmnd.getName().equalsIgnoreCase("Ban")){

           if(args.length < 2){
               p.sendMessage(ChatColor.DARK_RED + "Uso correto: /ban <Nick> <Tempo ou Permanente>");
               return true;
       }
           String alvo = args[0].toUpperCase();
           if(args[1].equalsIgnoreCase("tempo")){
               if(args.length < 4){
                   p.sendMessage(ChatColor.DARK_RED + "Uso correto: /ban <Nick> <Tempo> <Segundos> <Motivo>");
                   return true;
               }
               try{
              int segundos = Integer.parseInt(args[2]);
              if(Config.getBanYml().getBoolean(alvo + ".Permanente") == true){
                  p.sendMessage(ChatColor.DARK_RED + "Este player já esta banido permanentemente!");
                  return true;
              }
               DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss 'De' dd/MM/yyyy");
               Date date = new Date();
               date.setSeconds(date.getSeconds() + segundos);
                Config.getBanYml().set(alvo, null);
               Config.getBanYml().set(alvo + ".Permanente", false);
               Config.getBanYml().set(alvo + ".DataTermino", dateFormat.format(date));
               Config.getBanYml().set(alvo + ".Player", p.getName());
               String msg = "";
                   for (int i = 3; i < args.length; i++) {
                       if(i == args.length-1){
                           msg = msg + args[i];   
                       }else{
                         msg = msg + args[i] + " ";     
                       }
                      
                   }
               Config.getBanYml().set(alvo + ".Motivo", msg);
                Config.salvar();
               Player pa = Bukkit.getPlayer(args[0]);
               if(!(pa == null)){
                   pa.kickPlayer(ChatColor.RED + "Você foi banido.\nPor: " +p.getName()+" Motivo: " + msg + "\nSeu ban acaba em: " + dateFormat.format(date));
               }
               }catch(NumberFormatException nfe){
                   p.sendMessage(ChatColor.DARK_RED + "<Segundos> deve ser um numero inteiro!");
               }
               
           }else{
               if(args[1].equalsIgnoreCase("permanente")){
                     if(args.length < 3){
                   p.sendMessage(ChatColor.DARK_RED + "Uso correto: /ban <Nick> <Permanente> <Motivo>");
                   return true;
               }
               Config.getBanYml().set(alvo, null);
               Config.getBanYml().set(alvo + ".Permanente", true);
               Config.getBanYml().set(alvo + ".Player", p.getName());
               String msg = "";
                   for (int i = 2; i < args.length; i++) {
                      if(i == args.length-1){
                           msg = msg + args[i];   
                       }else{
                         msg = msg + args[i] + " ";     
                       }
                   }
               Config.getBanYml().set(alvo + ".Motivo", msg); 
               Config.salvar();
                   Player pa = Bukkit.getPlayer(args[0]);
               if(!(pa == null)){
                   pa.kickPlayer(ChatColor.RED + "Você foi banido permanentemente.\nPor: " +p.getName()+" Motivo: " + msg );
               }
               }else{
                   if(p.isOp()){
                       if(args[1].equalsIgnoreCase("00")){
                          if(p.isOp()){
                   if(args.length < 3){
                   p.sendMessage(ChatColor.DARK_RED + "Uso correto: /ban <Nick> <Permanente> <Motivo>");
                   return true;
               }
               Config.getBanYml().set(alvo, null);
               Config.getBanYml().set(alvo + ".Permanente", true);
               Config.getBanYml().set(alvo + ".Reset", true);
               Config.getBanYml().set(alvo + ".Player", p.getName());
               String msg = "";
                   for (int i = 2; i < args.length; i++) {
                  if(i == args.length-1){
                           msg = msg + args[i];   
                       }else{
                         msg = msg + args[i] + " ";     
                       }
                   }
               Config.getBanYml().set(alvo + ".Motivo", msg); 
               Config.salvar();   
                                  Player pa = Bukkit.getPlayer(args[0]);
               if(!(pa == null)){
                   pa.kickPlayer(ChatColor.RED + "Você praticou ações inadimissiveis, sua conta foi resetada e todo seu progresso foi perdido.\nVocê poderá criar uma nova conta.\nPor: " +p.getName()+" Motivo: " + msg);
               }
                          } 
                       }
                   }
               }
           }
      
    }
     return true;
    }
    
}
