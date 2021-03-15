package br.com.challengeb2w.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import br.com.b2wchallenge.exceptions.PlanetAlreadyExistsException;
import br.com.b2wchallenge.exceptions.PlanetNotFoundException;
import br.com.b2wchallenge.models.Planet;
import br.com.b2wchallenge.models.SWAPIPlanet;
import br.com.b2wchallenge.repositories.PlanetRepository;
import br.com.b2wchallenge.service.PlanetsService;
import br.com.b2wchallenge.service.SWAPIPlanetService;
import br.com.challengeb2w.BaseTest;

@RunWith(MockitoJUnitRunner.class)
public class PlanetsServiceTest extends BaseTest {

    @Mock
    private PlanetRepository mockPlanetRepository;

    @Mock
    private SWAPIPlanetService mockSWApiPlanetService;

    @InjectMocks
    private PlanetsService service;

    @Test
    public void testAddPlanet() throws Exception {
        final Planet planet = getRandomPlanet();
        SWAPIPlanet swapiPlanet = getRandomSWAPIPlanet();
        List<SWAPIPlanet> search = new ArrayList<>();

        swapiPlanet.setName(planet.getName());
        search.add(swapiPlanet);
        when(mockSWApiPlanetService.getSWAPIPlanet(anyString()))
            .thenReturn(search);
        when(mockPlanetRepository.findOneByNameIgnoreCase(anyString()))
            .thenReturn(null);

        service.add(planet);

        verify(mockPlanetRepository, atLeastOnce())
            .findOneByNameIgnoreCase(planet.getName());
        verify(mockSWApiPlanetService, times(1))
            .getSWAPIPlanet(planet.getName());
        verify(mockPlanetRepository, times(1))
            .save(planet);

        reset(mockPlanetRepository, mockSWApiPlanetService);
        search.clear();

        when(mockSWApiPlanetService.getSWAPIPlanet(anyString()))
            .thenReturn(search);
        when(mockPlanetRepository.findOneByNameIgnoreCase(anyString()))
            .thenReturn(planet);

        assertThatExceptionOfType(PlanetAlreadyExistsException.class)
            .isThrownBy(
                ()->service.add(planet)
            ).withMessageContaining(planet.toString());

        verify(mockPlanetRepository, atLeastOnce())
            .findOneByNameIgnoreCase(planet.getName());
        verify(mockSWApiPlanetService, never())
            .getSWAPIPlanet(planet.getName());
        verify(mockPlanetRepository, never())
            .save(any(Planet.class));

        reset(mockPlanetRepository, mockSWApiPlanetService);
        search.clear();

        when(mockSWApiPlanetService.getSWAPIPlanet(anyString()))
            .thenReturn(null);
        when(mockPlanetRepository.findOneByNameIgnoreCase(anyString()))
            .thenReturn(null);

        assertThatExceptionOfType(PlanetNotFoundException.class)
            .isThrownBy(
                () -> service.add(planet)
            ).withMessageContaining(planet.getName());

        verify(mockPlanetRepository, atLeastOnce())
            .findOneByNameIgnoreCase(planet.getName());
        verify(mockSWApiPlanetService, times(1))
            .getSWAPIPlanet(planet.getName());
        verify(mockPlanetRepository, never())
            .save(any(Planet.class));

        reset(mockPlanetRepository, mockSWApiPlanetService);
        search.clear();

        when(mockSWApiPlanetService.getSWAPIPlanet(anyString()))
            .thenReturn(search);
        when(mockPlanetRepository.findOneByNameIgnoreCase(anyString()))
            .thenReturn(null);

        assertThatExceptionOfType(PlanetNotFoundException.class)
            .isThrownBy(
                () -> service.add(planet)
            ).withMessageContaining(planet.getName());

        verify(mockPlanetRepository, atLeastOnce())
            .findOneByNameIgnoreCase(planet.getName());
        verify(mockSWApiPlanetService, times(1))
            .getSWAPIPlanet(planet.getName());
        verify(mockPlanetRepository, never())
            .save(any(Planet.class));
    }

