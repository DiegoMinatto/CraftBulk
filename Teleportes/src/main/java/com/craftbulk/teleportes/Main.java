package com.craftbulk.teleportes;

//importes
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
//importes

public class Main extends JavaPlugin  {

//variaveis publicas
    public BukkitScheduler sh = Bukkit.getScheduler();
    public static Economy economy = null;
//variaveis publicas
//ao habilitar

    @Override
    public void onEnable() {
        super.onEnable(); //To change body of generated methods, choose Tools | Templates.
        if (!setupEconomy()) {
            Bukkit.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", new Object[]{getDescription().getName()}));
            getServer().getPluginManager().disablePlugin(this);
        }
    }
//ao desabititar
    @Override
    public void onDisable() {
        super.onDisable(); //To change body of generated methods, choose Tools | Templates.
    }
//verificando plugin economy
  

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = (Economy) economyProvider.getProvider();
        }
        return economy != null;
    }
    //hashtimer
    public ArrayList<String> players = new ArrayList();
    public ArrayList<Date> datahora = new ArrayList();
    public HashMap<Player, Integer> taskdl = new HashMap();
    public HashMap<Player, Integer> taskdl2 = new HashMap();

    //hashtimer
    //comandos
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("Teleporte")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 0) {
                    p.sendMessage(ChatColor.DARK_RED + "Oops!, a sintaxe do seu comando está incorreta!");
                    p.sendMessage(ChatColor.DARK_RED + "Uso correto: /teleporte <solicitar> ou <aceitar> e <jogador>");
                    return true;
                }
                if (args[0].equalsIgnoreCase("Solicitar")) {
                    if (args.length == 1) {
                        p.sendMessage(ChatColor.DARK_RED + "Oops!, a sintaxe do seu comando está incorreta!");
                        p.sendMessage(ChatColor.DARK_RED + "Uso correto: /teleporte solicitar <jogador>");
                        return true;
                    }
                    if (Bukkit.getPlayer(args[1]) == null) {
                        p.sendMessage(ChatColor.DARK_RED + "O player " + ChatColor.RED + args[1] + ChatColor.DARK_RED + " não está online no momento!");
                        return true;
                    }
                    Player alvo = Bukkit.getPlayer(args[1]);
                    if (alvo == p) {
                        p.sendMessage(ChatColor.DARK_RED + "Você não pode solicitar teleporte a você mesmo!");
                        return true;
                    }
                    if (!(alvo.hasPermission("Teleporte.Aceitar"))) {
                        p.sendMessage(ChatColor.DARK_RED + "O player " + ChatColor.RED + args[1] + ChatColor.DARK_RED + " não pode aceitar seu pedido de teleporte!");
                        return true;
                    }
                   
                        Date date = new Date();
                    for (int i = 0; i < datahora.size(); i++) {
                        if (datahora.get(0).before(date)) {
                            datahora.remove(0);
                            players.remove(0);
                        }
                    }
                   if(players.contains(p.getName() + "_" + alvo.getName())){
                       p.sendMessage(ChatColor.DARK_RED + "Você já enviou uma solicitação para este player, aguarde o tempo acabar para enviar uma novamente!");
                       return true;
                   } 
                        
                    
                    EconomyResponse econ = economy.withdrawPlayer(p, 100);
                    if (!(econ.transactionSuccess())) {
                        p.sendMessage(ChatColor.DARK_RED + "Você precisa de " + ChatColor.RED + "100 Coin(s)" + ChatColor.DARK_RED + "para mandar uma solicitação de teleporte!");
                        return true;
                    }
                    //timer enviar
                    final Player pf = p;
                    if (this.taskdl.containsKey(p) || this.taskdl2.containsKey(p)) {
                        p.sendMessage(ChatColor.DARK_RED + "Aguarde para enviar uma nova solicitação!");
                        return true;
                    }
                    //timer enviar
                    p.sendMessage(ChatColor.GOLD + "Aguarde 5 segundos!");
                    int taskID = this.sh.scheduleSyncDelayedTask(this, () -> {
                        Date date2 = new Date();
                        date2.setSeconds(date2.getSeconds() + 10);
                        p.sendMessage(ChatColor.GOLD + "Pedido de teleporte enviado!");
                        alvo.sendMessage(ChatColor.GOLD + "O player " + p.getName() + " deseja ir até você para aceitar digite /teleporte aceitar " + p.getName()+"!");
                        players.add(p.getName() + "_" + alvo.getName());
                        datahora.add(date2);
                        Main.this.sh.cancelTask((Main.this.taskdl.get(pf)));
                        Main.this.taskdl.remove(pf);
                    }, 100);
                    this.taskdl.put(p, taskID);
                    //timer ant-flod
                    int taskIDc = this.sh.scheduleSyncDelayedTask(this, () -> {
                        Main.this.sh.cancelTask((Main.this.taskdl2.get(pf)));
                        Main.this.taskdl2.remove(pf);
                    }, 200);
                    this.taskdl2.put(p, taskIDc);
                } else if (args[0].equalsIgnoreCase("Aceitar")) {
                    if (args.length == 1) {
                        p.sendMessage(ChatColor.DARK_RED + "Oops!, a sintaxe de seu comando está incorreta");
                        p.sendMessage(ChatColor.DARK_RED + "Uso correto: /teleporte aceitar <Jogador>");
                        return true;
                    }
                    if (!(p.hasPermission("Teleporte.Aceitar"))) {
                        p.sendMessage(ChatColor.DARK_RED + "Você não tem permissão para aceitar um pedido de teleporte!");
                        return true;
                    }
                    if (Bukkit.getPlayer(args[1]) == null) {
                        p.sendMessage(ChatColor.DARK_RED + "O player " + ChatColor.RED + args[1] + ChatColor.DARK_RED + " não está online no momento!");
                        return true;
                    }
                    Player alvo = Bukkit.getPlayer(args[1]);

                    Date date = new Date();
                    for (int i = 0; i < datahora.size(); i++) {
                        if (datahora.get(0).before(date)) {
                            datahora.remove(0);
                            players.remove(0);
                        }

                    }
                    if (!(players.contains(alvo.getName() + "_" + p.getName()))) {
                        p.sendMessage(ChatColor.DARK_RED + "O player " + ChatColor.RED + alvo.getName() + ChatColor.DARK_RED + " não lhe solicitou teleporte!");
                        return true;
                    }
                    datahora.remove(players.indexOf(alvo.getName() + "_" + p.getName()));
                    players.remove(alvo.getName() + "_" + p.getName());
                    alvo.teleport(p);
                    alvo.sendMessage(ChatColor.GOLD + "O player " + p.getName() + " aceitou seu pedido de teleporte!");
                    p.sendMessage(ChatColor.GOLD + "O player " + alvo.getName() + " foi teleportado até você!");

                }
            } else {
                sender.sendMessage(ChatColor.DARK_RED + "Este comando somente pode ser utilizado in-game!");
                return true;
            }
        }
        return true;
    }

}
