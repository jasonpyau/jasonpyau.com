package com.jasonpyau.service;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonpyau.util.CacheUtil;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SimpleIconsService {

    public static final String EMPTY_SVG = "";
    
    private final JsonNode v6_23_0IconsData = getV6_23_0IconsData();

    private JsonNode getV6_23_0IconsData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(new URL("https://raw.githubusercontent.com/simple-icons/simple-icons/6.23.0/_data/simple-icons.json")).get("icons");
        } catch (IOException e) {
            return objectMapper.createObjectNode();
        }
    }

    @Cacheable(cacheNames = CacheUtil.SIMPLE_ICONS_SVG_CACHE)
    public String getSimpleIconsSvg(String simpleIconsIconSlug) {
        if (simpleIconsIconSlug == null || simpleIconsIconSlug.isBlank()) {
            return EMPTY_SVG;
        }
        RestClient restClient = RestClient.create();
        CompletableFuture<String> first = CompletableFuture.supplyAsync(() -> {
            ResponseEntity<String> res = restClient
                                        .get()
                                        .uri("https://cdn.simpleicons.org/"+simpleIconsIconSlug)
                                        .retrieve()
                                        .toEntity(String.class);
            return res.getBody();
        });
        CompletableFuture<String> second = CompletableFuture.supplyAsync(() -> {
            ResponseEntity<String> res = restClient
                                        .get()
                                        .uri("https://raw.githubusercontent.com/simple-icons/simple-icons/6.23.0/icons/"+simpleIconsIconSlug+".svg")
                                        .retrieve()
                                        .toEntity(String.class);
            return res.getBody();
        });
        try {
            return first.join();
        } catch (CompletionException e) {}
        // If we got here, we used simple-icons/6.23.0.
        try {
            String svg = second.join();
            Matcher matcher = Pattern.compile("<title>(.*?)</title>").matcher(svg);
            if (matcher.find() && v6_23_0IconsData != null) {
                String title = matcher.group(1);
                for (JsonNode data : v6_23_0IconsData) {
                    if (data.get("title").asText().equals(title)) {
                        svg = replaceSvgFill(svg, "#"+data.get("hex").asText());
                        break;
                    }
                }
            }
            return svg;
        } catch (CompletionException e) {}
        return EMPTY_SVG;
    }

    public String replaceSvgFill(String simpleIconsSvg, String svgFill) {
        simpleIconsSvg = simpleIconsSvg.replaceAll("[\\s]*fill[\\s]*=[\\s]*\"(.*?)\"[\\s]*", " ");
        simpleIconsSvg = simpleIconsSvg.replace("<svg", String.format("<svg fill=\"%s\"", svgFill));
        return simpleIconsSvg;
    }
}
