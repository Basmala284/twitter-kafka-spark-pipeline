package com.basmala284;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.Status;
import twitter4j.conf.ConfigurationBuilder;

public class Twitter4jTest {
    public static void main(String[] args) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("your-consumer-key")
                .setOAuthConsumerSecret("your-consumer-secret")
                .setOAuthAccessToken("your-access-token")
                .setOAuthAccessTokenSecret("your-access-token-secret");

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        try {
            Status status = twitter.updateStatus("Hello, Twitter!");
            System.out.println("Successfully updated status to: " + status.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}