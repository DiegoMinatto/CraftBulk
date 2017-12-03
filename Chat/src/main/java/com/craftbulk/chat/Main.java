package com.craftbulk.chat;

import java.util.ArrayList;
import java.util.HashMap;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Main
        extends JavaPlugin
        implements Listener {
    ArrayList<String> plr = new ArrayList();
    ArrayList<String> alv = new ArrayList();

    static SimpleClans simpleClansHook;
    public static Economy econ = null;
    public BukkitScheduler sh = Bukkit.getScheduler();
    public HashMap<Player, Integer> taskglobal = new HashMap();
    public HashMap<Player, Integer> tasklocal = new HashMap();

    public static Chat chat = null;
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        return rsp != null;
    }

    @Override
    public void onEnable() {
        simpleClansHook = (SimpleClans) Bukkit.getServer().getPluginManager().getPlugin("SimpleClans");
        Bukkit.getPluginManager().registerEvents(this, this);
        setupChat();
        super.onEnable();
         if (!setupEconomy()) {
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServicesManager().getRegistration(Chat.class);
        chat = (Chat) rsp.getProvider();
        return chat != null;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    public void chatFormat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        if(tasklocal.containsKey(p)){
            p.sendMessage(ChatColor.DARK_RED + "Aguarde para utilizar o chat local novamente!");
            event.setCancelled(true);
            return;
        }
        onChatTagManager onchat = new onChatTagManager(p);
        getServer().getPluginManager().callEvent(onchat);
        String tags = onchat.getTag();

        for (Player alvo : event.getRecipients()) {
            if ((alvo.getWorld() == p.getWorld()) && (alvo.getLocation().distance(p.getLocation()) <= 40.0D)) {
                String prefix;
                String cla;
                if (chat.getPlayerPrefix(p).equals("")) {
                    prefix = "";
                } else {
                    prefix = chat.getPlayerPrefix(p) + "§8." ;
                }
                try {
                    cla = simpleClansHook.getClanManager().getClanPlayer(p).getClan().getTagLabel();
                } catch (NullPointerException npe) {
                    cla = "";
                }
                String msg = event.getMessage();
                
                if(p.hasPermission("chat.cores")){
                    if(msg.toLowerCase().contains("&k")){
                  msg = msg.replace("&k", "");
                }
                 if(msg.toLowerCase().contains("&l")){
                  msg = msg.replace("&l", "");
                }
                  if(msg.toLowerCase().contains("&m")){
                  msg = msg.replace("&m", "");
                }
                   if(msg.toLowerCase().contains("&n")){
                  msg = msg.replace("&n", "");
                }
                    if(msg.toLowerCase().contains("&o")){
                  msg = msg.replace("&o", "");
                }
                     if(msg.toLowerCase().contains("&r")){
                  msg = msg.replace("&r", "");
                }
                    
                     msg = ChatColor.translateAlternateColorCodes('&', msg);
                }else{
                     msg = event.getMessage();
                }

                alvo.sendMessage(ChatColor.GOLD + "[L] " + ChatColor.translateAlternateColorCodes('&', tags) + ChatColor.translateAlternateColorCodes('&', prefix) + cla + ChatColor.WHITE + p.getDisplayName() + ChatColor.DARK_GRAY + ": " + ChatColor.WHITE + msg.toLowerCase());
            }
        }
           event.getRecipients().clear();
        if(p.hasPermission("chat.staff")){
            return ;
        }
     
               final Player pf = p;
       int taskID = this.sh.scheduleSyncDelayedTask(this, () -> {
                        Main.this.sh.cancelTask((Main.this.tasklocal.get(pf)));
                        Main.this.tasklocal.remove(pf);
                    }, 20);
                    this.tasklocal.put(p,taskID);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "Este comando apenas pode ser utilizado in-game!");
            return true;
        }
        Player p = (Player) sender;
        if (command.getName().equalsIgnoreCase("G")) {
            if (args.length == 0) {
                p.sendMessage(ChatColor.DARK_RED + "Oops, a sintaxe de seu comando est� incorreta!");
                p.sendMessage(ChatColor.DARK_RED + "Uso correto: /G <Mensagem>");
                return true;
            }
            if(taskglobal.containsKey(p)){
             p.sendMessage(ChatColor.DARK_RED + "Aguarde para utilizar o chat global novamente");
             return true;
            }
               RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        econ = (Economy) rsp.getProvider();
        EconomyResponse transacao = econ.withdrawPlayer(p, 0.50D);
        if(transacao.transactionSuccess()){
            p.sendMessage(ChatColor.GREEN + "Voce pagou 50 coin(s) para utilizar o chat global!");
        }else{
            p.sendMessage(ChatColor.DARK_RED + "Você precisa de 30 coin(s)"
                    + " para falar no global!");
            
            return true;
        }
            String msg = "";
            for (int i = 0; i < args.length; i++) {
                msg = msg + args[i] + " ";
            }
            String prefix;

            if (chat.getPlayerPrefix(p).equals("")) {
                prefix = "";
            } else {
                prefix = chat.getPlayerPrefix(p) + "§8.";
            }
            onChatTagManager onchat = new onChatTagManager(p);
            getServer().getPluginManager().callEvent(onchat);
            String tags = onchat.getTag();
            String cla;

            try {
                cla = simpleClansHook.getClanManager().getClanPlayer(p).getClan().getTagLabel();

            } catch (NullPointerException npe) {
                cla = "";
            }
                if(p.hasPermission("chat.cores")){
                if(msg.toLowerCase().contains("&k")){
                  msg = msg.replace("&k", "");
                }
                 if(msg.toLowerCase().contains("&l")){
                  msg = msg.replace("&l", "");
                }
                  if(msg.toLowerCase().contains("&m")){
                  msg = msg.replace("&m", "");
                }
                   if(msg.toLowerCase().contains("&n")){
                  msg = msg.replace("&n", "");
                }
                    if(msg.toLowerCase().contains("&o")){
                  msg = msg.replace("&o", "");
                }
                     if(msg.toLowerCase().contains("&r")){
                  msg = msg.replace("&r", "");
                }
                     msg = ChatColor.translateAlternateColorCodes('&', msg);
                }
         
       Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[G] " + ChatColor.translateAlternateColorCodes('&', tags) + ChatColor.translateAlternateColorCodes('&', prefix) + cla + ChatColor.WHITE + p.getDisplayName() + ChatColor.DARK_GRAY + ": " + ChatColor.WHITE + msg.toLowerCase());
     if(p.hasPermission("chat.staff")){
            return true;
        }
       final Player pf = p;
       int taskID = this.sh.scheduleSyncDelayedTask(this, () -> {
                        Main.this.sh.cancelTask((Main.this.taskglobal.get(pf)));
                        Main.this.taskglobal.remove(pf);
                    }, 100);
                    this.taskglobal.put(p,taskID);
        }
        if (command.getName().equalsIgnoreCase("Mp")) {
            if (args.length < 2) {
                p.sendMessage(ChatColor.DARK_RED + "Oops, a sintaxe de seu comando est� incorreta!");
                p.sendMessage(ChatColor.DARK_RED + "Uso correto: /MP <Player> <Mensagem>");
                return true;
            }
            if (getServer().getPlayer(args[0]) == null) {
                p.sendMessage(ChatColor.DARK_RED + "O player " + ChatColor.RED + args[0] + ChatColor.DARK_RED + " não está online no momento!");
                return true;
            }
            Player alvo = getServer().getPlayer(args[0]);
            if(alvo == p){
                p.sendMessage(ChatColor.DARK_RED + "Você não pode mandar uma mensagem privada para você mesmo!");
                return true;
            }
            String msg = "";
            for (int i = 1; i < args.length; i++) {
                msg = msg + args[i] + " ";
            }
            if (p.hasPermission("chat.staff") || alvo.hasPermission("chat.staff")) {
                //administrador
                alvo.sendMessage(ChatColor.DARK_GRAY + "[" +ChatColor.DARK_AQUA + "Mp" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_AQUA + "Mensagem de " + p.getName() + ChatColor.DARK_GRAY +": " +ChatColor.DARK_AQUA + msg.toLowerCase());
                p.sendMessage(ChatColor.DARK_GRAY + "[" +ChatColor.DARK_AQUA + "Mp" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_AQUA + "Mensagem para " + alvo.getName() + ChatColor.DARK_GRAY +": " +ChatColor.DARK_AQUA + msg.toLowerCase());
               
                
            } else {
                //normal
               p.sendMessage(ChatColor.DARK_GRAY + "[" +ChatColor.DARK_GREEN + "Mp" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_GREEN + "Mensagem para " + alvo.getName() + ChatColor.DARK_GRAY +": " +ChatColor.DARK_GREEN + msg.toLowerCase());
               alvo.sendMessage(ChatColor.DARK_GRAY + "[" +ChatColor.DARK_GREEN + "Mp" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_GREEN + "Mensagem de " + p.getName() + ChatColor.DARK_GRAY +": " +ChatColor.DARK_GREEN + msg.toLowerCase());
            }
            if(plr.contains(p.getName())){
                for (int i = 0; i < plr.size(); i++) {
                    if(plr.get(i).equals(p.getName())){
                 alv.set(i, alvo.getName());
                    }
                }
            }else{
            plr.add(p.getName());
            alv.add(alvo.getName());
            }
              if(plr.contains(alvo.getName())){
                for (int i = 0; i < plr.size(); i++) {
                      if(plr.get(i).equals(alvo.getName())){
                 alv.set(i, p.getName());
                      }
                }
            }else{
            plr.add(alvo.getName());
            alv.add(p.getName());
            }
            
            

        }
        if(command.getName().equalsIgnoreCase("r")){
            if(args.length == 0){
             p.sendMessage(ChatColor.DARK_RED + "Oops, a sintaxe de seu comando esta incorreta!");
                p.sendMessage(ChatColor.DARK_RED + "Uso correto: /r <Mensagem>");
                return true;   
            }
            if(!(alv.contains(p.getName()) && plr.contains(p.getName()))){
                p.sendMessage(ChatColor.DARK_RED + "Você não possui nenhuma conversa para responder!");
                return true;
            }
             String msg = "";
            for (int i = 0; i < args.length; i++) {
                msg = msg + args[i] + " ";
            }
            for (int i = 0; i < alv.size(); i++) {
                if(alv.get(i).equals(p.getName())){
                    if(getServer().getPlayer(plr.get(i)) == null){
                    p.sendMessage(ChatColor.DARK_RED + "O player " + ChatColor.RED + plr.get(i) + ChatColor.DARK_RED + " não está online no momento!");
                    return true;
                    }
                    Player alvo = getServer().getPlayer(plr.get(i));
                   if (p.hasPermission("chat.staff") || alvo.hasPermission("chat.staff")) {
                //administrador
                alvo.sendMessage(ChatColor.DARK_GRAY + "[" +ChatColor.DARK_AQUA + "Mp" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_AQUA + "Mensagem de " + p.getName() + ChatColor.DARK_GRAY +": " +ChatColor.DARK_AQUA + msg.toLowerCase());
                p.sendMessage(ChatColor.DARK_GRAY + "[" +ChatColor.DARK_AQUA + "Mp" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_AQUA + "Mensagem para " + alvo.getName() + ChatColor.DARK_GRAY +": " +ChatColor.DARK_AQUA + msg.toLowerCase());
               
                
            } else {
                //normal
               p.sendMessage(ChatColor.DARK_GRAY + "[" +ChatColor.DARK_GREEN + "Mp" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_GREEN + "Mensagem para " + alvo.getName() + ChatColor.DARK_GRAY +": " +ChatColor.DARK_GREEN + msg.toLowerCase());
               alvo.sendMessage(ChatColor.DARK_GRAY + "[" +ChatColor.DARK_GREEN + "Mp" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_GREEN + "Mensagem de " + p.getName() + ChatColor.DARK_GRAY +": " +ChatColor.DARK_GREEN + msg.toLowerCase());
            }
                         for (int i2 = 0; i2 < plr.size(); i2++) {
                      if(plr.get(i2).equals(alvo.getName())){
                 alv.set(i2, p.getName());
                      }
                }
                   
                 
                }
            }
        }
        return true;
    }
}
