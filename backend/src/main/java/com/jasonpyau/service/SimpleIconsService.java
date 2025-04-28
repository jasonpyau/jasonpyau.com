package com.jasonpyau.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonpyau.util.CacheUtil;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SimpleIconsService {

    public static final String EMPTY_SVG = "";
    
    private final HashMap<String, JsonNode> v6_23_0IconsData = getV6_23_0IconsData();

    private HashMap<String, JsonNode> getV6_23_0IconsData() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            URL url = new URI("https://raw.githubusercontent.com/simple-icons/simple-icons/6.23.0/_data/simple-icons.json").toURL();
            JsonNode iconsDataArray = objectMapper.readTree(url).get("icons");
            HashMap<String, JsonNode> res = new HashMap<>();
            for (JsonNode data : iconsDataArray) {
                res.put(data.get("title").asText(), data);
            }
            return res;
        } catch (IOException | URISyntaxException e) {
            return new HashMap<>();
        }
    }

    @Cacheable(cacheNames = CacheUtil.SIMPLE_ICONS_SVG_CACHE)
    public String getSimpleIconsSvg(String simpleIconsIconSlug) {
        if (!StringUtils.hasText(simpleIconsIconSlug)) {
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
            String svg = first.join();
            if (StringUtils.hasText(svg)) {
                return svg;
            }
        } catch (CompletionException e) {}
        // If we got here, we used simple-icons/6.23.0.
        try {
            String svg = second.join();
            if (StringUtils.hasText(svg)) {
                Matcher matcher = Pattern.compile("<title>(.*?)</title>").matcher(svg);
                if (matcher.find()) {
                    String title = matcher.group(1);
                    JsonNode iconData = v6_23_0IconsData.get(title);
                    if (iconData != null) {
                        svg = replaceSvgFill(svg, "#"+iconData.get("hex").asText());
                    }
                }
                return svg;
            }
        } catch (CompletionException e) {}
        return EMPTY_SVG;
    }

    public String replaceSvgFill(String simpleIconsSvg, String svgFill) {
        simpleIconsSvg = simpleIconsSvg.replaceAll("[\\s]*fill[\\s]*=[\\s]*\"(.*?)\"[\\s]*", " ");
        simpleIconsSvg = simpleIconsSvg.replace("<svg", String.format("<svg fill=\"%s\"", svgFill));
        return simpleIconsSvg;
    }
}
