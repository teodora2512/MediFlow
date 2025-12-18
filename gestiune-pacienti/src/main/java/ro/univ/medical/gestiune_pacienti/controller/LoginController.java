package ro.univ.medical.gestiune_pacienti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping({"/editor/home", "/user/home"})
    public String homeRedirect() {
        return "redirect:/pacienti";
    }
}
