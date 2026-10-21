package com.theprofessorrater.theprofessorrater;

public class ProfessorProfile implements StaffMemberProfile {

    @Override
    public String displayTitle() {
        return "Professor";
    }
}