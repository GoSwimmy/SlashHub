package com.goswimmy.slashhub;

import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(
        id = "slashhub",
        name = "Slashhub",
        version = "1.0-SNAPSHOT",
        description = "Plugin for /hub and /lobby",
        url = "goswimmy.com",
        authors = {"GoSwimmy"}
)
public class Slashhub {

    private final ProxyServer server;
    private static Toml config;
    public static Path folder;

    public Toml loadConfig(Path path) {
        File folder = path.toFile();
        File file = new File(folder, "config.toml");
        if(!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if(!file.exists()) {
            try(InputStream input = getClass().getResourceAsStream("/"+file.getName())) {
                if(input != null) {
                    Files.copy(input, file.toPath());
                } else {
                    file.createNewFile();
                }
            }catch(IOException exception) {
                exception.printStackTrace();
                return null;
            }
        }
        return new Toml().read(file);
    }

    @Inject
    public Slashhub(ProxyServer server, Logger logger, @DataDirectory final Path folder) {
        this.server = server;
        Slashhub.folder = folder;
        config = loadConfig(folder);
    }

    @Inject
    private Logger logger;

    public static Toml getConfig() {
        return config;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getCommandManager().register(new SlashHubCommand(server), "hub", "lobby");
    }

    public static void reload() {
        File file = new File(String.valueOf(folder), "config.toml");
        config = new Toml().read(file);
    }
}
