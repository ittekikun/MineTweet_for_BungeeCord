package com.ittekikun.bplugin.MineTweet;

import com.ittekikun.bplugin.MineTweet.Config.MineTweetConfig;
import com.ittekikun.bplugin.MineTweet.Listener.ConnectionListener;
import com.ittekikun.bplugin.MineTweet.Twitter.TwitterManager;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.util.logging.Logger;

public class MineTweet extends Plugin
{
    public static MineTweet instance;
    public static Logger log;
    public static final String prefix = "[MineTweet] ";
    public MineTweetConfig mtConfig;
    public TwitterManager twitterManager;

    public static final String KEYWORD_USER = "$user";
    public static final String KEYWORD_NUMBER = "$number";
    public static final String KEYWORD_TIME = "$time";
    public static final String KEYWORD_NEWLINE = "$newline";
    public static final String SOURCE_NEWLINE = "\n";

    @Override
    public void onEnable()
    {
        log = getLogger();
        //log.setFilter(new LogFilter(prefix));
        instance = this;

        mtConfig = new MineTweetConfig(this);
        mtConfig.loadConfigs();

        twitterManager = new TwitterManager(this);
        twitterManager.startSetup();

        this.getProxy().getPluginManager().registerListener(this, new ConnectionListener(this));
    }

    public File getPluginJarFile()
    {
        return this.getFile();
    }
}
