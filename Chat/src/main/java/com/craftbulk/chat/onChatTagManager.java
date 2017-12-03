/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.craftbulk.chat;

import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author diego
 */
public class onChatTagManager extends Event {
    Player player;
    String tag;
    ArrayList<String> str = new ArrayList();
    ArrayList<String> grp = new ArrayList();
    onChatTagManager(Player p) {
       this.player = p;
    }
    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
   
    private static final HandlerList handlers = new HandlerList();

    public String getTag() {
        String[] tagordem = {"{destemido}","{predador}","{mito}","{magnata}"};
        String espelho = "{destemido}{predador}{mito}{magnata}";
        for (int i = 0; i < str.size(); i++) {
        if(espelho.contains("{" + grp.get(i) + "}")){
        espelho = espelho.replace("{" + grp.get(i) + "}", "&f"+ str.get(i).trim() + "&8.");
        }
        }
        for (String tagordem1 : tagordem) {
            if (espelho.contains(tagordem1)) {
                espelho = espelho.replace(tagordem1, "");
            }
        }
        
        return espelho.trim();
    }

    public void setTag(String tag,String grupo) {
        str.add(tag);
        grp.add(grupo);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
