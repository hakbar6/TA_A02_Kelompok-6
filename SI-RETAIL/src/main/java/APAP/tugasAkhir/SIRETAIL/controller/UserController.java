package APAP.tugasAkhir.SIRETAIL.controller;

import APAP.tugasAkhir.SIRETAIL.model.RoleModel;
import APAP.tugasAkhir.SIRETAIL.model.UserModel;
import APAP.tugasAkhir.SIRETAIL.service.RoleService;
import APAP.tugasAkhir.SIRETAIL.service.UserService;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Qualifier("roleServiceImpl")
    @Autowired
    private RoleService roleService;

    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;

    @GetMapping(value = "/create")
    public String addUserFormPage(
            Model model
    ){
        UserModel user = new UserModel();
        List<RoleModel>  listRole = roleService.listRole();
        model.addAttribute("user",user);
        model.addAttribute("role", listRole);
        return "add-user";
    }

    @PostMapping(value = "/create")
    public String addUserSubmit(
            @ModelAttribute UserModel user,
            @RequestParam("password") String password,
            Model model
    ){
        if(userService.confirmPasswordWhenCreate(password).equals("none")){
            userService.addUser(user);
            return "redirect:/";
        }
        else if(userService.confirmPasswordWhenCreate(password).equals("create-user-berhasil")){
            userService.addUser(user);
            return "redirect:/";
        }
        else{
            String message =  userService.confirmPasswordWhenCreate(password);
            model.addAttribute("message", message);
            return "password-salah";
        }
    }

    @GetMapping(value = "/viewalluser")
    public String viewUser(Model model){
        List<UserModel> userList = userService.daftarUser();
        model.addAttribute("userList", userList);
        return "viewall-user";
    }

    @GetMapping(value = "/update/{username}")
    public String updateUser(
                            @PathVariable String username,
                            Model model,
                            Authentication auth,
                            RedirectAttributes red
        ){
        List<RoleModel>  listRole = roleService.listRole();
        UserModel user = userService.getUserByUsername(username);
        String editorRole = userService.getUserByUsername(auth.getName()).getRole().getRole();
        if (editorRole.equals("Manager Cabang") && (user.getRole().getRole().equals("Kepala Retail") || user.getRole().getRole().equals("Staff Cabang"))) {
            red.addFlashAttribute("pesanError","Role anda tidak dapat mengakses fitur ini");
            return "redirect:/user/viewalluser";
        }
        else if (editorRole.equals("Staff Cabang")) {
            red.addFlashAttribute("pesanError","Role anda tidak dapat mengakses fitur ini");
            return "redirect:/user/viewalluser";
        }
        model.addAttribute("user", user);
        model.addAttribute("role", listRole);
        return "form-update-user";
    }

    @PostMapping(value = "/update")
    public String updateUserSubmit(
        @RequestParam("oldPassword") String oldPassword,
        @RequestParam("newPassword") String newPassword,
        @RequestParam("confirmedNewPassword") String confirmedNewPassword,
        @PathVariable String username,
        Model model
    ){
        UserModel user = userService.getUserByUsername(username);
        if(userService.confirmPasswordWhenUpdate(username, oldPassword, newPassword, confirmedNewPassword).equals("none")) {
            UserModel currentLoggedIn = userService.getUserNameLogin();
            userService.updateUser(user, newPassword);
            model.addAttribute("id", user.getId_user());
            return "update-user";
        }

        else if(userService.confirmPasswordWhenUpdate(username, oldPassword, newPassword, confirmedNewPassword).equals("update-password-berhasil")) {
            UserModel currentLoggedIn = userService.getUserNameLogin();
            userService.updateUser(user,newPassword);
            model.addAttribute("id", user.getId_user());
            return "update-user";
        }

        else{
            String message =  userService.confirmPasswordWhenUpdate(username, oldPassword, newPassword, confirmedNewPassword);
            model.addAttribute("message", message);
            return "password-salah";
        }
    }
}

