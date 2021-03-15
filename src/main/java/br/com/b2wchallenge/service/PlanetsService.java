package br.com.b2wchallenge.service;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.b2wchallenge.exceptions.PlanetAlreadyExistsException;
import br.com.b2wchallenge.exceptions.PlanetNotFoundException;
import br.com.b2wchallenge.models.Planet;
import br.com.b2wchallenge.models.SWAPIPlanet;
import br.com.b2wchallenge.repositories.PlanetRepository;
import lombok.extern.slf4j.Slf4j;

@Service
public class PlanetsService {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private SWAPIPlanetService swApiPlanetService;

    public Planet add(Planet p) throws PlanetAlreadyExistsException, URISyntaxException, PlanetNotFoundException {
        if (planetRepository.findOneByNameIgnoreCase(p.getName()) != null) {
            PlanetAlreadyExistsException ex = new PlanetAlreadyExistsException();
            ex.setPlanet(p);
            throw ex;
        }

        Integer films = this.filmsQuantityByName(p.getName());
        p.setFilms(films);

        return planetRepository.save(p);
    }

    public Planet load(String id) throws PlanetNotFoundException {
        Planet p = planetRepository.findOne(id);
        if (p == null) {
            PlanetNotFoundException ex = new PlanetNotFoundException();
            ex.setId(id);
            throw ex;
        }
        return p;
    }

    public Planet loadbyName(String name) {
        return planetRepository.findOneByNameIgnoreCase(name);
    }

    public Page<Planet> searchByName(String name, Integer page, Integer byPage, String sort, Boolean reverse) {
        Pageable pageable = getPageable(page, byPage, sort, reverse);

        return planetRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<Planet> searchByName(String name, Integer page, Integer byPage, String sort) {
        return this.searchByName(name, page, byPage, sort, false);
    }

    public Page<Planet> searchByName(String name, Integer page, Integer byPage) {
        return this.searchByName(name, page, byPage, "name");
    }

    public Page<Planet> searchByName(String name) {
        return this.searchByName(name, null, null);
    }

    public Page<Planet> list(Integer page, Integer byPage, String sort, Boolean reverse) {
        Pageable pageable = getPageable(page, byPage, sort, reverse);
           
        return planetRepository.findAll(pageable);
    }

    public Page<Planet> list(Integer page, Integer byPage, String sort) {
        return this.list(page, byPage, sort, false);
    }

    public Page<Planet> list(Integer page, Integer byPage) {
        return this.list(page, byPage, "name");
    }

    public Page<Planet> list() {
        return this.list(null, null);
    }

    public void remove(String id) throws PlanetNotFoundException {
        Planet p = planetRepository.findOne(id);
        if (p == null) {
            PlanetNotFoundException ex = new PlanetNotFoundException();
            ex.setId(id);
            throw ex;
        }
        planetRepository.delete(p);
    }

    public Integer filmsQuantityById(String id) throws URISyntaxException, PlanetNotFoundException {
        Planet p = planetRepository.findOne(id);

        if (p == null) {
            PlanetNotFoundException ex = new PlanetNotFoundException();
            ex.setId(id);
            throw ex;
        }
        return p.getFilms();
    }

    public Integer filmsQuantityByName(String name) throws URISyntaxException, PlanetNotFoundException {
        Planet planet = planetRepository.findOneByNameIgnoreCase(name);
        if (planet != null) {
            return planet.getFilms();
        }
        List<SWAPIPlanet> swAPIPlanets = swApiPlanetService.getSWAPIPlanet(name);
        if (swAPIPlanets == null || swAPIPlanets.isEmpty()) {
            PlanetNotFoundException ex = new PlanetNotFoundException();
            ex.setId(name);
            throw ex;
        }

        return swAPIPlanets.stream()
            .filter(p -> p.getName().equalsIgnoreCase(name))
            .map(p -> {
                if(p.getFilms() == null)
                    return 0;
                return p.getFilms().length;
            }).mapToInt(Integer::intValue)
            .sum();
    }

    public boolean isStarWars(String name) throws URISyntaxException {
        List<SWAPIPlanet> swAPIPlanets = swApiPlanetService.getSWAPIPlanet(name);
        if (swAPIPlanets == null || swAPIPlanets.isEmpty()) {
            return false;
        }

        return (swAPIPlanets.stream()
            .filter(p -> p.getName().equalsIgnoreCase(name))
            .count()) > 0;
    }

    private Pageable getPageable(Integer page, Integer byPage, String sort, Boolean reverse) {
        if (page == null) {
            page = 0;
            byPage = Long.valueOf(planetRepository.count()).intValue();
        }

        if (byPage == null)
            byPage = 10;

        Direction ordDirection = getDirection(reverse);
        return PageRequest.of(page, byPage, ordDirection, sort);
    }

    private Direction getDirection(Boolean reverse) {
        return (reverse != null && reverse.equals(Boolean.TRUE)) ? Direction.DESC : Direction.ASC;
    }
}
