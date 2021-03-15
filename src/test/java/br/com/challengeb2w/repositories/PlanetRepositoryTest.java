package br.com.challengeb2w.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.b2wchallenge.models.Planet;
import br.com.b2wchallenge.repositories.PlanetRepository;
import br.com.challengeb2w.BaseTest;

@RunWith(SpringRunner.class)
@DataMongoTest
public class PlanetRepositoryTest extends BaseTest{

    @Autowired
    private PlanetRepository planetRepository;

    @Before
    public void beforeEach() {
        planetRepository.deleteAll();
        assertThat(planetRepository.count()).isEqualTo(0l);
    }

    @Test
    public void testCreate() {
        Planet expected = getRandomPlanet();

        Planet returned = planetRepository.save(expected);
        assertThat(returned.getId()).isNotNull();
        assertThat(returned).isEqualTo(expected);

        Planet sameName = getRandomPlanet();
        sameName.setName(expected.getName());

        assertThat(sameName.getId()).isNull();
        assertThat(sameName.getName()).isEqualTo(expected.getName());
        try {
            planetRepository.save(sameName);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(DuplicateKeyException.class);
            return;
        }
        Assert.fail("Nothing exception was thrown");
    }

    @Test
    public void testFindAll() {
        assertThat(planetRepository.findAll()).isEmpty();

        planetRepository.save(getRandomPlanet());
        planetRepository.save(getRandomPlanet());
        planetRepository.save(getRandomPlanet());
        planetRepository.save(getRandomPlanet());
        planetRepository.save(getRandomPlanet());

        List<Planet> returned = planetRepository.findAll();

        assertThat(returned).hasSize(5);
    }

    @Test
    public void testSortByNameFindAll() {
        assertThat(planetRepository.findAll()).isEmpty();

        Sort asc = new Sort(Sort.Direction.ASC, "name");
        Sort desc = new Sort(Sort.Direction.DESC, "name");

        LinkedList<Planet> ordList = new LinkedList<>();
        LinkedList<Planet> descList = new LinkedList<>();
        for(int i =0; i < 100; i++) {
            Planet p = this.getRandomPlanet();
            p.setName(String.format("Planet %03d", i+1));
            planetRepository.save(p);
            ordList.add(i, p);
            descList.addFirst(p);
        }

        List<Planet> result = planetRepository.findAll(asc);
        assertThat(result).hasSize(100);
        assertThat(result).containsExactlyElementsOf(ordList);

        result = planetRepository.findAll(desc);
        assertThat(result).hasSize(100);
        assertThat(result).containsExactlyElementsOf(descList);
    }

    @Test
    public void testPaginationFindAll() {
        assertThat(planetRepository.findAll()).isEmpty();

        Sort asc = new Sort(Sort.Direction.ASC, "name");
        Sort desc = new Sort(Sort.Direction.DESC, "name");

        LinkedList<Planet> ordList = new LinkedList<>();
        LinkedList<Planet> descList = new LinkedList<>();
        for(int i =0; i < 100; i++) {
            Planet p = this.getRandomPlanet();
            p.setName(String.format("Planet %03d", i+1));
            planetRepository.save(p);
            ordList.add(i, p);
            descList.addFirst(p);
        }

        for(int i=0; i<4; i++) {
            Page<Planet> pResult = planetRepository.findAll(PageRequest.of(i, 25, asc));
            assertThat(pResult).hasSize(25);
            assertThat(pResult.getContent()).containsExactlyElementsOf(ordList.subList(i*25, (i*25)+25));
        }

        for(int i=0; i<4; i++) {
            Page<Planet> pResult = planetRepository.findAll(PageRequest.of(i, 25, desc));
            assertThat(pResult).hasSize(25);
            assertThat(pResult.getContent()).containsExactlyElementsOf(descList.subList(i*25, (i*25)+25));
        }
    }

    @Test
    public void testOrderByNameAndPaginationFindByNameContainingIgnoreCase() {
        Sort asc = new Sort(Sort.Direction.ASC, "name");
        Sort desc = new Sort(Sort.Direction.DESC, "name");

        LinkedList<Planet> ordList = new LinkedList<>();
        LinkedList<Planet> descList = new LinkedList<>();
        for(int i =0; i < 100; i++) {
            Planet p = this.getRandomPlanet();
            p.setName(String.format("Planet %03d", i+1));
            planetRepository.save(p);
            ordList.add(i, p);
            descList.addFirst(p);
        }

        for(int i=0; i<4; i++) {
            Page<Planet> pResult = planetRepository.findByNameContainingIgnoreCase("PlaN", PageRequest.of(i, 25, asc));
            assertThat(pResult).hasSize(25);
            assertThat(pResult.getContent()).containsExactlyElementsOf(ordList.subList(i*25, (i*25)+25));
        }

        for(int i=0; i<4; i++) {
            Page<Planet> pResult = planetRepository.findByNameContainingIgnoreCase("lanE", PageRequest.of(i, 25, desc));
            assertThat(pResult).hasSize(25);
            assertThat(pResult.getContent()).containsExactlyElementsOf(descList.subList(i*25, (i*25)+25));
        }
    }