    @Test
    public void testLoadFindOneInRepository() throws PlanetNotFoundException {
        final String ID = "Planet_id";
        Planet planet = getRandomPlanet();

        when(mockPlanetRepository.findOne(anyString())).thenReturn(planet);
        service.load(ID);

        verify(mockPlanetRepository, times(1))
            .findOne(ID);

        reset(mockPlanetRepository);
        when(mockPlanetRepository.findOne(anyString()))
            .thenReturn(null);
        assertThatExceptionOfType(PlanetNotFoundException.class)
            .isThrownBy(
                ()->service.load(ID)
            ).withMessageContaining(ID);
    }

    @Test
    public void teste_isStarWars() throws URISyntaxException {
        final String NAME = "Planet SW 01";
        List<SWAPIPlanet> result = new ArrayList<>();

        when(mockSWApiPlanetService.getSWAPIPlanet(anyString()))
            .thenReturn(result);

        assertThat(service.isStarWars(NAME)).isFalse();
        verify(mockSWApiPlanetService, times(1))
            .getSWAPIPlanet(NAME);

        reset(mockSWApiPlanetService);
        when(mockSWApiPlanetService.getSWAPIPlanet(anyString()))
            .thenReturn(null);

        assertThat(service.isStarWars(NAME)).isFalse();
        verify(mockSWApiPlanetService, times(1))
            .getSWAPIPlanet(NAME);

        reset(mockSWApiPlanetService);
        int testSize = getRandomNumber(5, 50);
        while(result.size() < testSize) {
            result.add(getRandomSWAPIPlanet());
        }

        when(mockSWApiPlanetService.getSWAPIPlanet(anyString()))
            .thenReturn(result);

        assertThat(service.isStarWars(NAME)).isFalse();
        verify(mockSWApiPlanetService, times(1))
            .getSWAPIPlanet(NAME);

        reset(mockSWApiPlanetService);
        result.clear();

        testSize = getRandomNumber(5, 50);
        while(result.size() < testSize) {
            result.add(getRandomSWAPIPlanet());
        }
        result.add(getRandomSWAPIPlanet(NAME));
        result.add(getRandomSWAPIPlanet(NAME.toLowerCase()));
        result.add(getRandomSWAPIPlanet(NAME.toUpperCase()));

        when(mockSWApiPlanetService.getSWAPIPlanet(anyString()))
            .thenReturn(result);

        assertThat(service.isStarWars(NAME))
            .isTrue();
        verify(mockSWApiPlanetService, times(1))
            .getSWAPIPlanet(NAME);
    }

    @Test
    public void testeLoadByNameFindOneBIgnoreCaseInRepository() {
        final String NAME = "Planet_Name";
        service.loadbyName(NAME);

        verify(mockPlanetRepository, times(1))
            .findOneByNameIgnoreCase(NAME);
    }

    @Test
    public void testRemovePlanet() throws PlanetNotFoundException {
        Planet Planet = getRandomPlanet();
        Planet.setId("p_id");

        when(mockPlanetRepository.findOne(anyString()))
            .thenReturn(null);

        assertThatExceptionOfType(PlanetNotFoundException.class)
            .isThrownBy(
                ()->service.remove(Planet.getId())
            ).withMessageContaining(Planet.getId());
        
        verify(mockPlanetRepository, times(1))
            .findOne(Planet.getId());
        verify(mockPlanetRepository, never())
            .delete(any(Planet.class));

        reset(mockPlanetRepository);

        when(mockPlanetRepository.findOne(anyString()))
            .thenReturn(Planet);

        service.remove(Planet.getId());

        verify(mockPlanetRepository, times(1))
            .findOne(Planet.getId());
        verify(mockPlanetRepository, times(1))
             .delete(Planet);
    }

    @Test
    public void testeSearchByNameWithoutPaginationAndOrder() {
        when(mockPlanetRepository.count())
            .thenReturn(1l);
        final String NAME = "Test";
        Pageable p = PageRequest.of(0, 1, Sort.Direction.ASC, "name");

        service.searchByName(NAME);

        verify(mockPlanetRepository, times(1))
            .findByNameContainingIgnoreCase(NAME, p);
    }

