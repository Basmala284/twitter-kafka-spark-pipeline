package com.basmala284;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class TwitterKafkaProducer {
    public static void main(String[] args) {
        // Twitter API Bearer Token
        String bearerToken = "AAAAAAAAAAAAAAAAAAAAABG90wEAAAAAznA%2F447rttc3qv2wlhq6nLrdIEk%3DYtYXyi0PmXrxoMWXrNzGtxBF5TmW2CbvOw5PmJjHLFa9CkOQ5O"; // Replace with your Bearer Token from Twitter Developer Portal

        // Kafka topic
        String topic = "twitter_topic";

        // Set up Kafka producer

        Properties props = new Properties();
//        props.put("bootstrap.servers", "localhost:9092");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // Twitter API v2 Recent Search Endpoint
        String query = "Football"; // Search query
        int maxResults = 10; // Number of tweets to fetch
        String url = String.format(
                "https://api.twitter.com/2/tweets/search/recent?query=%s&max_results=%d",
                query, maxResults
        );

        try {
            // Create HTTP client and request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + bearerToken)
                    .GET()
                    .build();

            // Send the request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse the JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseJson = mapper.readTree(response.body());

            // Extract and send tweets to Kafka
            if (responseJson.has("data")) {
                for (JsonNode tweet : responseJson.get("data")) {
                    String tweetText = tweet.get("text").asText();

                    // Add metadata to the tweet
                    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    int tweetLength = tweetText.length();
                    String enrichedTweet = String.format("{\"tweet\":\"%s\", \"timestamp\":\"%s\", \"length\":%d}",
                            tweetText, timestamp, tweetLength);

                    // Publish enriched tweet to Kafka
                    System.out.println("Enriched Tweet: " + enrichedTweet);
                    producer.send(new ProducerRecord<>(topic, enrichedTweet));
                }
            } else {
                System.out.println("No tweets found for the query: " + query);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}