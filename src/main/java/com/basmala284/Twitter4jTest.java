//package com.basmala284;
//import twitter4j.*;
//import twitter4j.TwitterFactory;
//import twitter4j.Status;
//import twitter4j.conf.ConfigurationBuilder;
//
//public class Twitter4jTest {
//    public static void main(String[] args) {
//        ConfigurationBuilder cb = new ConfigurationBuilder();
//        cb.setDebugEnabled(true)
//                .setOAuthConsumerKey("ZJYs8LEIuEXHVykFNDFV3lSdp")
//                .setOAuthConsumerSecret("eRJhgAXsWkU6CfUE0oLHcyoPZte1SMEvxTfLjreaWTu5GYuHCA")
//                .setOAuthAccessToken("1917127361377542144-QaAbJU0LW2Pj0aCfnHUkNFMIC7rDLV")
//                .setOAuthAccessTokenSecret("rQR0cmowwLxEj25s9i9LpfbsPOYwqeLUFGITVMCPlhjU4");
//
//
//        TwitterFactory tf = new TwitterFactory(cb.build());
//        Twitter twitter = tf.getInstance();
//
//        try {
//            Query query = new Query("#Football");
//            query.setCount(10); // Limit the number of tweets
//            QueryResult result = twitter.search(query);
//
//            for (Status status : result.getTweets()) {
//                System.out.println("@" + status.getUser().getScreenName() + ": " + status.getText());
//            }
//        } catch (TwitterException e) {
//            e.printStackTrace();
//        }
//    }
//}