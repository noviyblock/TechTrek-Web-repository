package com.startupgame.service.game;

import com.startupgame.config.YandexTranslateProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class YandexTranslateService {

    private final YandexTranslateProperties properties;
    private final RestTemplate restTemplate = new RestTemplate();

    public String translateText(String text, String targetLang) {
        String url = properties.getEndpoint();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Api-Key " + properties.getApiKey());

        Map<String, Object> body = new HashMap<>();
        body.put("folderId", properties.getFolderId());
        body.put("texts", List.of(text));
        body.put("targetLanguageCode", targetLang);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        List<Map<String, String>> translations = (List<Map<String, String>>) response.getBody().get("translations");
        return translations.get(0).get("text");
    }
}
