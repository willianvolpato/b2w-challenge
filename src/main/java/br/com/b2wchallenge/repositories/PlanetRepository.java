package br.com.b2wchallenge.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.b2wchallenge.models.Planet;

public interface PlanetRepository extends MongoRepository<Planet, String>, PlanetRepositoryCustomInterface {
    Page<Planet> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
