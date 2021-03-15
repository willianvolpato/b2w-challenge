package br.com.b2wchallenge.repositories;

import br.com.b2wchallenge.models.Planet;

public interface PlanetRepositoryCustomInterface {
    public Planet findOneByNameIgnoreCase(String name);
    public Planet findOne(String id);
}
