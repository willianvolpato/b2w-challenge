package br.com.challengeb2w.validators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import br.com.b2wchallenge.models.Planet;
import br.com.b2wchallenge.service.PlanetsService;
import br.com.b2wchallenge.validators.PlanetValidator;
import br.com.challengeb2w.BaseTest;

@RunWith(MockitoJUnitRunner.class)
public class PlanetaValidatorTest extends BaseTest{

    public static final String NAME = "name";
    public static final String NAME_ALREADY_EXISTS = "The name [%s] already exists";

    @Mock
    PlanetsService planetService;

    @InjectMocks
    PlanetValidator planetValidator;

    @Test
    public void testSupports() {
        assertThat(planetValidator.supports(Planet.class)).isTrue();
        assertThat(planetValidator.supports(PlanetaValidatorTest.class)).isFalse();
    }

    @Test
    public void testIfValidCallLoadById() {
        Planet planet = getRandomPlanet();

        Errors errors = mock(Errors.class);
        when(errors.hasErrors()).thenReturn(false);

        when(planetService.loadbyName(anyString()))
            .thenReturn(null);
        planetValidator.validate(planet, errors);
        verify(planetService, times(1))
            .loadbyName(planet.getName());
        verify(errors, never()).rejectValue(NAME, "field.already.exists",
            new String[] { NAME, planet.getName() },
            String.format(NAME_ALREADY_EXISTS, planet.getName())
        );

        reset(planetService);
        when(planetService.loadbyName(anyString()))
            .thenReturn(planet);
        planetValidator.validate(planet, errors);
        verify(planetService, times(1))
            .loadbyName(planet.getName());
        verify(errors, times(1)).rejectValue(NAME, "field.already.exists",
            new String[] { NAME, planet.getName() },
            String.format(NAME_ALREADY_EXISTS, planet.getName())
        );
    }
}