    @Test
    public void testSearchByNameWithDescOrderOnIdWithoutPagination() {
        when(mockPlanetRepository.count()).thenReturn(1l);
        final String NAME = "Test";
        Pageable p = PageRequest.of(0, 1, Sort.Direction.DESC, "id");

        service.searchByName(NAME, null, null, "id", true);

        verify(mockPlanetRepository, times(1)).findByNameContainingIgnoreCase(NAME, p);
    }

    @Test
    public void testSearchByNameWithPagination() {
        final String NAME = "Test";

        Pageable p = PageRequest.of(12, 50, Sort.Direction.ASC, "name");

        service.searchByName(NAME, 12, 50);
        verify(mockPlanetRepository, times(1)).findByNameContainingIgnoreCase(NAME, p);
    }

    @Test
    public void testListWithoutPaginationAndOrder() {
        when(mockPlanetRepository.count())
            .thenReturn(1l);

        Pageable p = PageRequest.of(0, 1, Sort.Direction.ASC, "name");

        service.list();

        verify(mockPlanetRepository, times(1))
            .findAll(p);
    }

    @Test
    public void testListWitOrderByDescIdWithoutPagination() {
        when(mockPlanetRepository.count())
            .thenReturn(1l);
        Pageable p = PageRequest.of(0, 1, Sort.Direction.DESC, "id");

        service.list(null, null, "id", true);

        verify(mockPlanetRepository, times(1)).findAll(p);
    }

    @Test
    public void testListWithPagination() {
        Pageable p = PageRequest.of(12, 50, Sort.Direction.ASC, "name");

        service.list(12, 50);
        verify(mockPlanetRepository, times(1))
            .findAll(p);
    }

