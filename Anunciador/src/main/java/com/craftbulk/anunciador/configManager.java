/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.craftbulk.anunciador;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author diego
 */
public class configManager {

    private Main plugin = null;
    private static FileConfiguration MensagensYml;
    private static File MensagemFile;

    public configManager(Main main) {
        this.plugin = main;
        inicializar();
    }

    public final void inicializar() {
        MensagemFile = new File(plugin.getDataFolder() + "/Mensagens.yml");
        MensagensYml = YamlConfiguration.loadConfiguration(MensagemFile);
        if (!(MensagemFile.exists())) {
            MensagensYml.set("Config.NumeroMensagens", 0);
            MensagensYml.set("Config.TempoMensagens", 10000);
            List mensagens = new ArrayList<>();
            MensagensYml.set("Mensagens", mensagens);
        }
        salvar();
    }

    public FileConfiguration getMensagensYml() {
        return MensagensYml;
    }

    public boolean salvar() {
        try {
            MensagensYml.save(MensagemFile);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

}
