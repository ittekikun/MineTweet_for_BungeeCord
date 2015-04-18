package com.ittekikun.bplugin.MineTweet.Twitter.Gui.Swing;

import com.ittekikun.bplugin.MineTweet.MineTweet;
import com.ittekikun.bplugin.MineTweet.Twitter.TwitterManager;
import com.ittekikun.bplugin.MineTweet.Utility;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CertifyGui_Swing
{
    static JButton button1 = new JButton("WEBで認証する(ブラウザが開きます)");
    static JButton button2 = new JButton("登録(PINコードを入力してください)");
    static JButton button3 = new JButton("閉じる(登録が終わったらこのボタンから閉じてください)");

    static JTextField field1 = new JTextField(1);

    static JFrame mainFrame = new JFrame("Twitterアクセストークン取得");

    public MineTweet plugin;
    public RequestToken requestToken;
    public Twitter twitter;
    public TwitterManager twitterManager;

    public CertifyGui_Swing(MineTweet plugin)
    {
        this.plugin = plugin;
        this.twitter = plugin.twitterManager.twitter;
        this.twitterManager = plugin.twitterManager;
    }

    public void openGui()
    {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400,300);
        mainFrame.setLocationRelativeTo(null);

        //×ボタンで閉じれなくなる(閉じると本体も強制終了してしまう為)
        //本体が強制終了の件はコード上の問題であった
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.setVisible(true);

        button1.addActionListener(new ActionAdapter());
        button2.addActionListener(new ActionAdapter());
        button3.addActionListener(new ActionAdapter());

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.LINE_AXIS));
        panel1.add(field1);	//テキストエリア
        panel1.add(button2);//ボタン

        Container contentPanel = mainFrame.getContentPane();
        contentPanel.add(panel1, BorderLayout.CENTER);
        contentPanel.add(button1, BorderLayout.NORTH);
        contentPanel.add(button3, BorderLayout.SOUTH);

        field1.setToolTipText("ここにPINコードを入力してください。");
    }

    class ActionAdapter implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {

            if (event.getSource() == button1)
            {
                requestToken = twitterManager.openOAuthPage();
            }

            if(event.getSource() == button2)
            {
                String pin = field1.getText();
                register(twitter,pin,requestToken,twitterManager);
            }

            if(event.getSource() == button3)
            {
                Component c = (Component)event.getSource();
                Window window = SwingUtilities.getWindowAncestor(c);
                window.dispose();
            }
        }

        private AccessToken register(Twitter twitter, String pin, RequestToken  requesttoken, TwitterManager twitterManager)
        {
            AccessToken accessToken = null;
            try
            {
                if(requesttoken != null)
                {
                    if(pin.length() == 7)
                    {
                        if(Utility.isInteger(pin))
                        {
                            accessToken = twitter.getOAuthAccessToken(requesttoken, pin);
                            twitterManager.storeAccessToken(accessToken);
                            twitterManager.startSetup();
                            JOptionPane.showMessageDialog(null, "登録しました。");
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "何も入力されていません。\r\nもしくは入力されているPINコードが間違っています。");
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "何も入力されていません。\r\nもしくは入力されているPINコードが間違っています。");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "まだWEBで認証していません。");
                }
            }
            catch (TwitterException te)
            {
                if(401 == te.getStatusCode())
                {
                    JOptionPane.showMessageDialog(null, "アクセストークンを取得できませんでした。\r\nもしくは既に取得済みか、\r\n入力されているPINコードが間違っています。\r\nサーバーを再起動し、もう一度WEBで認証してください。");
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "例外が発生しました。\r\n恐らくPINコードが正しく入力されていません。\r\nサーバーを再起動し、もう一度WEBで認証してください。");
                    te.printStackTrace();
                }
            }
            return accessToken;
        }
    }
}