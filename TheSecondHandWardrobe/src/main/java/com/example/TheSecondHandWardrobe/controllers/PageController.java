package com.example.TheSecondHandWardrobe.controllers;

import com.example.TheSecondHandWardrobe.data.RegistrationRequest;
import com.example.TheSecondHandWardrobe.entities.Advertisement;
import com.example.TheSecondHandWardrobe.entities.AppUser;
import com.example.TheSecondHandWardrobe.entities.Image;
import com.example.TheSecondHandWardrobe.security.PasswordEncoder;
import com.example.TheSecondHandWardrobe.services.AdvertisementService;
import com.example.TheSecondHandWardrobe.services.AppUserService;
import com.example.TheSecondHandWardrobe.services.ImageService;
import com.example.TheSecondHandWardrobe.services.RegistrationService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class PageController {

    private final AppUserService appUserService;
    private final RegistrationService registrationService;
    private final AdvertisementService advertisementService;
    private final ImageService imageService;

    @GetMapping("/login")
    public String login(HttpSession session, Model model) {
        if (session.getAttribute("log") == null) {
            return "/login";
        }

        boolean log = (boolean) session.getAttribute("log");
        if (log) {
            model.addAttribute("logger", true);
            session.invalidate();
        }


        return "/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session) {
        AppUser user = appUserService.findByEmail(email);
        if (user == null) {
            model.addAttribute("notInBase", true);
            return "/login";
        }


//        if (!user.getPassword().equals(password)) {
//            model.addAttribute("error", true);
//            model.addAttribute("mail", email);
//            return "/login";
//        }

        if (!user.getEnabled()) {
            model.addAttribute("notEnabled", true);
            return "/login";
        }

        session.setAttribute("loggedInName", user.getName());
        session.setAttribute("loggedInEmail", user.getEmail());
        session.setAttribute("loggedInLastname", user.getLastName());
        session.setAttribute("logged", true);
        return "redirect:/index";
    }

    @PostMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/index")
    public String index(HttpSession session, Model model) {
        String loggedInName = (String) session.getAttribute("loggedInName");
        if (loggedInName != null) {
            model.addAttribute("loggedInName", loggedInName);
        }
        return "/index";
    }

    @GetMapping("/registration")
    public String registration() {
        return "/registration";
    }

    @PostMapping("/registration")
    public String register(@ModelAttribute RegistrationRequest request, @RequestParam("confirm") String confirm, Model model) {
        String email = request.getEmail();
        AppUser appUser = appUserService.findByEmail(email);
        if (appUser != null) {
            model.addAttribute("used", true);
            model.addAttribute("first", request.getName());
            model.addAttribute("last", request.getLastName());
            return "/registration";
        }

        if (!confirm.equals(request.getPassword())) {
            model.addAttribute("error", true);
            model.addAttribute("first", request.getName());
            model.addAttribute("last", request.getLastName());
            model.addAttribute("mail", request.getEmail());
            return "/registration";
        }

        registrationService.register(request);
        return "redirect:/login";
    }

    @GetMapping("/addAdvertisement")
    public String addAdvertisement(HttpSession session, Model model) {
        String loggedInName = (String) session.getAttribute("loggedInName");
        String loggedInLastName = (String) session.getAttribute("loggedInLastName");
        String loggedInEmail = (String) session.getAttribute("loggedInEmail");

        if (loggedInEmail == null) {
            session.setAttribute("log", true);
            return "redirect:/login";
        }

        model.addAttribute("loggedInName", loggedInName);
        return "/advertisement";
    }

    @GetMapping("/listAllAds")
    public String listAds(HttpSession session, Model model) {
        String loggedInEmail = (String) session.getAttribute("loggedInEmail");
        String loggedInName = (String) session.getAttribute("loggedInName");
        List<Advertisement> advertisements = advertisementService.findAll();
        advertisements = advertisements.stream().filter(ad -> ad.getAppUser().getEmail().equals(session.getAttribute("loggedInEmail"))).collect(Collectors.toList());
        if (advertisements.size() == 0) {
            model.addAttribute("loggedInName", loggedInName);
            model.addAttribute("error4", true);
        } else {

            model.addAttribute("loggedInEmail", loggedInEmail);
            model.addAttribute("loggedInName", loggedInName);
            model.addAttribute("advertisements", advertisements);
        }
        return "/listAllAds";
    }

    @PostMapping("/addAdvertisement")
    public String added(@RequestParam("file") MultipartFile file, @RequestParam("name") String name, @RequestParam("lastName") String lastName, @RequestParam("email") String email,
                        @RequestParam("category") String category, @RequestParam("size") String size, @RequestParam("shoeSize") String shoeSize, @RequestParam("details") String details,
                        @RequestParam("defects") String defects, HttpSession session, Model model) throws IOException {

        String loggedInEmail = (String) session.getAttribute("loggedInEmail");
        String loggedInName = (String) session.getAttribute("loggedInName");
        String loggedInLastname = (String) session.getAttribute("loggedInLastname");

        if (!(loggedInEmail.equals(email) && loggedInName.equals(name) && loggedInLastname.equals(lastName))) {
            model.addAttribute("error", true);
            return "advertisement";
        }

        Image image = imageService.addImage(file);

        if (image.getImageBytes().length == 0) {
            model.addAttribute("error3", true);
            model.addAttribute("first", name);
            model.addAttribute("last", lastName);
            model.addAttribute("mail", email);
            model.addAttribute("detali", details);
            model.addAttribute("defecti", defects);
            return "advertisement";
        }

        if (category == null) {
            model.addAttribute("error12", true);
            model.addAttribute("first", name);
            model.addAttribute("last", lastName);
            model.addAttribute("mail", email);
            model.addAttribute("detali", details);
            model.addAttribute("defecti", defects);
            return "advertisement";
        }


        String sumSize = null;
        if (!size.equals("")) {
            sumSize = size;
        } else if (!shoeSize.equals("")) {
            sumSize = shoeSize;
        }

        AppUser appUser = appUserService.findByEmail(email);
        Advertisement advertisement = new Advertisement(details, defects, category, sumSize, appUser);
        advertisement.setImage(image);
        advertisementService.addAd(advertisement);

        List<Advertisement> advertisements = advertisementService.printAds();
        session.setAttribute("advertisements", advertisements);
        return "redirect:/listAllAds";
    }

    @GetMapping("/advertisements/delete/{id}")
    public String delete(@PathVariable(name = "id") Long id, HttpSession session) {
        advertisementService.deleteAd(id);
        List<Advertisement> ads = advertisementService.findAll();
        session.setAttribute("advertisements", ads);
        return "redirect:/delete";

    }


    @GetMapping("/delete")
    public String delete() {
        return "/delete";
    }

    @PostMapping("/advertisements/edit/{id}")
    @Transactional
    public String saveAd(@PathVariable Long id, @ModelAttribute Advertisement advertisement,
                         @RequestParam("email") String email, @RequestParam("file") MultipartFile file,
                         @RequestParam("size") String size, @RequestParam("name") String name, @RequestParam("lastName") String lastName,
                         @RequestParam("details") String details, @RequestParam("defects") String defects, @RequestParam("shoeSize") String shoeSize,
                         @RequestParam("category") String category, HttpSession session, Model model) throws IOException {

        String loggedInEmail = (String) session.getAttribute("loggedInEmail");
        String loggedInName = (String) session.getAttribute("loggedInName");
        String loggedInLastname = (String) session.getAttribute("loggedInLastname");
        model.addAttribute("loggedInName", loggedInName);

        Advertisement ad = advertisementService.findAd(id);
        AppUser appUser = appUserService.findByEmail(email);
        Image image = imageService.findByAdvertisement(advertisement);
        String newImageBase64 = Base64.getEncoder().encodeToString(file.getBytes());
        image.setBase(newImageBase64);
        image.setImageBytes(file.getBytes());
        ad.setAppUser(appUser);
        ad.setDefects(advertisement.getDefects());
        ad.setModel(advertisement.getModel());

        String sumSize = null;
        if (!size.equals("")) {
            sumSize = size;
        } else if (!shoeSize.equals("")) {
            sumSize = shoeSize;
        }

        ad.setSize(sumSize);
        ad.setModel(category);
        ad.setDetails(advertisement.getDetails());
        ad.setDefects(advertisement.getDefects());
        ad.setImage(image);
        advertisementService.addAd(ad);

        List<Advertisement> ads = advertisementService.findAll();
        session.setAttribute("advertisements", ads);
        return "/saved";
    }

    @GetMapping("/advertisements/edit/{id}")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ModelAndView editPage(@PathVariable(name = "id") Long id) {
        ModelAndView editView = new ModelAndView("editAd");
        Advertisement ad = advertisementService.findAd(id);
        System.out.println(ad);
        editView.addObject("ad", ad);
        return editView;
    }
}
