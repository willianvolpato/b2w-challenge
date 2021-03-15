package br.com.challengeb2w;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import br.com.b2wchallenge.models.Planet;
import br.com.b2wchallenge.models.SWAPIPlanet;

public class BaseTest {

    protected Planet getRandomPlanet() {
        Planet planet = new Planet();

        planet.setName(RandomStringUtils.random(RandomUtils.nextInt(5, 10)));
        planet.setTerrain(RandomStringUtils.random(RandomUtils.nextInt(5, 10)));
        planet.setWeather(RandomStringUtils.random(RandomUtils.nextInt(5, 10)));

        return planet;
    }

    protected int getRandomNumber(int min, int max) {
        return RandomUtils.nextInt(min, max);
    }

    protected SWAPIPlanet getRandomSWAPIPlanet() {
        SWAPIPlanet swapiPlanet = new SWAPIPlanet();

        swapiPlanet.setName(RandomStringUtils.random(RandomUtils.nextInt(5, 10)));

        int numFilms = RandomUtils.nextInt(1, 5);

        if (numFilms <= 0)
            return swapiPlanet;

        String[] films = new String[numFilms];
        for (int i = 0; i < numFilms; i++) {
            films[i] = RandomStringUtils.random(RandomUtils.nextInt(5, 10));
        }
        swapiPlanet.setFilms(films);
        return swapiPlanet;
    }

    protected SWAPIPlanet getRandomSWAPIPlanet(String name) {
        SWAPIPlanet p = getRandomSWAPIPlanet();
        p.setName(name);

        return p;
    }
}
