package APAP.tugasAkhir.SIRETAIL.controller;

import APAP.tugasAkhir.SIRETAIL.model.CabangModel;
import APAP.tugasAkhir.SIRETAIL.model.ItemCabangModel;
import APAP.tugasAkhir.SIRETAIL.model.UserModel;
import APAP.tugasAkhir.SIRETAIL.repository.UserDB;
import APAP.tugasAkhir.SIRETAIL.service.CabangService;
import APAP.tugasAkhir.SIRETAIL.service.ItemCabangService;

import APAP.tugasAkhir.SIRETAIL.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

@Controller
public class CabangController {

    @Qualifier("cabangServiceImpl")
    @Autowired
    private CabangService cabangService;

    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;

//    @Qualifier("itemCabangServiceImpl")
//    @Autowired
//    private ItemCabangService itemCabangService;



    // Method untuk halaman form buat cabang baru
    @GetMapping("/cabang/create")
    public String addCabangForm(
            Model model
    ) {
        CabangModel cabang = new CabangModel();
        model.addAttribute("cabang", cabang);
        return "form-add-cabang";
    }

    // Method untuk submisi form buat cabang baru
    @PostMapping("/cabang/create")
    public String addCabangSubmit(
            @ModelAttribute CabangModel cabang,
            Model model,
            Authentication authentication
    ) {
        String username = ((UserDetails)authentication.getPrincipal()).getUsername();
        UserModel user = userService.getUserByUsername(username);
        cabangService.addCabang(cabang,user);
        model.addAttribute("noCabang", cabang.getNoCabang());
        return "add-cabang";
    }

    // Method untuk halaman form ubah informasi cabang
    @GetMapping("/cabang/update/{noCabang}")
    public String updateCabangForm(
            @PathVariable Long noCabang,
            Model model
    ) {
        CabangModel cabang = cabangService.getCabang(noCabang);
        model.addAttribute("cabang", cabang);
        return "form-update-cabang";
    }

    // Method untuk submisi form ubah informasi cabang
    @PostMapping("/cabang/update")
    public String updateCabangSubmit(
            @ModelAttribute CabangModel cabang,
            Model model
    ) {
        cabangService.updateCabang(cabang);
        model.addAttribute("noCabang", cabang.getNoCabang());
        return "update-cabang";
    }

    // Method untuk menghapus cabang
    @RequestMapping(value = "/cabang/delete/{noCabang}", method = RequestMethod.GET)
    public String deleteCabang(
            @PathVariable Long noCabang,
            Model model
    ) {
        CabangModel cabang = cabangService.getCabang(noCabang);

        model.addAttribute("noCabang", cabang.getNoCabang());

        if (cabang == null) {
            return "no-cabang";
        }

        cabangService.deleteCabang(noCabang);
        return "delete-cabang";
    }

    // Method untuk menampilkan daftar cabang
    @GetMapping("/cabang")
    public String viewAllCabang(Model model) {
        List<CabangModel> listCabang = cabangService.getListCabang();
        model.addAttribute("listCabang", listCabang);

        return "viewall-cabang";
    }

    // Method untuk menampilkan detail cabang
    @GetMapping("/cabang/view")
    public String viewDetailCabang(
            @RequestParam(value = "noCabang") Long noCabang,
            Model model
    ) {
        CabangModel cabang = cabangService.getCabang(noCabang);

        if (cabang == null) {
            return "no-cabang";
        }

        model.addAttribute("cabang", cabang);

        return "view-cabang";
    }
}
