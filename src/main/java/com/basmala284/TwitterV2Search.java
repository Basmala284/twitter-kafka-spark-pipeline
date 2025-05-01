//package com.basmala284;
//
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//
//public class TwitterV2Search {
//    public static void main(String[] args) {
//        String bearerToken = "AAAAAAAAAAAAAAAAAAAAABG90wEAAAAA3Ze4VVadPWW2CuFyMhzf67j9Npo%3DYOoxzyfIF7MtiR8nvC4ToQy0BbCNhD0vSgeJb5v2t99kwLEzTG"; // Replace with your v2 Bearer Token
//        String query = "Football"; // Search query
//        int maxResults = 10; // Number of results to fetch
//
//        try {
//            // Build the Twitter API v2 request
//            HttpClient client = HttpClient.newHttpClient();
//            String url = String.format(
//                    "https://api.twitter.com/2/tweets/search/recent?query=%s&max_results=%d",
//                    query, maxResults
//            );
//
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(url))
//                    .header("Authorization", "Bearer " + bearerToken)
//                    .GET()
//                    .build();
//
//            // Send the request and get the response
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            // Print the response
//            System.out.println("Response Code: " + response.statusCode());
//            System.out.println("Response Body: " + response.body());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}