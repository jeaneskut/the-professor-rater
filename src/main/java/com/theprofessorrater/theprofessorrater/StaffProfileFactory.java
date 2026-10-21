package com.theprofessorrater.theprofessorrater;

import org.springframework.stereotype.Component;

@Component
public class StaffProfileFactory {

    public StaffMemberProfile createProfile(StaffRating rating) {
        return switch (rating.getRoleType()) {
            case TA -> new TAProfile();
            case PROF -> new ProfessorProfile();
            case INSTRUCTOR -> () -> "Instructor";
            case STAFF -> () -> "Staff Member";
        };
    }
}