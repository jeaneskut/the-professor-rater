package com.theprofessorrater.theprofessorrater;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
public class StaffRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required.")
    @Size(max = 100, message = "Name cannot exceed 100 characters.")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Enter a valid email address.")
    @Size(max = 254, message = "Email cannot exceed 254 characters.")
    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @NotNull(message = "Please select a role.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType roleType;

    @NotNull(message = "Clarity score is required.")
    @Min(value = 1, message = "Clarity must be at least 1.")
    @Max(value = 10, message = "Clarity cannot exceed 10.")
    @Column(nullable = false)
    private Integer clarity;

    @NotNull(message = "Niceness score is required.")
    @Min(value = 1, message = "Niceness must be at least 1.")
    @Max(value = 10, message = "Niceness cannot exceed 10.")
    @Column(nullable = false)
    private Integer niceness;

    @NotNull(message = "Knowledge score is required.")
    @Min(value = 1, message = "Knowledge must be at least 1.")
    @Max(value = 10, message = "Knowledge cannot exceed 10.")
    @Column(nullable = false)
    private Integer knowledgeableScore;

    @Size(max = 1000, message = "Comment cannot exceed 1000 characters.")
    @Column(length = 1000)
    private String comment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public StaffRating() {
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Integer getClarity() {
        return clarity;
    }

    public void setClarity(Integer clarity) {
        this.clarity = clarity;
    }

    public Integer getNiceness() {
        return niceness;
    }

    public void setNiceness(Integer niceness) {
        this.niceness = niceness;
    }

    public Integer getKnowledgeableScore() {
        return knowledgeableScore;
    }

    public void setKnowledgeableScore(Integer knowledgeableScore) {
        this.knowledgeableScore = knowledgeableScore;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public double getOverallScore() {
        if (clarity == null || niceness == null
                || knowledgeableScore == null) {
            return 0.0;
        }

        return (clarity + niceness + knowledgeableScore) / 3.0;
    }
}
