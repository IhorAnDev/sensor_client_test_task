import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SensorConsumer {
    public static void main(String[] args) {
        String sensorName = "Chvitcer";
        Random random = new Random();
        registerSensor(sensorName);

        for (int i = 0; i < 500; i++) {
            System.out.println(i);
            sendMeasurement(random.nextDouble() * 45.0, random.nextBoolean(), sensorName);
        }
    }

    private static void registerSensor(String name) {
        final String url = "http://localhost:8085/sensor/register";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("name", name);

        sendRequestWithDataJson(url, jsonData);
    }

    private static void sendMeasurement(Double value, Boolean isRaining, String sensorName) {
        final String url = "http://localhost:8085/measurements/add";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("value", value);
        jsonData.put("isRaining", isRaining);
        jsonData.put("sensor", Map.of("name", sensorName));
        sendRequestWithDataJson(url, jsonData);
    }

    public static void sendRequestWithDataJson(String url, Map<String, Object> jsonData) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> request = new HttpEntity<>(jsonData, headers);
        try {
            restTemplate.postForObject(url, request, String.class);
            System.out.println("Sent successfully!");
        } catch (HttpClientErrorException e) {
            System.out.println("Error!");
            System.out.println(e.getMessage());
        }
    }
}
