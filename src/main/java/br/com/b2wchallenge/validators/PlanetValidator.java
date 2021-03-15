package br.com.b2wchallenge.validators;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import br.com.b2wchallenge.models.Planet;
import br.com.b2wchallenge.service.PlanetsService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PlanetValidator implements Validator {

    public static final String NAME = "name";
    public static final String FIELD_REQUIRED = "field.required";
    public static final String FIELD_ALREADY_EXISTS = "field.already.exists";
    public static final String INVALID_FIELD_VALUE = "invalid.field.value";

    @Autowired
    PlanetsService planetService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Planet.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, NAME, FIELD_REQUIRED, new String[] { "Name" },
                "Name isn't defined");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "wather", FIELD_REQUIRED, new String[] { "Weather" },
                "Weather isn't defined");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "terreno", FIELD_REQUIRED, new String[] { "Terrain" },
                "Terrain isn't defined");

        Planet planet = (Planet) target;

        if (planet.getName() == null)
            return;

        if (planetService.loadbyName(planet.getName()) != null) {
            errors.rejectValue(NAME, FIELD_ALREADY_EXISTS,
                new String[] { NAME, planet.getName() },
                String.format("The name [%s] already exists", planet.getName()));
        }

        try {
            if (!errors.hasErrors() && !planetService.isStarWars(planet.getName())) {
                errors.rejectValue(NAME, INVALID_FIELD_VALUE,
                    new String[] { "name", planet.getName() },
                    String.format("The name [%s] is don't a Start Wars franchise", planet.getName()));
            }
        } catch (URISyntaxException e) {
            log.error(e.getLocalizedMessage());
            errors.reject(null, e.getLocalizedMessage());
        }
    }
}
