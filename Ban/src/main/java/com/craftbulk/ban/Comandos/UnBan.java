/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.craftbulk.ban.Comandos;

import com.craftbulk.ban.ConfigManager.Config;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author diego
 */
public class UnBan implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String str, String[] args) {
        if(command.getName().equalsIgnoreCase("unban")){
            if(sender.isOp()){
            if(args.length == 0){
              sender.sendMessage(ChatColor.DARK_RED + "Uso correto: /unban <player>");
              return true;
            }
            String alvo = args[0].toUpperCase();
            if(Config.getBanYml().contains(alvo)){
                if(Config.getBanYml().contains(alvo + ".Reset") && Config.getBanYml().getBoolean(alvo + ".Reset") == true){    
                    sender.sendMessage(ChatColor.DARK_RED + "Esta conta foi resetada e não pode ser desbanida!");
                    return true;
                }
                 Config.getBanYml().set(alvo, null);
                   DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss 'De' dd/MM/yyyy");
                  Date date = new Date();
                  Config.getBanYml().set(alvo+ ".DataUnban", dateFormat.format(date));
                  Config.salvar();
                  sender.sendMessage(ChatColor.GOLD + "O player " + args[0] + " foi desbanido com sucesso!");
            }else{
                sender.sendMessage(ChatColor.DARK_RED + "Este player não está banido!");
            }
        }else{
                sender.sendMessage(ChatColor.DARK_RED + "Você não possui permissão!");
            }
        }
        return true;
    }
    
}
