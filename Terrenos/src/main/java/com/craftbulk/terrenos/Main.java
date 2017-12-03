/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.craftbulk.terrenos;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.text.DecimalFormat;
import java.util.Iterator;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author diego
 */
public class Main extends JavaPlugin  {
//ao habilitar ao plugin

    public String nomeregiao;

    @Override
    public void onEnable() {

        //verificando vault
        if (!setupEconomy()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        //verificando worldedit e worldguard
        getWorldGuard();
        getWorldEdit();
        System.out.println("Plugin terrenos habilitado!");
       
    }
  

    @Override
    public void onDisable() {
        System.out.println("Plugin terrenos desabilitado");
    }
    //metodo de verificacao vault

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        return rsp != null;
    }
    //metodo de verificacao worldedit

    public WorldEditPlugin getWorldEdit() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if ((p instanceof WorldEditPlugin)) {
            return (WorldEditPlugin) p;
        }
        return null;
    }
    //metodo de verificacao worldguard

    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        if ((plugin == null) || (!(plugin instanceof WorldGuardPlugin))) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }
    //metodo de comandos

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("Terreno")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.DARK_RED + "Este comando somente pode ser utilizado in-game!");
            }
            Player p = (Player) sender;
            if (!p.hasPermission("terrenos.*")) {
                p.sendMessage(ChatColor.DARK_RED + "Oops, parece que você não possui permição para utilizar este comando!");
                return true;
            }
                if(!p.getWorld().getName().equalsIgnoreCase("world")){
                p.sendMessage(ChatColor.DARK_RED + "Você não pode comprar um terreno aqui!");
                return true;
            }
            Vault vault = new Vault();
            Regions region = new Regions();
            Construcoes construcoes = new Construcoes();
            if (args.length == 0) {
                //player somente digitou /terreno
                p.sendMessage(ChatColor.DARK_RED + "Oops, a sintaxe de seu comando está incorreta!");
                p.sendMessage(ChatColor.DARK_RED + "Uso correto: /Terreno <comprar, amigos, deletar>!");
                return true;
            }
            if (args[0].equalsIgnoreCase("Comprar")) {
                if (args.length <= 1) {
                    p.sendMessage(ChatColor.DARK_RED + "Oops, a sintaxe de seu comando está incorreta!");
                    p.sendMessage(ChatColor.DARK_RED + "Opções disponiveis: pequeno, medio, grande ou pvp!");
                    return true;
                }
                if (args[1].equalsIgnoreCase("Pequeno")) {
                    DecimalFormat df2 = new DecimalFormat("###");
                    Location locmax = new Location(p.getWorld(), Double.valueOf(df2.format(p.getLocation().getX())) + 7.0D, Double.valueOf(df2.format(p.getLocation().getY())), Double.valueOf(df2.format(p.getLocation().getZ())) - 7.0D);
                    Location locmin = new Location(p.getWorld(), Double.valueOf(df2.format(p.getLocation().getX())) - 7.0D, Double.valueOf(df2.format(p.getLocation().getY())), Double.valueOf(df2.format(p.getLocation().getZ())) + 7.0D);
                    if (!regionmax(p)) {
                        if (!region.procimity(locmin, locmax, p)) {
                            if (vault.descontar(p, 1500D)) {
                                region.gerarRegion(p, locmin, locmax, nomeregiao);
                                Location loccerca = new Location(p.getWorld(), Double.valueOf(df2.format(p.getLocation().getX())), Double.valueOf(df2.format(p.getLocation().getY())), Double.valueOf(df2.format(p.getLocation().getZ())));
                                construcoes.pequeno(loccerca);
                            } else {
                                p.sendMessage(ChatColor.DARK_RED + "Você não possui money suficiente!");
                            }
                        }
                    }

                } else if (args[1].equalsIgnoreCase("Medio")) {

                    DecimalFormat df2 = new DecimalFormat("###");
                    Location locmax = new Location(p.getWorld(), Double.valueOf(df2.format(p.getLocation().getX())) + 9.0D, Double.valueOf(df2.format(p.getLocation().getY())), Double.valueOf(df2.format(p.getLocation().getZ())) - 9.0D);
                    Location locmin = new Location(p.getWorld(), Double.valueOf(df2.format(p.getLocation().getX())) - 9.0D, Double.valueOf(df2.format(p.getLocation().getY())), Double.valueOf(df2.format(p.getLocation().getZ())) + 9.0D);
                    if (!regionmax(p)) {
                        if (!region.procimity(locmin, locmax, p)) {
                            if (vault.descontar(p, 2000D)) {
                                region.gerarRegion(p, locmin, locmax, nomeregiao);
                                Location loccerca = new Location(p.getWorld(), Double.valueOf(df2.format(p.getLocation().getX())), Double.valueOf(df2.format(p.getLocation().getY())), Double.valueOf(df2.format(p.getLocation().getZ())));
                                construcoes.medio(loccerca);
                            } else {
                                p.sendMessage(ChatColor.DARK_RED + "Você não possui money suficiente!");
                            }
                        }
                    }

                } else if (args[1].equalsIgnoreCase("Grande")) {

                    DecimalFormat df2 = new DecimalFormat("###");
                    Location locmax = new Location(p.getWorld(), Double.valueOf(df2.format(p.getLocation().getX())) + 14.0D, Double.valueOf(df2.format(p.getLocation().getY())), Double.valueOf(df2.format(p.getLocation().getZ())) - 14.0D);
                    Location locmin = new Location(p.getWorld(), Double.valueOf(df2.format(p.getLocation().getX())) - 14.0D, Double.valueOf(df2.format(p.getLocation().getY())), Double.valueOf(df2.format(p.getLocation().getZ())) + 14.0D);
                    if (!regionmax(p)) {
                        if (!region.procimity(locmin, locmax, p)) {
                            if (vault.descontar(p, 3000D)) {
                                region.gerarRegion(p, locmin, locmax, nomeregiao);
                                Location loccerca = new Location(p.getWorld(), Double.valueOf(df2.format(p.getLocation().getX())), Double.valueOf(df2.format(p.getLocation().getY())), Double.valueOf(df2.format(p.getLocation().getZ())));
                                construcoes.grande(loccerca);
                            } else {
                                p.sendMessage(ChatColor.DARK_RED + "Você não possui money suficiente!");
                            }
                        }
                    }

                } else if (args[1].equalsIgnoreCase("PvP")) {
                    if (args.length > 1) {
                        RegionManager regionManager = getWorldGuard().getRegionManager(p.getWorld());
                        ApplicableRegionSet set = regionManager.getApplicableRegions(p.getLocation());
                        for (ProtectedRegion r : set) {
                            ProtectedRegion regions = set.iterator().next();
                            if (regions.getOwners().contains(p.getUniqueId())) {

                                if (args[2].equalsIgnoreCase("Ligado")) {
                                    if (!(regions.getFlag(DefaultFlag.PVP) == StateFlag.State.ALLOW || regions.getFlag(DefaultFlag.PVP) == null)) {

                                        if (vault.descontar(p, 1000D)) {
                                            //pvp on 
                                            regions.setFlag(DefaultFlag.PVP, StateFlag.State.ALLOW);
                                        
                                            try {
                                                regionManager.save();
                                            } catch (StorageException ex) {
                                                p.sendMessage(ChatColor.DARK_RED + "Oops, algo parece estar errado informe algum menbro da staff!");

                                            }
                                          
                                            p.sendMessage(ChatColor.GREEN + "Agora seu terreno está com PvP ligado.");
                                        } else {
                                            p.sendMessage(ChatColor.DARK_RED + "Para desativar o PvP de sua região você precisa de 30.000 coin(s)!");
                                        }

                                    } else {
                                        p.sendMessage(ChatColor.DARK_RED + "O pvp de sua regiao já esta ativado para alterar use o comando /terreno comprar pvp desligado!");
                                    }
                                } else if (args[2].equalsIgnoreCase("Desligado")) {
                                    if (!(regions.getFlag(DefaultFlag.PVP) == StateFlag.State.DENY)) {
                                        if (vault.descontar(p, 30000D)) {
                                            regions.setFlag(DefaultFlag.PVP, StateFlag.State.DENY);
                                            try {
                                                regionManager.save();
                                            } catch (StorageException ex) {
                                                p.sendMessage(ChatColor.DARK_RED + "Oops, algo parece estar errado informe algum menbro da staff!");
                                            }
                                            p.sendMessage(ChatColor.GREEN + "Agora seu terreno está com PvP desligado.");
                                            //pvp off
                                        } else {
                                            p.sendMessage(ChatColor.DARK_RED + "Para desativar o PvP de sua região você precisa de 30.000 coin(s)!");
                                        }

                                    } else {
                                        p.sendMessage(ChatColor.DARK_RED + "O pvp de sua regiao já esta desativado para alterar use o comando /terreno comprar pvp ligado!");
                                    }
                                } else {
                                    p.sendMessage(ChatColor.DARK_RED + "Oops, a sintaxe de seu comando está incorreta!");
                                    p.sendMessage(ChatColor.DARK_RED + "Opções disponiveis: ligado ou desligado!");
                                }

                                return true;
                            } else {
                                p.sendMessage(ChatColor.DARK_RED + "Você deve estar em seu terreno para fazer isso!");
                                return true;
                            }

                        }
                        p.sendMessage(ChatColor.DARK_RED + "Você não está dentro de um terreno!");
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Oops, a sintaxe de seu comando está incorreta!");
                        p.sendMessage(ChatColor.DARK_RED + "Opções disponiveis: ligado ou desligado!");
                    }

                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Oops, a sintaxe de seu comando está incorreta!");
                    p.sendMessage(ChatColor.DARK_RED + "Opções disponiveis: pequeno, medio, grande ou pvp!");
                }
            } else if (args[0].equalsIgnoreCase("Amigos")) {
                if (args.length <= 1) {
                    p.sendMessage(ChatColor.DARK_RED + "Oops, a sintaxe de seu comando está incorreta!");
                    p.sendMessage(ChatColor.DARK_RED + "Opções disponiveis: Adicionar, Remover ou Listar!");
                    return true;
                }
                if (args[1].equalsIgnoreCase("Adicionar")) {
                    if (args.length >= 3) {
                        //adiciona 
                        RegionManager regionManager = getWorldGuard().getRegionManager(p.getWorld());
                        ApplicableRegionSet set = regionManager.getApplicableRegions(p.getLocation());
                        Iterator<ProtectedRegion> rm = set.iterator();
                        if (rm.hasNext()) {
                            ProtectedRegion regions = (ProtectedRegion) rm.next();
                            if (regions.getOwners().contains(p.getName())) {
                                regions.getMembers().addPlayer(args[2]);
                            }
                            p.sendMessage(ChatColor.GREEN + args[2] + " foi adicionado a sua lista de amigos com sucesso!");
                            return true;
                        }
                        p.sendMessage(ChatColor.DARK_RED + "Você deve estar em seu terreno para fazer isso!");
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Oops! a sintaxe de seu comando está incorreta!");
                        p.sendMessage(ChatColor.DARK_RED + "Uso correto: /terreno amigos adicionar <nome>");
                    }

                } else if (args[1].equalsIgnoreCase("Remover")) {
                    if (args.length >= 3) {
                        //remove 
                        RegionManager regionManager = getWorldGuard().getRegionManager(p.getWorld());
                        ApplicableRegionSet set = regionManager.getApplicableRegions(p.getLocation());
                        Iterator<ProtectedRegion> rm = set.iterator();
                        if (rm.hasNext()) {
                            ProtectedRegion regions = (ProtectedRegion) rm.next();
                            if (regions.getOwners().contains(p.getName())) {
                                regions.getMembers().removePlayer(args[2]);
                            }
                            p.sendMessage(ChatColor.GREEN + args[2] + " foi removido da sua lista de amigos com sucesso!");
                            return true;
                        }
                        p.sendMessage(ChatColor.DARK_RED + "Você deve estar em seu terreno para fazer isso!");

                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Oops! a sintaxe de seu comando está incorreta!");
                        p.sendMessage(ChatColor.DARK_RED + "Uso correto: /terreno amigos remover <nome>");
                    }

                } else if (args[1].equalsIgnoreCase("Listar")) {
                    RegionManager regionManager = getWorldGuard().getRegionManager(p.getWorld());
                    ApplicableRegionSet set = regionManager.getApplicableRegions(p.getLocation());
                    for (ProtectedRegion regions : set) {
                        if (regions.getOwners().contains(p.getUniqueId())) {
                            p.sendMessage(ChatColor.GREEN + regions.getMembers().getPlayers().toString());
                            return true;
                        }
                    }
                    p.sendMessage(ChatColor.DARK_RED + "Você deve estar em seu terreno para fazer isso!");
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Oops, a sintaxe de seu comando está incorreta!");
                    p.sendMessage(ChatColor.DARK_RED + "Opções disponiveis: Adicionar, Remover ou Listar!");
                }

            } else if (args[0].equalsIgnoreCase("Deletar")) {
                RegionManager regionManager = getWorldGuard().getRegionManager(p.getWorld());
                ApplicableRegionSet set = regionManager.getApplicableRegions(p.getLocation());
                for (ProtectedRegion regions : set) {
                    if (regions.getOwners().contains(p.getUniqueId())) {
                        regionManager.removeRegion(regions.getId());
                        p.sendMessage(ChatColor.GREEN + "Terreno deletado com sucesso!");
                        return true;
                    }
                }
                p.sendMessage(ChatColor.DARK_RED + "Você deve estar em seu terreno para fazer isso!");
            } else {
                p.sendMessage(ChatColor.DARK_RED + "Oops, a sintaxe de seu comando está incorreta!");
                p.sendMessage(ChatColor.DARK_RED + "Opções disponiveis: comprar, amigos ou deletar!");
            }
        }
        //metodo de virificaçao se ha uma regiao;
        return true;
    }

    public boolean regionmax(Player player) {
        if (!getWorldGuard().getRegionManager(player.getWorld()).hasRegion(player.getName() + "1")) {
            nomeregiao = player.getName() + "1";
        } else if (!getWorldGuard().getRegionManager(player.getWorld()).hasRegion(player.getName() + "2")) {
            nomeregiao = player.getName() + "2";
        } else if (!getWorldGuard().getRegionManager(player.getWorld()).hasRegion(player.getName() + "3")) {
            nomeregiao = player.getName() + "3";
        } else if (!getWorldGuard().getRegionManager(player.getWorld()).hasRegion(player.getName() + "4")) {
            nomeregiao = player.getName() + "4";
        } else if (!getWorldGuard().getRegionManager(player.getWorld()).hasRegion(player.getName() + "5")) {
            nomeregiao = player.getName() + "5";
        } else {
            player.sendMessage(ChatColor.DARK_RED + "Oops!, você já possui o número máximo de regiões!");
            return true;
        }
        return false;
    }

}
