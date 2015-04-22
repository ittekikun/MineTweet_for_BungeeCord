package com.ittekikun.bplugin.MineTweet.Config;

import com.ittekikun.bplugin.MineTweet.MineTweet;
import com.ittekikun.bplugin.MineTweet.Utility;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

public class ConfigAccessor
{
    private final String fileName;
    private final Plugin plugin;

    private File configFile;
    private Configuration configuration;

    public ConfigAccessor(Plugin plugin, String fileName)
    {
        if (plugin == null)
        {
            throw new IllegalArgumentException("plugin cannot be null");
        }
        this.plugin = plugin;
        this.fileName = fileName;
        File dataFolder = plugin.getDataFolder();
        if (dataFolder == null)
        {
            throw new IllegalStateException();
        }
        this.configFile = new File(plugin.getDataFolder(), fileName);
    }

    public Configuration getConfig() throws IOException
    {
        if (configuration == null)
        {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        }
        return configuration;
    }

    public void saveConfig()
    {
        if (configuration == null || configFile == null)
        {
            return;
        }
        else
        {
            try
            {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, configFile);
            }
            catch (IOException ex)
            {
                MineTweet.log.severe("Could not save config to " + configFile);
                ex.printStackTrace();
            }
        }
    }

    public void saveDefaultConfig()
    {
        if (!configFile.exists())
        {
            // TODO このへんの仕様確認する
            Utility.copyFileFromJar(MineTweet.instance.getPluginJarFile(), configFile, fileName);
        }
    }
}