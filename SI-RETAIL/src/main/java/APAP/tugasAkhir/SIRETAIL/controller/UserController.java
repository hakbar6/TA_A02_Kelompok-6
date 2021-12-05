package APAP.tugasAkhir.SIRETAIL.controller;

import APAP.tugasAkhir.SIRETAIL.model.RoleModel;
import APAP.tugasAkhir.SIRETAIL.model.UserModel;
import APAP.tugasAkhir.SIRETAIL.service.RoleService;
import APAP.tugasAkhir.SIRETAIL.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
            Model model
    ){
        userService.addUser(user);
        return "redirect:/";
    }

    @GetMapping(value = "/viewalluser")
    public String viewUser(Model model){
        List<UserModel> userList = userService.daftarUser();
        model.addAttribute("userList", userList);
        return "viewall-user";
    }
}
