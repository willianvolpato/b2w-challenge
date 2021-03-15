package br.com.b2wchallenge.repositories;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import br.com.b2wchallenge.models.Planet;

public class PlanetRepositoryImpl implements PlanetRepositoryCustomInterface {

    @Autowired
    private MongoTemplate mongoTemplate;
    private Collation ignoreCaseCollation;

    public PlanetRepositoryImpl() {
        this.ignoreCaseCollation = Collation.of(Locale.ENGLISH).strength(Collation.ComparisonLevel.secondary());
    }

    @Override
    public Planet findOneByNameIgnoreCase(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        query.collation(this.ignoreCaseCollation);

        return mongoTemplate.findOne(query, Planet.class);
    }

    @Override
    public Planet findOne(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        query.collation(this.ignoreCaseCollation);

        return mongoTemplate.findOne(query, Planet.class);
    }
}
