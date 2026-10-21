package com.theprofessorrater.theprofessorrater;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRatingRepository extends JpaRepository<StaffRating, Long> {
        boolean existsByEmailIgnoreCase(String email);

        boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}