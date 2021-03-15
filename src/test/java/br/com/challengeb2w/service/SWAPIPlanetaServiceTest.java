package br.com.challengeb2w.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.client.utils.URIBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import br.com.b2wchallenge.models.SWAPIPlanet;
import br.com.b2wchallenge.service.SWAPIPlanetService;
import br.com.b2wchallenge.service.utils.SWAPIPlanetSearch;
import br.com.challengeb2w.BaseTest;

@RunWith(MockitoJUnitRunner.class)
public class SWAPIPlanetaServiceTest extends BaseTest {
    public static final String SWAPI_BASE_URL = "https://swapi.co";
    public static final String SWAPI_PLANETS_RESOURCE = "/api/planets/";

    @Mock
    private RestTemplate mockRestTemplate;

    @InjectMocks
    private SWAPIPlanetService swService;

    @Test
    public void getSWAPIPlanetaCallRestTemplateGetForObject() throws URISyntaxException {
        final String NAME = "Planet SW 01";
        SWAPIPlanet p = getRandomSWAPIPlanet();

        URIBuilder searchURL = new URIBuilder("https://swapi.co")
            .setPath("/api/planets/")
            .addParameter("search", NAME)
            .setCharset(Charset.forName("UTF-8"));

        SWAPIPlanetSearch mockSearch = mock(SWAPIPlanetSearch.class);

        when(mockSearch.getResults()).thenReturn(new SWAPIPlanet[]{p});
        when(mockRestTemplate.getForObject(any(URI.class), any()))
            .thenReturn(mockSearch);

        List<SWAPIPlanet> result = swService.getSWAPIPlanet(NAME);
        assertThat(result).hasSize(1);
        assertThat(result).contains(p);

        verify(mockRestTemplate, times(1))
            .getForObject(searchURL.build(), SWAPIPlanetSearch.class);
        verify(mockSearch, times(2)).getResults();

        reset(mockSearch, mockRestTemplate);

        when(mockSearch.getResults())
            .thenReturn(null);
        when(mockRestTemplate.getForObject(any(URI.class), any()))
            .thenReturn(mockSearch);

        result = swService.getSWAPIPlanet(NAME);
        assertThat(result).isEmpty();

        verify(mockRestTemplate, times(1))
            .getForObject(searchURL.build(), SWAPIPlanetSearch.class);
        verify(mockSearch, times(1))
            .getResults();

        reset(mockSearch, mockRestTemplate);

        when(mockSearch.getResults())
            .thenReturn(new SWAPIPlanet[0]);
        when(mockRestTemplate.getForObject(any(URI.class), any()))
            .thenReturn(mockSearch);

        result = swService.getSWAPIPlanet(NAME);
        assertThat(result).isEmpty();

        verify(mockRestTemplate, times(1))
            .getForObject(searchURL.build(), SWAPIPlanetSearch.class);
        verify(mockSearch, times(2)).getResults();

        reset(mockSearch, mockRestTemplate);
        when(mockRestTemplate.getForObject(any(URI.class), any()))
            .thenReturn(null);

        result = swService.getSWAPIPlanet(NAME);
        assertThat(result).isEmpty();

        verify(mockRestTemplate, times(1))
            .getForObject(searchURL.build(), SWAPIPlanetSearch.class);
        verify(mockSearch, never())
            .getResults();
    }
}
