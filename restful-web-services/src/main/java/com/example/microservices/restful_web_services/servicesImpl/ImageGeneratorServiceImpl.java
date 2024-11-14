package com.example.microservices.restful_web_services.servicesImpl;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.example.microservices.restful_web_services.services.ImageGeneratorService;

@Service
public class ImageGeneratorServiceImpl implements ImageGeneratorService {
	
	@Value("${openai.api.key}")
    private String openaiApiKey;
	
	private static final String API_URL = "https://api.openai.com/v1/images/generations";

	public ResponseEntity<String> generateImage(@RequestParam("word") String word) {
        try {
            String imageUrl = requestImageFromAPI(word);
            if (imageUrl != null) {
                String filePath = saveImage(imageUrl, word + ".png");
                return ResponseEntity.ok("Image generated and saved to " + filePath);
            } else {
                return ResponseEntity.status(500).body("Failed to generate image.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }
	
	private String requestImageFromAPI(String word) throws Exception {
        String prompt = "A meaningful image related to the word " + word;

        // Set up the REST template
        RestTemplate restTemplate = new RestTemplate();
        HttpURLConnection connection = (HttpURLConnection) new URL(API_URL).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + openaiApiKey);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Build the JSON request
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("prompt", prompt);
        jsonRequest.put("n", 1);
        jsonRequest.put("size", "1024x1024");

        connection.getOutputStream().write(jsonRequest.toString().getBytes());

        // Read the JSON response
        InputStream responseStream = connection.getInputStream();
        StringBuilder responseBuilder = new StringBuilder();
        int ch;
        while ((ch = responseStream.read()) != -1) {
            responseBuilder.append((char) ch);
        }

        JSONObject jsonResponse = new JSONObject(responseBuilder.toString());
        return jsonResponse.getJSONArray("data").getJSONObject(0).getString("url");
    }

    private String saveImage(String imageUrl, String fileName) throws Exception {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = connection.getInputStream();

        String filePath = Paths.get(fileName).toAbsolutePath().toString();
        FileOutputStream outputStream = new FileOutputStream(filePath);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
        inputStream.close();

        return filePath;
    }
}