    @Test
    public void testFilmQuantityByName() throws URISyntaxException, PlanetNotFoundException {
        final String NAME = "Planet SW 01";
        List<SWAPIPlanet> result = new ArrayList<>();

        when(mockPlanetRepository.findOneByNameIgnoreCase(anyString()))
            .thenReturn(null);
        when(mockSWApiPlanetService.getSWAPIPlanet(anyString()))
            .thenReturn(result);

        assertThatExceptionOfType(PlanetNotFoundException.class)
            .isThrownBy(
                ()->service.filmsQuantityByName(NAME)
            ).withMessageContaining(NAME);
        verify(mockPlanetRepository, times(1))
            .findOneByNameIgnoreCase(NAME);
        verify(mockSWApiPlanetService, times(1))
            .getSWAPIPlanet(NAME);

        reset(mockSWApiPlanetService, mockPlanetRepository);
        when(mockPlanetRepository.findOneByNameIgnoreCase(anyString()))
            .thenReturn(null);
        when(mockSWApiPlanetService.getSWAPIPlanet(anyString()))
            .thenReturn(null);

        assertThatExceptionOfType(PlanetNotFoundException.class)
            .isThrownBy(
                ()->service.filmsQuantityByName(NAME)
            ).withMessageContaining(NAME);
        verify(mockPlanetRepository, times(1))
            .findOneByNameIgnoreCase(NAME);
        verify(mockSWApiPlanetService, times(1))
            .getSWAPIPlanet(NAME);

        reset(mockSWApiPlanetService, mockPlanetRepository);
        result.clear();

        Planet planet = getRandomPlanet();
        when(mockPlanetRepository.findOneByNameIgnoreCase(anyString()))
            .thenReturn(planet);
        when(mockSWApiPlanetService.getSWAPIPlanet(anyString()))
            .thenReturn(null);

        assertThat(service.filmsQuantityByName(NAME))
            .isEqualTo(planet.getFilms());
        verify(mockPlanetRepository, times(1))
            .findOneByNameIgnoreCase(NAME);
        verify(mockSWApiPlanetService, never())
            .getSWAPIPlanet(NAME);

        reset(mockSWApiPlanetService, mockPlanetRepository);
        SWAPIPlanet swapiPanet01 = getRandomSWAPIPlanet();
        swapiPanet01.setName(NAME);
        result.add(swapiPanet01);

        when(mockPlanetRepository.findOneByNameIgnoreCase(anyString()))
            .thenReturn(null);
        when(mockSWApiPlanetService.getSWAPIPlanet(anyString()))
            .thenReturn(result);

        assertThat(service.filmsQuantityByName(NAME))
            .isEqualTo(swapiPanet01.getFilms().length);
        verify(mockPlanetRepository, times(1))
            .findOneByNameIgnoreCase(NAME);
        verify(mockSWApiPlanetService, times(1))
            .getSWAPIPlanet(NAME);

        reset(mockSWApiPlanetService, mockPlanetRepository);
        result.clear();
        swapiPanet01 = getRandomSWAPIPlanet(NAME);

        swapiPanet01.setFilms(null);
        result.add(swapiPanet01);
        when(mockPlanetRepository.findOneByNameIgnoreCase(anyString()))
            .thenReturn(null);
        when(mockSWApiPlanetService.getSWAPIPlanet(anyString()))
            .thenReturn(result);

        assertThat(service.filmsQuantityByName(NAME))
            .isEqualTo(0);
        verify(mockPlanetRepository, times(1))
            .findOneByNameIgnoreCase(NAME);
        verify(mockSWApiPlanetService, times(1))
            .getSWAPIPlanet(NAME);

        reset(mockSWApiPlanetService, mockPlanetRepository);
        result.clear();
        swapiPanet01 = getRandomSWAPIPlanet(NAME);

        swapiPanet01.setFilms(new String[0]);
        result.add(swapiPanet01);
        when(mockPlanetRepository.findOneByNameIgnoreCase(anyString()))
            .thenReturn(null);
        when(mockSWApiPlanetService.getSWAPIPlanet(anyString()))
            .thenReturn(result);

        assertThat(service.filmsQuantityByName(NAME))
            .isEqualTo(0);
        verify(mockPlanetRepository, times(1))
            .findOneByNameIgnoreCase(NAME);
        verify(mockSWApiPlanetService, times(1))
            .getSWAPIPlanet(NAME);

        reset(mockSWApiPlanetService, mockPlanetRepository);
        result.clear();
        swapiPanet01 = getRandomSWAPIPlanet(NAME);
        result.add(swapiPanet01);
        result.add(getRandomSWAPIPlanet());
        result.add(getRandomSWAPIPlanet());
        result.add(getRandomSWAPIPlanet());
        when(mockPlanetRepository.findOneByNameIgnoreCase(anyString()))
            .thenReturn(null);
        when(mockSWApiPlanetService.getSWAPIPlanet(anyString()))
            .thenReturn(result);

        assertThat(service.filmsQuantityByName(NAME))
            .isEqualTo(swapiPanet01.getFilms().length);
        verify(mockPlanetRepository, times(1))
            .findOneByNameIgnoreCase(NAME);
        verify(mockSWApiPlanetService, times(1))
            .getSWAPIPlanet(NAME);

        reset(mockSWApiPlanetService, mockPlanetRepository);
        result.clear();
        swapiPanet01 = getRandomSWAPIPlanet(NAME);

        swapiPanet01.setFilms(null);
        result.add(swapiPanet01);
        result.add(getRandomSWAPIPlanet());
        result.add(getRandomSWAPIPlanet());
        result.add(getRandomSWAPIPlanet());
        when(mockPlanetRepository.findOneByNameIgnoreCase(anyString()))
            .thenReturn(null);
        when(mockSWApiPlanetService.getSWAPIPlanet(anyString()))
            .thenReturn(result);

        assertThat(service.filmsQuantityByName(NAME))
            .isEqualTo(0);
        verify(mockPlanetRepository, times(1))
            .findOneByNameIgnoreCase(NAME);
        verify(mockSWApiPlanetService, times(1))
            .getSWAPIPlanet(NAME);

        reset(mockSWApiPlanetService, mockPlanetRepository);
        result.clear();
        swapiPanet01 = getRandomSWAPIPlanet(NAME);

        swapiPanet01.setFilms(new String[0]);
        result.add(swapiPanet01);
        result.add(getRandomSWAPIPlanet());
        result.add(getRandomSWAPIPlanet());
        result.add(getRandomSWAPIPlanet());
        when(mockPlanetRepository.findOneByNameIgnoreCase(anyString()))
            .thenReturn(null);
        when(mockSWApiPlanetService.getSWAPIPlanet(anyString()))
            .thenReturn(result);

        assertThat(service.filmsQuantityByName(NAME))
            .isEqualTo(0);
        verify(mockPlanetRepository, times(1))
            .findOneByNameIgnoreCase(NAME);
        verify(mockSWApiPlanetService, times(1))
            .getSWAPIPlanet(NAME);

        reset(mockSWApiPlanetService, mockPlanetRepository);
        result.clear();
        result.add(getRandomSWAPIPlanet());
        result.add(getRandomSWAPIPlanet());
        result.add(getRandomSWAPIPlanet());
        result.add(getRandomSWAPIPlanet());
        when(mockPlanetRepository.findOneByNameIgnoreCase(anyString()))
            .thenReturn(null);
        when(mockSWApiPlanetService.getSWAPIPlanet(anyString()))
            .thenReturn(result);

        assertThat(service.filmsQuantityByName(NAME))
            .isEqualTo(0);
        verify(mockPlanetRepository, times(1))
            .findOneByNameIgnoreCase(NAME);
        verify(mockSWApiPlanetService, times(1))
            .getSWAPIPlanet(NAME);

        reset(mockSWApiPlanetService, mockPlanetRepository);
        result.clear();
        swapiPanet01 = getRandomSWAPIPlanet(NAME);
        SWAPIPlanet swapiPlanet02 = getRandomSWAPIPlanet(NAME);
        SWAPIPlanet swapiPLanet03 = getRandomSWAPIPlanet(NAME);
        swapiPLanet03.setFilms(null);
        SWAPIPlanet p4 = getRandomSWAPIPlanet(NAME);
        p4.setFilms(new String[0]);
        result.add(swapiPanet01);
        result.add(swapiPlanet02);
        result.add(swapiPLanet03);
        result.add(getRandomSWAPIPlanet());
        result.add(getRandomSWAPIPlanet());
        when(mockPlanetRepository.findOneByNameIgnoreCase(anyString()))
            .thenReturn(null);
        when(mockSWApiPlanetService.getSWAPIPlanet(anyString()))
            .thenReturn(result);

        assertThat(service.filmsQuantityByName(NAME))
            .isEqualTo(swapiPanet01.getFilms().length + swapiPlanet02.getFilms().length);
        verify(mockPlanetRepository, times(1))
            .findOneByNameIgnoreCase(NAME);
        verify(mockSWApiPlanetService, times(1))
            .getSWAPIPlanet(NAME);
    }

    @Test
    public void testFilmsQuantityByIdWithoutGetSWAPIPlaneta() throws URISyntaxException, PlanetNotFoundException {
        Planet planet = getRandomPlanet();
        planet.setId("Planet-id");

        when(mockPlanetRepository.findOne(anyString()))
            .thenReturn(null);

        assertThatExceptionOfType(PlanetNotFoundException.class)
            .isThrownBy(
                ()->service.filmsQuantityById(planet.getId())
            ).withMessageContaining(planet.getId());

        verify(mockPlanetRepository, times(1))
            .findOne(planet.getId());
        verify(mockSWApiPlanetService, never())
            .getSWAPIPlanet(planet.getName());

        reset(mockPlanetRepository, mockSWApiPlanetService);

        when(mockPlanetRepository.findOne(anyString()))
            .thenReturn(planet);

        assertThat(service.filmsQuantityById(planet.getId()))
            .isEqualTo(planet.getFilms());
        verify(mockPlanetRepository, times(1))
            .findOne(planet.getId());
        verify(mockSWApiPlanetService, never())
            .getSWAPIPlanet(planet.getName());
    }
}