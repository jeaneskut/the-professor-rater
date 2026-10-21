package com.theprofessorrater.theprofessorrater;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}