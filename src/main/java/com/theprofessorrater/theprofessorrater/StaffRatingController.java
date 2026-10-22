package com.theprofessorrater.theprofessorrater;

import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

// the controller
// contains proper mappings for postgreSQL
@Controller
public class StaffRatingController {

    private final StaffRatingRepository ratingRepository;
    private final StaffProfileFactory profileFactory;

    public StaffRatingController(
            StaffRatingRepository ratingRepository,
            StaffProfileFactory profileFactory) {
        this.ratingRepository = ratingRepository;
        this.profileFactory = profileFactory;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<StaffRating> ratings = ratingRepository.findAll();
        model.addAttribute("ratings", ratings);
        return "index";
    }

    @GetMapping("/ratings/create")
    public String showCreateForm(Model model) {
        model.addAttribute("rating", new StaffRating());
        return "create";
    }

    @PostMapping("/ratings")
    public String createRating(
            @Valid @ModelAttribute("rating") StaffRating rating,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (rating.getEmail() != null
                && ratingRepository.existsByEmailIgnoreCase(rating.getEmail())) {
            result.rejectValue(
                    "email",
                    "duplicate",
                    "A rating with this email already exists.");
        }

        if (result.hasErrors()) {
            return "create";
        }

        try {
            ratingRepository.save(rating);
        } catch (DataIntegrityViolationException ex) {
            result.rejectValue(
                    "email",
                    "duplicate",
                    "A rating with this email already exists.");
            return "create";
        }

        redirectAttributes.addFlashAttribute(
                "successMessage", "Rating added successfully.");

        return "redirect:/";
    }

    @GetMapping("/ratings/{id}")
    public String detail(@PathVariable Long id, Model model) {
        StaffRating rating = findRating(id);

        model.addAttribute("rating", rating);
        model.addAttribute(
                "profile", profileFactory.createProfile(rating));

        return "detail";
    }

    @GetMapping("/ratings/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("rating", findRating(id));
        model.addAttribute("roles", RoleType.values());
        return "edit";
    }

    @PostMapping("/ratings/{id}")
    public String updateRating(
            @PathVariable Long id,
            @Valid @ModelAttribute("rating") StaffRating submitted,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        StaffRating existing = findRating(id);

        if (submitted.getEmail() != null
                && ratingRepository.existsByEmailIgnoreCaseAndIdNot(
                        submitted.getEmail(), id)) {
            result.rejectValue(
                    "email",
                    "duplicate",
                    "A rating with this email already exists.");
        }

        if (result.hasErrors()) {
            submitted.setId(id);
            return "edit";
        }

        submitted.setId(id);
        submitted.setCreatedAt(existing.getCreatedAt());

        try {
            ratingRepository.save(submitted);
        } catch (DataIntegrityViolationException ex) {
            result.rejectValue(
                    "email",
                    "duplicate",
                    "A rating with this email already exists.");
            return "edit";
        }

        redirectAttributes.addFlashAttribute(
                "successMessage", "Rating updated successfully.");

        return "redirect:/ratings/" + id;
    }

    @PostMapping("/ratings/{id}/delete")
    public String deleteRating(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        StaffRating rating = findRating(id);
        ratingRepository.delete(rating);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Rating deleted successfully.");

        return "redirect:/";
    }

    private StaffRating findRating(Long id) {
        return ratingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Rating not found"));
    }
}