package com.ittekikun.bplugin.MineTweet.Listener;

import com.ittekikun.bplugin.MineTweet.Config.MineTweetConfig;
import com.ittekikun.bplugin.MineTweet.MineTweet;
import com.ittekikun.bplugin.MineTweet.Twitter.TwitterManager;
import com.ittekikun.bplugin.MineTweet.Utility;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import twitter4j.TwitterException;

import java.io.File;
import java.util.UUID;

public class ConnectionListener implements Listener
{

    MineTweet plugin;
    MineTweetConfig mtConfig;
    TwitterManager twitterManager;

    public ConnectionListener(MineTweet plugin)
    {
        this.plugin = plugin;
        this.mtConfig = plugin.mtConfig;
        this.twitterManager = plugin.twitterManager;
    }

    @EventHandler
    public void onLoginEvent(LoginEvent event) throws TwitterException
    {
        if (!mtConfig.tweetWithPicture)
        {
            PendingConnection connection = event.getConnection();
            String name = connection.getName();

            String number = Integer.toString(plugin.getProxy().getOnlineCount());

            String message = replaceKeywords(mtConfig.join_message_temp, name, number);

            twitterManager.tweet(message);
        }
        else
        {
            PendingConnection connection = event.getConnection();
            final String name = connection.getName();

            final String number = Integer.toString(plugin.getProxy().getOnlineCount());
            plugin.getProxy().getScheduler().runAsync(plugin, new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        String uuid = UUID.randomUUID().toString();
                        File tweetImage = new File(plugin.getDataFolder(), uuid + ".png");

                        Utility.generationPlayerImage(name, "JOIN THE GAME!", tweetImage);

                        String message = replaceKeywords(mtConfig.join_message_temp, name, number);
                        twitterManager.tweet(message, tweetImage);
                        tweetImage.delete();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @EventHandler
    public void onPlayerDisconnectEvent(PlayerDisconnectEvent event) throws TwitterException
    {
        if (!mtConfig.tweetWithPicture)
        {
            ProxiedPlayer player = event.getPlayer();
            String name = player.getName();

            String number = Integer.toString(plugin.getProxy().getOnlineCount() -1);

            String message = replaceKeywords(mtConfig.quit_message_temp, name, number);

            twitterManager.tweet(message);
        }
        else
        {
            final ProxiedPlayer player = event.getPlayer();
            final String name = player.getName();

            final String number = Integer.toString(plugin.getProxy().getOnlineCount() -1);
            plugin.getProxy().getScheduler().runAsync(plugin, new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        String uuid = UUID.randomUUID().toString();
                        File tweetImage = new File(plugin.getDataFolder(), uuid + ".png");

                        Utility.generationPlayerImage(name, "LEFT THE GAME!", tweetImage);

                        String message = replaceKeywords(mtConfig.quit_message_temp, name, number);
                        twitterManager.tweet(message, tweetImage);
                        tweetImage.delete();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private String replaceKeywords(String source, String name, String number)
    {
        String result = source;
        if (result.contains(MineTweet.KEYWORD_USER))
        {
            result = result.replace(MineTweet.KEYWORD_USER, name);
        }
        if (result.contains(MineTweet.KEYWORD_NUMBER))
        {
            result = result.replace(MineTweet.KEYWORD_NUMBER, number);
        }
        if (result.contains(MineTweet.KEYWORD_NEWLINE))
        {
            result = result.replace(MineTweet.KEYWORD_NEWLINE, MineTweet.SOURCE_NEWLINE);
        }
        if (result.contains(MineTweet.KEYWORD_TIME))
        {
            String time = Utility.timeGetter(mtConfig.dateformat);

            result = result.replace(MineTweet.KEYWORD_TIME, time);
        }
        return result;
    }
}
