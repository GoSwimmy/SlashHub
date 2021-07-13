package com.goswimmy.slashhub;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class SlashHubCommand implements Command {

    public final ProxyServer server;

    public SlashHubCommand(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(CommandSource source, @NonNull String[] strings) {
        if(source instanceof Player) {
            Player p = (Player) source;
            String rawserver = Slashhub.getConfig().getString("main.server");
            if(server.getServer(rawserver).isPresent()) {
                Optional<RegisteredServer> s = server.getServer(rawserver);
                server.getPlayer(p.getUniqueId()).get().createConnectionRequest(s.get()).connect();
            } else {
                p.sendMessage(TextComponent.of("An error occurred!"));
            }
        }
    }

}
