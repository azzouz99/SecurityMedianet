package com.example.securitymedianet.Services.Flask;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FlaskServices implements IFlaskServices {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Map<String, Object> predictAll(float analysteConcepteur, float gestionCoordination) {
        String url = "http://localhost:5000/predict/all"; // Replace with your Flask server URL

        // Create request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Create request body
        Map<String, Object> body = new HashMap<>();
        body.put("Analyste concepteur", analysteConcepteur);
        body.put("Gestion et coordination du projet", gestionCoordination);

        // Create HttpEntity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Make the request
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        // Return the response body
        return response.getBody();
    }
    @Override
    public Map<String, Object> predictIC(float analysteConcepteur, float gestionCoordination) {
        String url = "http://localhost:5000/predict/insertion_contenu"; // Replace with your Flask server URL

        // Create request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Create request body
        Map<String, Object> body = new HashMap<>();
        body.put("Analyste concepteur", analysteConcepteur);
        body.put("Gestion et coordination du projet", gestionCoordination);

        // Create HttpEntity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Make the request
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        // Return the response body
        return response.getBody();
    }
    @Override
    public Map<String, Object> predictIS(float gestionCoordination) {
        String url = "http://localhost:5000/predict/ingenieur_systeme"; // Replace with your Flask server URL

        // Create request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Create request body
        Map<String, Object> body = new HashMap<>();

        body.put("Gestion et coordination du projet", gestionCoordination);

        // Create HttpEntity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Make the request
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        // Return the response body
        return response.getBody();
    }
    @Override
    public Map<String, Object> predictIT(float gestionCoordination, float ingenieur_systeme) {
        String url = "http://localhost:5000/predict/ingenieur_test";
        // Create request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        // Create request body
        Map<String, Object> body = new HashMap<>();
        body.put("Gestion et coordination du projet", gestionCoordination);
        body.put("Ingénieur système", ingenieur_systeme);
        // Create HttpEntity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        // Make the request
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        // Return the response body
        return response.getBody();
    }
    @Override
    public Map<String, Object> predictSEO(float analysteConcepteur, float gestionCoordination) {
        String url = "http://localhost:5000/predict/seo"; // Replace with your Flask server URL
        // Create request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        // Create request body
        Map<String, Object> body = new HashMap<>();
        body.put("Analyste concepteur", analysteConcepteur);
        body.put("Gestion et coordination du projet", gestionCoordination);
        // Create HttpEntity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        // Make the request
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        // Return the response body
        return response.getBody();
    }
    @Override
    public Map<String, Object> predictIntegration(float ingenieur_test, float ingenieur_systeme, float infographie) {
        String url = "http://localhost:5000/predict/integration"; // Replace with your Flask server URL
        // Create request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        // Create request body
        Map<String, Object> body = new HashMap<>();
        body.put("Ingénieur test", ingenieur_test);
        body.put("Ingénieur système", ingenieur_systeme);
        body.put("Infographie ", infographie);
        // Create HttpEntity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        // Make the request
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        // Return the response body
        return response.getBody();
    }
    @Override
    public Map<String, Object> predictInfographie(float analysteConcepteur, float insertion_contenu) {
        String url = "http://localhost:5000/predict/infographie"; // Replace with your Flask server URL
        // Create request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        // Create request body
        Map<String, Object> body = new HashMap<>();
        body.put("Analyste concepteur", analysteConcepteur);
        body.put("Insertion contenu", insertion_contenu);
        // Create HttpEntity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        // Make the request
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        // Return the response body
        return response.getBody();
    }
    @Override
    public Map<String, Object> predictFormation(float gestionCoordination) {
        String url = "http://localhost:5000/predict/formation"; // Replace with your Flask server URL

        // Create request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Create request body
        Map<String, Object> body = new HashMap<>();

        body.put("Gestion et coordination du projet", gestionCoordination);

        // Create HttpEntity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Make the request
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        // Return the response body
        return response.getBody();
    }
    @Override
    public Map<String, Object> predictCost(String requestData) {
        String url = "http://127.0.0.1:5000/predict/cost";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        List<Map<String, Object>> requestDataList;
        try {
            requestDataList = objectMapper.readValue(requestData, new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse request data", e);
        }

        HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(requestDataList, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Map.class
        );

        return response.getBody();
    }
}
