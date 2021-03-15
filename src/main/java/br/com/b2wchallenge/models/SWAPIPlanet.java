package br.com.b2wchallenge.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SWAPIPlanet {
    String name;
    String[] films;
}
