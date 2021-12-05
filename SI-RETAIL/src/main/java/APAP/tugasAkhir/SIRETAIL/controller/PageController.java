package APAP.tugasAkhir.SIRETAIL.controller;

import APAP.tugasAkhir.SIRETAIL.model.RoleModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("/")
    public String home(
            Model model,
            Authentication authentication
    ){
        String role = "";
        // if( authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("Kepala Retail")) ){
        //     role = "Kepala Retail";
        // }else if( authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("Manager Cabang")) ){
        //     role = "Manager Cabang";
        // }else if( authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("Staff Cabang")) ) {
        //     role = "Staff Cabang";
        // }
        model.addAttribute("role",role);
        return "home";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }
}
