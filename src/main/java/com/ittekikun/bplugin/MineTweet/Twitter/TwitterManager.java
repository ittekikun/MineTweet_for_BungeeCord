package com.ittekikun.bplugin.MineTweet.Twitter;

import com.ittekikun.bplugin.MineTweet.Config.MineTweetConfig;
import com.ittekikun.bplugin.MineTweet.Data.ConsumerKey;
import com.ittekikun.bplugin.MineTweet.MineTweet;
import com.ittekikun.bplugin.MineTweet.Twitter.Gui.Swing.CertifyGui_Swing;
import com.ittekikun.bplugin.MineTweet.Utility;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class TwitterManager
{
    public MineTweet plugin;
    public Twitter twitter;
    public MineTweetConfig mtConfig;
    public AccessToken accesstoken;

    public static Boolean status;

    public TwitterManager(MineTweet plugin)
    {
        this.plugin = plugin;
        this.mtConfig = plugin.mtConfig;
    }

    public void startSetup()
    {
        if(mtConfig.GUICertify)
        {
            //TwitterStream twStream = new TwitterStreamFactory(configuration).getInstance();
            ConfigurationBuilder conf = new ConfigurationBuilder();

            conf.setOAuthConsumerKey(ConsumerKey.m_ConsumerKey);
            conf.setOAuthConsumerSecret(ConsumerKey.m_ConsumerSecret);

            twitter = new TwitterFactory(conf.build()).getInstance();

            accesstoken = loadAccessToken();

            //初期起動時(ファイルなし)
            if(accesstoken == null)
            {
                status = false;

                plugin.getProxy().getScheduler().runAsync(plugin, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        final CertifyGui_Swing certifyGui_swing = new CertifyGui_Swing(plugin);
                        certifyGui_swing.openGui();
                        return;
                    }
                });
            }
            //ファイル有り
            else
            {
                status = true;

                twitter.setOAuthAccessToken(accesstoken);
            }
        }
        //GUI未使用時
        else
        {
            status = true;

            ConfigurationBuilder conf = new ConfigurationBuilder();
            conf.setOAuthConsumerKey(mtConfig.consumerKey);
            conf.setOAuthConsumerSecret(mtConfig.consumerSecret);
            conf.setOAuthAccessToken(mtConfig.accessToken);
            conf.setOAuthAccessTokenSecret(mtConfig.accessTokenSecret);

            twitter = new TwitterFactory(conf.build()).getInstance();
        }
    }

    public void tweet(String tweet) throws TwitterException
    {
        StatusUpdate statusUpdate = makeUpdate(tweet);
        updateStatus(statusUpdate);
    }

    public void tweet(String tweet, File media) throws TwitterException
    {
        StatusUpdate statusUpdate = makeUpdate(tweet);
        statusUpdate.media(media);

        updateStatus(statusUpdate);
    }

    private StatusUpdate makeUpdate(String tweet)
    {
        StatusUpdate statusUpdate;

        if(mtConfig.addDate)
        {
            String time = Utility.timeGetter(mtConfig.dateformat);

            statusUpdate = new StatusUpdate(tweet + "\n" + time);
        }
        else
        {
            statusUpdate = new StatusUpdate(tweet);
        }
        return statusUpdate;
    }

    private void updateStatus(StatusUpdate statusUpdate) throws TwitterException
    {
        if(status)
        {
            twitter.updateStatus(statusUpdate);
        }
        else
        {
            MineTweet.log.severe("現在ツイートができない状況の為、下記のツイートは行われませんでした。");
            MineTweet.log.severe(statusUpdate.getStatus());
            return;
        }
    }

    public RequestToken openOAuthPage()
    {
        RequestToken requestToken = null;
        Desktop desktop = Desktop.getDesktop();

        try
        {
            requestToken = twitter.getOAuthRequestToken();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            URI uri = new URI(requestToken.getAuthorizationURL());
            desktop.browse(uri);
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return requestToken;
    }

    public AccessToken loadAccessToken()
    {
        File f = createAccessTokenFileName();

        ObjectInputStream is = null;
        try
        {
            is = new ObjectInputStream(new FileInputStream(f));
            AccessToken accessToken = (AccessToken)is.readObject();
            return accessToken;
        }
        catch (IOException e)
        {
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            if(is != null){
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public AccessToken getAccessToken(RequestToken  requesttoken,String pin) throws TwitterException
    {
        AccessToken accessToken = null;

        accessToken = twitter.getOAuthAccessToken(requesttoken, pin);

        return accessToken;
    }

    public void storeAccessToken(AccessToken accessToken)
    {
        //ファイル名の生成
        File f = createAccessTokenFileName();

        //親ディレクトリが存在しない場合，親ディレクトリを作る．
        File d = f.getParentFile();
        if (!d.exists())
        {
            d.mkdirs();
        }

        //ファイルへの書き込み
        ObjectOutputStream os = null;
        try
        {
            os = new ObjectOutputStream(new FileOutputStream(f));
            os.writeObject(accessToken);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public File createAccessTokenFileName()
    {
        String s = plugin.getDataFolder() + "/AccessToken.yml";
        return new File(s);
    }
}