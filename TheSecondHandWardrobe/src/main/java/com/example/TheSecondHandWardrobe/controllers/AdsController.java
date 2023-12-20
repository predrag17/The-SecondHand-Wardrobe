package com.example.TheSecondHandWardrobe.controllers;

import com.example.TheSecondHandWardrobe.entities.Advertisement;
import com.example.TheSecondHandWardrobe.services.AdvertisementService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class AdsController {

    private final AdvertisementService advertisementService;


    @GetMapping("/dresses")
    public String dresses(Model model, HttpSession session) {
        List<Advertisement> ads = advertisementService.printByModel("Dresses");
        getTheModel(model, session, ads);
        return "/dresses";
    }

    @GetMapping("/shirts")
    public String shirts(Model model, HttpSession session) {
        List<Advertisement> ads = advertisementService.printByModel("Shirts");
        getTheModel(model, session, ads);
        return "/shirts";
    }

    @GetMapping("/shoes")
    public String shoes(Model model, HttpSession session) {
        List<Advertisement> ads = advertisementService.printByModel("Shoes");
        getTheModel(model, session, ads);
        return "/shoes";
    }

    @GetMapping("/jackets")
    public String jackets(Model model, HttpSession session) {
        List<Advertisement> ads = advertisementService.printByModel("Jackets&Coats");
        getTheModel(model, session, ads);
        return "/jackets";
    }

    @GetMapping("/jeans")
    public String jeans(Model model, HttpSession session) {
        List<Advertisement> ads = advertisementService.printByModel("Jeans");
        getTheModel(model, session, ads);
        return "/jeans";
    }

    @GetMapping("/bags")
    public String bags(Model model, HttpSession session) {
        List<Advertisement> ads = advertisementService.printByModel("Bags");
        getTheModel(model, session, ads);
        return "/bags";
    }

    @GetMapping("/accessories")
    public String accessories(Model model, HttpSession session) {
        List<Advertisement> ads = advertisementService.printByModel("Accessories");
        getTheModel(model, session, ads);
        return "/accessories";
    }

    @GetMapping("/skirts")
    public String skirts(Model model, HttpSession session) {
        List<Advertisement> ads = advertisementService.printByModel("Skirts");
        getTheModel(model, session, ads);
        return "/skirts";
    }

    private void getTheModel(Model model, HttpSession session, List<Advertisement> ads) {
        String loggedInName = getTheName(session);
        model.addAttribute("loggedInName", loggedInName);
        if (ads.size() == 0) {
            model.addAttribute("error4", true);
        } else {
            String loggedInEmail = (String) session.getAttribute("loggedInEmail");
            model.addAttribute("loggedInEmail", loggedInEmail);
            model.addAttribute("advertisements", ads);
        }
    }

    private String getTheName(HttpSession session) {
        return (String) session.getAttribute("loggedInName");
    }

}
