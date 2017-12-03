/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.craftbulk.terrenos;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import static org.bukkit.Bukkit.getServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 *
 * @author diego
 */
public class Vault {

    public static Economy econ = null;

    public boolean descontar(Player player, Double Valor) {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        econ = (Economy) rsp.getProvider();
        EconomyResponse transacao = econ.withdrawPlayer(player, Valor);
        return transacao.transactionSuccess();
    }

}
