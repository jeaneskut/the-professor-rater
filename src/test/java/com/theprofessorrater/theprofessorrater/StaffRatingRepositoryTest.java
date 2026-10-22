package com.theprofessorrater.theprofessorrater;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;


//these tests are for the repository/database components
@DataJpaTest
class StaffRatingRepositoryTest {

    @Autowired
    private StaffRatingRepository repository;

    private StaffRating createRating() {
        StaffRating rating = new StaffRating();
        rating.setName("Jane Doe");
        rating.setEmail("jane@example.com");
        rating.setRoleType(RoleType.TA);
        rating.setClarity(8);
        rating.setNiceness(9);
        rating.setKnowledgeableScore(10);
        rating.setComment("Helpful instructor");
        return rating;
    }

    @Test
    void saveAndRetrieveRating() {
        StaffRating saved = repository.saveAndFlush(createRating());

        StaffRating found = repository.findById(saved.getId()).orElseThrow();

        assertEquals("Jane Doe", found.getName());
        assertEquals("jane@example.com", found.getEmail());
        assertEquals(8, found.getClarity());
        assertNotNull(found.getCreatedAt());
        assertNotNull(found.getUpdatedAt());
    }

    @Test
    void deleteRemovesRating() {
        StaffRating saved = repository.saveAndFlush(createRating());
        Long id = saved.getId();

        repository.deleteById(id);
        repository.flush();

        assertTrue(repository.findById(id).isEmpty());
    }
}