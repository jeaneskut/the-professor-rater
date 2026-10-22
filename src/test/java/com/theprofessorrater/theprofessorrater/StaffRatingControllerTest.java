package com.theprofessorrater.theprofessorrater;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// these tests are for the controller
@WebMvcTest(StaffRatingController.class)
class StaffRatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StaffRatingRepository ratingRepository;

    @MockitoBean
    private StaffProfileFactory profileFactory;

    private StaffRating sampleRating() {
        StaffRating rating = new StaffRating();
        rating.setId(1L);
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
    void indexReturnsRatings() throws Exception {
        when(ratingRepository.findAll()).thenReturn(List.of(sampleRating()));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("ratings"));
    }

    @Test
    void detailReturnsRatingAndProfile() throws Exception {
        StaffRating rating = sampleRating();

        when(ratingRepository.findById(1L)).thenReturn(Optional.of(rating));

        when(profileFactory.createProfile(rating)).thenReturn(() -> "Teaching Assistant");

        mockMvc.perform(get("/ratings/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("detail"))
                .andExpect(model().attributeExists("rating"))
                .andExpect(model().attributeExists("profile"));
    }

    @Test
    void createValidRatingRedirectsToIndex() throws Exception {
        when(ratingRepository.existsByEmailIgnoreCase(anyString())).thenReturn(false);

        when(ratingRepository.save(any(StaffRating.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/ratings")
                .param("name", "Jane Doe")
                .param("email", "jane@example.com")
                .param("roleType", "TA")
                .param("clarity", "8")
                .param("niceness", "9")
                .param("knowledgeableScore", "10")
                .param("comment", "Helpful instructor"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(ratingRepository).save(any(StaffRating.class));
    }

    @Test
    void createRejectsInvalidEmail() throws Exception {
        mockMvc.perform(post("/ratings")
                .param("name", "Jane Doe")
                .param("email", "not-an-email")
                .param("roleType", "TA")
                .param("clarity", "8")
                .param("niceness", "9")
                .param("knowledgeableScore", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("create"))
                .andExpect(model().attributeHasFieldErrors(
                        "rating", "email"));

        verify(ratingRepository, never()).save(any(StaffRating.class));
    }

    @Test
    void createRejectsMissingRequiredFields() throws Exception {
        mockMvc.perform(post("/ratings"))
                .andExpect(status().isOk())
                .andExpect(view().name("create"))
                .andExpect(model().attributeHasFieldErrors(
                        "rating",
                        "name",
                        "email",
                        "roleType",
                        "clarity",
                        "niceness",
                        "knowledgeableScore"));
    }

    @Test
    void createRejectsOutOfRangeScore() throws Exception {
        mockMvc.perform(post("/ratings")
                .param("name", "Jane Doe")
                .param("email", "jane@example.com")
                .param("roleType", "TA")
                .param("clarity", "11")
                .param("niceness", "9")
                .param("knowledgeableScore", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("create"))
                .andExpect(model().attributeHasFieldErrors(
                        "rating", "clarity"));

        verify(ratingRepository, never()).save(any(StaffRating.class));
    }

    @Test
    void createRejectsDuplicateEmail() throws Exception {
        when(ratingRepository.existsByEmailIgnoreCase("jane@example.com")).thenReturn(true);

        mockMvc.perform(post("/ratings")
                .param("name", "Jane Doe")
                .param("email", "jane@example.com")
                .param("roleType", "TA")
                .param("clarity", "8")
                .param("niceness", "9")
                .param("knowledgeableScore", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("create"))
                .andExpect(model().attributeHasFieldErrors(
                        "rating", "email"));

        verify(ratingRepository, never()).save(any(StaffRating.class));
    }


    @Test
    void updateValidRatingRedirectsToDetails() throws Exception {
        StaffRating existing = sampleRating();

        when(ratingRepository.findById(1L)).thenReturn(Optional.of(existing));

        when(ratingRepository.existsByEmailIgnoreCaseAndIdNot("jane@example.com", 1L)).thenReturn(false);

        when(ratingRepository.save(any(StaffRating.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/ratings/1")
                .param("name", "Jane Doe Updated")
                .param("email", "jane@example.com")
                .param("roleType", "TA")
                .param("clarity", "9")
                .param("niceness", "9")
                .param("knowledgeableScore", "10")
                .param("comment", "Updated comment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ratings/1"));

        verify(ratingRepository).save(any(StaffRating.class));
    }

    @Test
    void deleteRatingRedirectsToIndex() throws Exception {
        StaffRating rating = sampleRating();

        when(ratingRepository.findById(1L)).thenReturn(Optional.of(rating));

        mockMvc.perform(post("/ratings/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(ratingRepository).delete(rating);
    }
}