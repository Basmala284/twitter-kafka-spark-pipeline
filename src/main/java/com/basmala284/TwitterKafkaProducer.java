package com.basmala284;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Properties;

public class TwitterKafkaProducer {
    public static void main(String[] args) {
        // Twitter API credentials
        String consumerKey = "ZJYs8LEIuEXHVykFNDFV3lSdp";
        String consumerSecret = "eRJhgAXsWkU6CfUE0oLHcyoPZte1SMEvxTfLjreaWTu5GYuHCA";
        String accessToken = "1917127361377542144-QaAbJU0LW2Pj0aCfnHUkNFMIC7rDLV";
        String accessTokenSecret = "rQR0cmowwLxEj25s9i9LpfbsPOYwqeLUFGITVMCPlhjU4";

        // Kafka topic
        String topic = "twitter_topic";

        // Set up Twitter API configuration
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);

        // Create Twitter instance
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        // Set up Kafka producer
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        try {
            // Search for tweets with specific hashtags or keywords
            Query query = new Query("#exampleHashtag");
            query.setCount(100); // Fetch up to 100 tweets per request
            QueryResult result = twitter.search(query);

            for (Status status : result.getTweets()) {
                String tweet = status.getText();
                System.out.println("Tweet: " + tweet);

                // Send the tweet to the Kafka topic
                producer.send(new ProducerRecord<>(topic, tweet));
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}