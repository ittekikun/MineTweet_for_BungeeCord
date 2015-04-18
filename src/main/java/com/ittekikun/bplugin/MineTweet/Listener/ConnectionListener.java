package com.ittekikun.bplugin.MineTweet.Listener;

import com.ittekikun.bplugin.MineTweet.MineTweet;
import com.ittekikun.bplugin.MineTweet.Twitter.TwitterManager;
import com.ittekikun.bplugin.MineTweet.Utility;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import twitter4j.TwitterException;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class ConnectionListener implements Listener
{

    MineTweet plugin;
    //MineTweetConfig mtConfig;
    TwitterManager twitterManager;

    public ConnectionListener(MineTweet plugin)
    {
        this.plugin = plugin;
        //this.mtConfig = plugin.mtConfig;
        this.twitterManager = plugin.twitterManager;
    }

    @EventHandler
    public void onServerConnectedEvent(ServerConnectedEvent event) throws TwitterException
    {
        //if (!mtConfig.debugMode)
        if (!true)
        {
            ProxiedPlayer player = event.getPlayer();
            String name = player.getName();

            String number = Integer.toString(plugin.getProxy().getOnlineCount());

            String message = replaceKeywords("$userさんがサーバーにログインしました。現在$number人がログインしています。【自動投稿】", name, number);

            twitterManager.tweet(message);
        }
        else
        {
//            Player player = event.getPlayer();
//            final String name = player.getName();
//
//            ArrayList players = Utility.getOnlinePlayers();
//            final String number = Integer.toString((players.size()));
//            //画像生成でラグが起きるので別スレッド
//            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    try
//                    {
//                        String uuid = UUID.randomUUID().toString();
//                        File tweetImage = new File(plugin.getDataFolder(), uuid + ".png");
//
//                        Utility.generationPlayerImage(name, "JOIN THE GAME!", tweetImage);
//
//                        String message = replaceKeywords(mtConfig.join_message_temp, name, number);
//                        twitterManager.tweet(message, tweetImage);
//                        tweetImage.delete();
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//            });
        }
    }

    @EventHandler
    public void onServerDisconnectEvent(ServerDisconnectEvent event) throws TwitterException
    {
        //if (!mtConfig.debugMode)
        if (!true)
        {
            ProxiedPlayer player = event.getPlayer();
            String name = player.getName();

            String number = Integer.toString(plugin.getProxy().getOnlineCount() -1);

            String message = replaceKeywords("$userさんがサーバーにログインしました。現在$number人がログインしています。【自動投稿】", name, number);

            twitterManager.tweet(message);
        }
        else
        {
            //            Player player = event.getPlayer();
            //            final String name = player.getName();
            //
            //            ArrayList players = Utility.getOnlinePlayers();
            //            final String number = Integer.toString((players.size()));
            //            //画像生成でラグが起きるので別スレッド
            //            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable()
            //            {
            //                @Override
            //                public void run()
            //                {
            //                    try
            //                    {
            //                        String uuid = UUID.randomUUID().toString();
            //                        File tweetImage = new File(plugin.getDataFolder(), uuid + ".png");
            //
            //                        Utility.generationPlayerImage(name, "JOIN THE GAME!", tweetImage);
            //
            //                        String message = replaceKeywords(mtConfig.join_message_temp, name, number);
            //                        twitterManager.tweet(message, tweetImage);
            //                        tweetImage.delete();
            //                    }
            //                    catch (Exception e)
            //                    {
            //                        e.printStackTrace();
            //                    }
            //                }
            //            });
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
        return result;
    }
}
