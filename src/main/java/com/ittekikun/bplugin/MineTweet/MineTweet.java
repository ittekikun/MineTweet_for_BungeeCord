package com.ittekikun.bplugin.MineTweet;

import com.ittekikun.bplugin.MineTweet.Listener.ConnectionListener;
import com.ittekikun.bplugin.MineTweet.Twitter.TwitterManager;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Logger;

public class MineTweet extends Plugin
{
    public static Logger log;
    public static final String prefix = "[MineTweet] ";
    public TwitterManager twitterManager;

    public static final String KEYWORD_USER = "$user";
    public static final String KEYWORD_NUMBER = "$number";

    @Override
    public void onEnable()
    {
        log = getLogger();
        //log.setFilter(new LogFilter(prefix));

        twitterManager = new TwitterManager(this);
        twitterManager.startSetup();

        this.getProxy().getPluginManager().registerListener(this, new ConnectionListener(this));
    }
}
