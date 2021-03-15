package br.com.b2wchallenge.endpoints;

import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.b2wchallenge.exceptions.PlanetAlreadyExistsException;
import br.com.b2wchallenge.exceptions.PlanetNotFoundException;
import br.com.b2wchallenge.models.Planet;
import br.com.b2wchallenge.service.PlanetsService;
import br.com.b2wchallenge.validators.PlanetValidator;

@RestController()
@RequestMapping("/api/planets")
public class PlanetsRestEndpoint {

    public static final String NAME = "name";
    public static final String PAGE = "page";
    public static final String SIZE = "size";

    @Autowired
    private PlanetsService planetsService;

    @Autowired
    private PlanetValidator planetValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(planetValidator);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
        )
    public Planet createPlanet(@Valid @RequestBody Planet p) throws PlanetAlreadyExistsException, URISyntaxException, PlanetNotFoundException {
        return planetsService.add(p);
    }

    @GetMapping(
            value = "/name/{name}/films",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
        )
    public Integer getAppearancesOfPlanetsByFilm(@PathVariable(name = NAME) String name) throws URISyntaxException, PlanetNotFoundException {
        return planetsService.filmsQuantityByName(name);
    }
    
    @GetMapping(
            value = "/name/{name}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
        )
    public Page<Planet> getPlanetsByName(
        @PathVariable(name = NAME) String name,
        @RequestParam(name = PAGE, required=false) Integer page,
        @RequestParam(name = SIZE, required=false) Integer size
    ) {
        return planetsService.searchByName(name, page, size);
    }

    @GetMapping(
        value = "/{id}/films",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public Integer getPlanetsAppearancesById(@PathVariable String id) throws URISyntaxException, PlanetNotFoundException {
        return planetsService.filmsQuantityById(id);
    }

    @GetMapping(
        value = "/{id}",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public Planet getPlanetById(@PathVariable String id) throws PlanetNotFoundException {
        return planetsService.load(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<Planet> listPlanet(
        @RequestParam(name = PAGE, required = false) Integer page,
        @RequestParam(name = SIZE, required = false) Integer size
    ) {
        return planetsService.list(page, size);
    }

    @DeleteMapping(value = "/{id}")
    public void removePlanet(@PathVariable String id) throws PlanetNotFoundException {
        planetsService.remove(id);
    }
}
