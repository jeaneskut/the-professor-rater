package com.theprofessorrater.theprofessorrater;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/ratings")
public class StaffRatingController {

    private final StaffRatingRepository ratingRepository;

    public StaffRatingController(StaffRatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @GetMapping("/create")
    public String showAddForm(Model model) {
        model.addAttribute("rating", new StaffRating());
        model.addAttribute("roles", RoleType.values());
        return "create";
    }

     @PostMapping
    public String createRating(
            @Valid StaffRating rating,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", RoleType.values());
            return "create";
        }

        try {
            ratingRepository.save(rating);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            bindingResult.rejectValue(
                    "email", "duplicate",
                    "This email may already have a rating."
            );
            model.addAttribute("roles", RoleType.values());
            return "create";
        }

        redirectAttributes.addFlashAttribute(
                "successMessage", "Rating added successfully!"
        );

        return "redirect:/";
    }
}