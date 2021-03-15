package br.com.b2wchallenge.service;

import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.b2wchallenge.models.SWAPIPlanet;
import br.com.b2wchallenge.service.utils.SWAPIPlanetSearch;

@Service
public class SWAPIPlanetService {
    public static final String SWAPI_BASE_URL = "https://swapi.co";
    public static final String SWAPI_PLANETS_RESOURCE = "/api/planets/";

    @Autowired
    private RestTemplate restTemplate;

    public List<SWAPIPlanet> getSWAPIPlanet(String name) throws URISyntaxException {
        URIBuilder searchURL = new URIBuilder(SWAPIPlanetService.SWAPI_BASE_URL)
            .setPath(SWAPIPlanetService.SWAPI_PLANETS_RESOURCE)
            .addParameter("search", name)
            .setCharset(Charset.forName("UTF-8"));

        SWAPIPlanetSearch results = restTemplate.getForObject(searchURL.build(), SWAPIPlanetSearch.class);

        if (results == null || results.getResults() == null)
            return Collections.emptyList();

        return Arrays.stream(results.getResults()).collect(Collectors.toList());
    }
}
