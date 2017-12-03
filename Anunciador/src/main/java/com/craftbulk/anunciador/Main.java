/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.craftbulk.anunciador;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author diego
 */
public class Main extends JavaPlugin {

    public configManager config = null;

    @Override
    public void onEnable() {
        config = new configManager(this);
        timer();
    }

    @Override
    public void onDisable() {
        super.onDisable(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equals("Anunciador")) {
            if (!(sender.hasPermission("Anunciador.Alterar"))) {
                sender.sendMessage("§4Você não possui permissão!");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage("§4Este comando necessita de complementos:");
                sender.sendMessage("§4/Anunciador addmsg <mensagem>");
                sender.sendMessage("§4/Anunciador removemsg <id>");
                sender.sendMessage("§4/Anunciador settempo <Segundos>");
                sender.sendMessage("§4/Anunciador getmsg");
            } else {
                if (args[0].equalsIgnoreCase("addmsg")) {
                    if (args.length < 2) {
                        sender.sendMessage("§4Uso correto : /Anunciador addmsg <mensagem>");
                        return true;
                    }
                    int nummensagem = config.getMensagensYml().getInt("Config.NumeroMensagens");
                    List mensagens;

                    mensagens = config.getMensagensYml().getList("Mensagens");

                    String mensagem = "";
                    for (int i = 1; i < args.length; i++) {
                        if (i == 1) {
                            mensagem = args[i];
                        } else {
                            mensagem = mensagem + " " + args[i];
                        }
                    }
                    mensagens.add(mensagem);
                    config.getMensagensYml().set("Mensagens", mensagens);
                    config.getMensagensYml().set("Config.NumeroMensagens", nummensagem + 1);
                    if (config.salvar()) {
                        sender.sendMessage("§6Mensagem salva com sucesso!");
                    } else {
                        sender.sendMessage("§4Erro ao salvar mensagem!");
                    }

                } else {
                    if (args[0].equalsIgnoreCase("removemsg")) {
                        if (args.length < 2) {
                            sender.sendMessage("§4/Anunciador removemsg <id>");
                            return true;
                        }
                        int id;
                        try {
                            id = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage("§4" + args[1] + " não é um id válido!");
                            return true;
                        }
                        int nummensagem = config.getMensagensYml().getInt("Config.NumeroMensagens");
                        List mensagens;
                        mensagens = config.getMensagensYml().getList("Mensagens");
                        if (mensagens.isEmpty()) {
                            sender.sendMessage("§4Não há nenhuma mensagem salva!");
                            return true;
                        }
                        try {
                            mensagens.remove(id - 1);
                        } catch (Exception e) {
                            sender.sendMessage("§4" + id + " Não é um id válido!");
                            return true;
                        }
                        config.getMensagensYml().set("Mensagens", mensagens);
                        config.getMensagensYml().set("Config.NumeroMensagens", nummensagem - 1);
                        if (config.salvar()) {
                            sender.sendMessage("§6Mensagem removida com sucesso!");
                        } else {
                            sender.sendMessage("§4Erro ao remover mensagem!");
                        }

                    } else {
                        if (args[0].equalsIgnoreCase("getmsg")) {
                            List mensagens;
                            mensagens = config.getMensagensYml().getList("Mensagens");
                            if (mensagens.isEmpty()) {
                                sender.sendMessage("§4Não há nenhuma mensagem salva!");
                                return true;
                            }
                            int i = 1;
                            sender.sendMessage("§6Mensagens:");
                            for (Object mensagen : mensagens) {
                                if (mensagens.size() <= i) {
                                    sender.sendMessage("§6- ID: " + i + " Mensagem: " + mensagen + "\n");
                                } else {
                                    sender.sendMessage("§6- ID: " + i + " Mensagem: " + mensagen);

                                }
                                i++;
                            }

                        } else {
                            if (args[0].equalsIgnoreCase("settempo")) {
                                if (args.length < 1) {
                                    sender.sendMessage("§4Uso correto: /Anunciador settempo <Segundos>");

                                }
                                int tempo;

                                try {
                                    tempo = Integer.parseInt(args[1]);

                                } catch (NumberFormatException e) {
                                    sender.sendMessage(args[1] + "§4 não é um tempo em segundos válido");
                                    return true;
                                }
                                if (tempo < 0) {
                                    sender.sendMessage(args[1] + "§4 não é um tempo em segundos válido");
                                    return true;
                                }

                                config.getMensagensYml().set("Config.TempoMensagens", tempo);
                                config.salvar();
                                timer.cancel();
                                timer();
                                sender.sendMessage("§6Tempo atualizado com sucesso!");
                            }
                        }
                    }
                }

            }
        }
        return true;
    }
    public List mensg;
    Timer timer;

    public void timer() {
        int initialDelay = 0; // start after 30 seconds
        int period = config.getMensagensYml().getInt("Config.TempoMensagens") * 1000;        // repeat every 5 seconds
        timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                sendmessage(getmessage());
            }
        };
        timer.scheduleAtFixedRate(task, initialDelay, period);
    }

    public int increment = -1;

    public String getmessage() {
        increment++;
        if (increment == 0) {
            mensg = config.getMensagensYml().getList("Mensagens");
        }
        if (mensg.size() == increment) {
            increment = 0;
        }
        return (String) mensg.get(increment);
    }

    public void sendmessage(String msg) {
        Bukkit.broadcastMessage("§6[§l➥§r§6]: " + msg);
    }

}
