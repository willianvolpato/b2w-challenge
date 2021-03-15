package br.com.b2wchallenge.service.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.b2wchallenge.models.SWAPIPlanet;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SWAPIPlanetSearch {
    SWAPIPlanet[] results;
}
