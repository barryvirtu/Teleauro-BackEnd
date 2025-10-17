package com.trip;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        String accessToken = "KWz2Hlt2TUOLQbBcxXgo"; // Your device token
        String url = "http://74.234.26.48:8080/api/v1/" + accessToken + "/telemetry";
        String routeFile = "src/main/java/com/trip/route_1.json";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Double>> route = mapper.readValue(new File(routeFile), List.class);

            long baseTimestamp = System.currentTimeMillis();

            for (int i = 0; i < route.size(); i++) {
                Map<String, Double> point = route.get(i);
                double lat = point.get("latitude");
                double lon = point.get("longitude");
                double speed = (i == 0 || i == route.size() - 1) ? 0 : 20; // Simulated speed

                long timestamp = baseTimestamp + i * 60000; // 1-minute intervals

                String jsonPayload = String.format(
                    "{\"ts\":%d,\"values\":{\"latitude\":%.6f,\"longitude\":%.6f,\"speed\":%.1f}}",
                    timestamp, lat, lon, speed
                );

                HttpPost post = new HttpPost(url);
                post.setHeader("Content-Type", "application/json");
                post.setEntity(new StringEntity(jsonPayload));
                client.execute(post);

                System.out.println("Sent: " + jsonPayload);
                Thread.sleep(1000); // Optional delay
            }

            System.out.println("Trip simulation complete.");
        } catch (Exception e) {
            System.err.println("Error sending telemetry:");
            e.printStackTrace();
        }
    }
}
