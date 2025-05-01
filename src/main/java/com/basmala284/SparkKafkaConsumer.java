package com.basmala284;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.*;
import scala.Tuple2;

import java.util.*;

public class SparkKafkaConsumer {
    public static void main(String[] args) throws InterruptedException {
        // Spark configuration
        SparkConf conf = new SparkConf().setAppName("TwitterStreamProcessor").setMaster("local[*]");
        JavaStreamingContext streamingContext = new JavaStreamingContext(conf, Durations.seconds(5));

        // Kafka parameters
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaParams.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaParams.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaParams.put(ConsumerConfig.GROUP_ID_CONFIG, "twitter_consumer_group");
        kafkaParams.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        kafkaParams.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);

        // Kafka topic
        Collection<String> topics = Arrays.asList("twitter_topic");

        // Kafka stream
        JavaInputDStream<org.apache.kafka.clients.consumer.ConsumerRecord<String, String>> stream =
                KafkaUtils.createDirectStream(
                        streamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.Subscribe(topics, kafkaParams)
                );

        // Process tweets
        stream.map(record -> record.value())
                .map(tweetJson -> {
                    // Extract tweet text and metadata
                    String tweet = tweetJson.split(",")[0].split(":")[1].replace("\"", "").trim();
                    return tweet;
                })
                .flatMap(tweet -> Arrays.asList(tweet.split(" ")).iterator())
                .filter(word -> word.startsWith("#")) // Extract hashtags
                .mapToPair(hashtag -> new Tuple2<>(hashtag, 1)) // Map each hashtag to (hashtag, 1)
                .reduceByKey(Integer::sum) // Aggregate counts for each hashtag
                .foreachRDD(rdd -> {
                    rdd.foreach(hashtagCount -> System.out.println("Hashtag: " + hashtagCount._1 + ", Count: " + hashtagCount._2));
                });

        // Start the streaming context
        streamingContext.start();
        streamingContext.awaitTermination();
    }
}