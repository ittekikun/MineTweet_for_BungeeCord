package com.ittekikun.bplugin.MineTweet.Config;

import com.ittekikun.bplugin.MineTweet.MineTweet;

import java.io.IOException;

public class MineTweetConfig
{
    public MineTweet plugin;
    public ConfigAccessor system;
    public ConfigAccessor twitter;

    public Boolean versionCheck;
    public Boolean GUICertify;

    public String consumerKey;
    public String consumerSecret;
    public String accessToken;
    public String accessTokenSecret;

    public Boolean addDate;
    public String dateformat;

    public Boolean playerJoinTweet;
    public Boolean playerQuitTweet;

    public Boolean tweetWithPicture;

    public String join_message_temp;
    public String quit_message_temp;


    public MineTweetConfig(MineTweet plugin)
    {
        this.plugin = plugin;
    }

    public void loadConfigs()
    {
        system = new ConfigAccessor(plugin, "system.yml");
        twitter = new ConfigAccessor(plugin, "twitter.yml");

        system.saveDefaultConfig();
        twitter.saveDefaultConfig();

        try
        {
            this.GUICertify = system.getConfig().getBoolean("GUICertify", true);
            this.consumerKey = system.getConfig().getString("consumerKey", "xxxxxxxxxx");
            this.consumerSecret = system.getConfig().getString("consumerSecret", "xxxxxxxxxx");
            this.accessToken = system.getConfig().getString("accessToken", "xxxxxxxxxx");
            this.accessTokenSecret = system.getConfig().getString("accessTokenSecret", "xxxxxxxxxx");

            this.versionCheck = system.getConfig().getBoolean("VersionCheck", true);

            this.addDate = twitter.getConfig().getBoolean("AddDate", true);
            this.dateformat = twitter.getConfig().getString("DateFormat", "EEE MMM d HH:mm:ss z");

            this.tweetWithPicture = twitter.getConfig().getBoolean("TweetWithPicture", false);
            this.playerJoinTweet = twitter.getConfig().getBoolean("PlayerJoinTweet", true);
            this.join_message_temp = twitter.getConfig().getString("JoinMessageTemplate", "$userさんがサーバーにログインしました。現在$number人がログインしています。【自動投稿】");
            this.playerQuitTweet = twitter.getConfig().getBoolean("PlayerQuitTweet", true);
            this.quit_message_temp = twitter.getConfig().getString("QuitMessageTemplate", "$userさんがサーバーからログアウトしました。現在$number人がログインしています。【自動投稿】");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