    @Test
    public void testSimilarNamesAndCaseFindByNameContainingIgnoreCase() {
        Planet planetOne = getRandomPlanet();
        planetOne.setName("Test01");
        planetOne = planetRepository.save(planetOne);
        Planet PlanetTwo = getRandomPlanet();
        PlanetTwo.setName("Test02");
        PlanetTwo = planetRepository.save(PlanetTwo);
        Planet planetThree = getRandomPlanet();
        planetThree.setName("Test03");
        planetThree = planetRepository.save(planetThree);

        planetRepository.save(getRandomPlanet());
        planetRepository.save(getRandomPlanet());
        planetRepository.save(getRandomPlanet());
        planetRepository.save(getRandomPlanet());
        planetRepository.save(getRandomPlanet());

        assertThat(planetRepository.count()).isEqualTo(8l);

        Page<Planet> returned = planetRepository.findByNameContainingIgnoreCase("Test01", null);
        assertThat(returned).size().isEqualTo(1);
        assertThat(returned.getContent()).containsOnly(planetOne);

        returned = planetRepository.findByNameContainingIgnoreCase("test02", null);
        assertThat(returned).size().isEqualTo(1);
        assertThat(returned).containsOnly(PlanetTwo);

        returned = planetRepository.findByNameContainingIgnoreCase("tesT03", null);
        assertThat(returned).size().isEqualTo(1);
        assertThat(returned).containsOnly(planetThree);

        returned = planetRepository.findByNameContainingIgnoreCase("Test", null);
        doAssertions(returned.getContent(), planetOne, PlanetTwo, planetThree);

        returned = planetRepository.findByNameContainingIgnoreCase("test", null);
        doAssertions(returned.getContent(), planetOne, PlanetTwo, planetThree);

        returned = planetRepository.findByNameContainingIgnoreCase("tESt", null);
        doAssertions(returned.getContent(), planetOne, PlanetTwo, planetThree);

        returned = planetRepository.findByNameContainingIgnoreCase("tes", null);
        doAssertions(returned.getContent(), planetOne, PlanetTwo, planetThree);

        returned = planetRepository.findByNameContainingIgnoreCase("TestTestTestTestTest", null);
        assertThat(returned.getContent()).isEmpty();
    }

    @Test
    public void teste_findOneByNameIgnoreCase(){
        assertThat(planetRepository.findOneByNameIgnoreCase("test")).isNull();

        Planet planetOne = getRandomPlanet();
        planetOne.setName("Test 01");
        planetRepository.save(planetOne);

        Planet planetTwo = getRandomPlanet();
        planetTwo.setName("Test 02");
        planetRepository.save(planetTwo);

        Planet planetThree = getRandomPlanet();
        planetThree.setName("Test 03");
        planetRepository.save(planetThree);

        assertThat(planetRepository.count()).isEqualTo(3l);

        String[] tests = {
                "Test 01", "test 01", "teSt 02", "tesT 03", "TEST 01", "Test 0", "test 0",
                "testi 0", "TEST", "TEST 0", "TEST  01", "tes", " ", "   Test 01", "Test  01"
            };

        for(int i = 0; i < tests.length; i++) {
            assertThat(planetRepository.findOneByNameIgnoreCase(tests[i])).isEqualTo(planetOne);
            assertThat(planetRepository.findOneByNameIgnoreCase(tests[i])).isEqualTo(planetTwo);
            assertThat(planetRepository.findOneByNameIgnoreCase(tests[i])).isEqualTo(planetThree);
            assertThat(planetRepository.findOneByNameIgnoreCase(tests[i])).isNull();
        }
    }

    @Test
    public void testFindOne(){
        assertThat(planetRepository.findOne("test")).isNull();

        Planet planetOne = getRandomPlanet();
        planetOne.setId("Test01");
        planetRepository.save(planetOne);

        Planet planetTwo = getRandomPlanet();
        planetTwo.setId("Test02");
        planetRepository.save(planetTwo);

        Planet planetThree = getRandomPlanet();
        planetThree.setId("Test03");
        planetRepository.save(planetThree);

        assertThat(planetRepository.count()).isEqualTo(3l);

        String[] tests = {
                "Test01", "Test02", "Test03", "test01", "teSt02", "tesT03", "TEST01",
                "Test0", "test0", "testi0", "TEST", "TEST0", "TEST 01", "tes", " ",
                "   Test_01", "Test_01"
            };

        for(int i = 0; i < tests.length; i++) {
            assertThat(planetRepository.findOne(tests[i])).isEqualTo(planetOne);
            assertThat(planetRepository.findOne(tests[i])).isEqualTo(planetTwo);
            assertThat(planetRepository.findOne(tests[i])).isEqualTo(planetThree);
            assertThat(planetRepository.findOne(tests[i])).isNull();;
        }
    }

    @Test
    public void testeDeleteById() {
        Planet planetOne = getRandomPlanet();
        planetOne.setName("Test 1");
        planetOne = planetRepository.save(planetOne);

        Planet planetTwo = getRandomPlanet();
        planetTwo.setName("Test 2");
        planetTwo = planetRepository.save(planetTwo);

        planetRepository.save(getRandomPlanet());

        assertThat(planetRepository.findAll()).hasSize(3);

        Optional<Planet> returned = planetRepository.findById(planetOne.getId());
        assertThat(returned.isPresent()).isTrue();
        assertThat(returned.get()).isEqualTo(planetOne);
        planetRepository.deleteById(planetOne.getId());

        returned = planetRepository.findById(planetOne.getId());
        assertThat(returned.isPresent()).isFalse();
        assertThat(planetRepository.findAll()).hasSize(2);
    }

    private void doAssertions(List<Planet> returned, Planet p1, Planet p2, Planet p3) {
        assertThat(returned).size().isGreaterThanOrEqualTo(3);
        assertThat(returned).contains(p1, p2, p3);
    }
}
