package APAP.tugasAkhir.SIRETAIL.controller;

import APAP.tugasAkhir.SIRETAIL.model.CabangModel;
import APAP.tugasAkhir.SIRETAIL.model.ItemCabangModel;
import APAP.tugasAkhir.SIRETAIL.model.UserModel;
import APAP.tugasAkhir.SIRETAIL.repository.UserDB;
import APAP.tugasAkhir.SIRETAIL.rest.CouponDTO;
import APAP.tugasAkhir.SIRETAIL.service.CabangService;
import APAP.tugasAkhir.SIRETAIL.service.ItemCabangService;
import APAP.tugasAkhir.SIRETAIL.service.KuponService;
import APAP.tugasAkhir.SIRETAIL.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    private KuponService kuponService;

    // Method untuk halaman form buat cabang baru
    @GetMapping("/cabang/create")
    public String addCabangForm(
            Model model,
            Authentication authentication
    ) {
        String username = ((UserDetails)authentication.getPrincipal()).getUsername();
        UserModel user = userService.getUserByUsername(username);
        if (user.getRole().getRole().equals("Kepala Retail") ||
                user.getRole().getRole().equals("Manager Cabang") ){
            CabangModel cabang = new CabangModel();
            model.addAttribute("cabang", cabang);
            return "form-add-cabang";
        }else{
            return "error/403";
        }
    }

    // Method untuk submit form buat cabang baru
    // pekerjaan harakan
    @PostMapping("/cabang/create")
    public String addCabangSubmit(
            @ModelAttribute CabangModel cabang,
            Model model,
            Authentication authentication
    ) {
        String username = ((UserDetails)authentication.getPrincipal()).getUsername();
        UserModel user = userService.getUserByUsername(username);
        if (user.getRole().getRole().equals("Kepala Retail") ||
                user.getRole().getRole().equals("Manager Cabang") ){
            cabangService.addCabang(cabang,user);
            model.addAttribute("noCabang", cabang.getNoCabang());
            return "add-cabang";
        }else{
            return "error/403";
        }
    }

    // Method untuk halaman form ubah informasi cabang
    @GetMapping("/cabang/update/{noCabang}")
    public String updateCabangForm(
            @PathVariable Long noCabang,
            Model model,
            Authentication authentication
    ) {
        String username = ((UserDetails)authentication.getPrincipal()).getUsername();
        UserModel user = userService.getUserByUsername(username);
        if (user.getRole().getRole().equals("Kepala Retail") ||
                user.getRole().getRole().equals("Manager Cabang")){
            CabangModel cabang = cabangService.getCabang(noCabang);
            model.addAttribute("cabang", cabang);
            return "form-update-cabang";
        }else{
            return "error/403";
        }
    }


    // Method untuk menampilkan halaman daftar permintaan
    @GetMapping("/cabang/daftarPermintaan")
    public String viewAllPermintaan(
            Model model,
            Authentication authentication
    ){
        String username = ((UserDetails)authentication.getPrincipal()).getUsername();
        UserModel user = userService.getUserByUsername(username);
        if (user.getRole().getRole().equals("Kepala Retail")){
            List<CabangModel> permintaan = cabangService.getListPermintaan();
            model.addAttribute("permintaan",permintaan);
            return "viewall-permintaan";
        }else{
            return "error/403";
        }
    }
    // pekerjaan harakan tutup

    // Method untuk submisi form ubah informasi cabang
    @PostMapping("/cabang/update")
    public String updateCabangSubmit(
            @ModelAttribute CabangModel cabang,
            Model model,
            Authentication authentication
    ) {
        String username = ((UserDetails)authentication.getPrincipal()).getUsername();
        UserModel user = userService.getUserByUsername(username);
        if (user.getRole().getRole().equals("Kepala Retail") ||
                user.getRole().getRole().equals("Manager Cabang")){
            cabangService.updateCabang(cabang);
            model.addAttribute("noCabang", cabang.getNoCabang());
            return "update-cabang";
        }else{
            return "error/403";
        }
    }

    // Method untuk menghapus cabang
    @RequestMapping(value = "/cabang/delete/{noCabang}", method = RequestMethod.GET)
    public String deleteCabang(
            @PathVariable Long noCabang,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        CabangModel cabang = cabangService.getCabang(noCabang);

        String message;

        model.addAttribute("noCabang", cabang.getNoCabang());

        if (cabang.getListItem().isEmpty()) {
            cabangService.deleteCabang(noCabang);
            return "delete-cabang";
        } else {
            message = "Cabang masih memiliki item!";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/cabang";
        }
    }

    // Method untuk merujuk ke halaman manage cabang
    @GetMapping("/cabang")
    public String manageCabang(
            Model model
    ){
        return "cabang";
    }

    // Method untuk menampilkan halaman daftar cabang
    @GetMapping("/cabang/daftarCabang")
    public String viewAllCabang(Model model, Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        UserModel user = userService.getUserByUsername(username);

        if (user.getRole().getRole().equals("Manager Cabang")) {
            List<CabangModel> listCabang = cabangService.getListCabangByUser(user);
            model.addAttribute("listCabang", listCabang);
        } else {
            List<CabangModel> listCabang = cabangService.getListCabang();
            model.addAttribute("listCabang", listCabang);
        }

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

        List<ItemCabangModel> listItem = cabang.getListItem();

        model.addAttribute("cabang", cabang);
        model.addAttribute("listItem", listItem);

        return "view-cabang";
    }

    @GetMapping(value = "/couponList/{noCabang}/{id}")
    private String getCoupon(
        @PathVariable Long id,
        @PathVariable Long noCabang,
        Model model
    ){
        List<CouponDTO> couponList = kuponService.getAllKupon();
        model.addAttribute("couponList", couponList);
        model.addAttribute("noCabang", noCabang);
        model.addAttribute("idItem", id);
        return "viewall-coupon";
    }

    @GetMapping(value = "/couponApplied")
    private String couponApplied(
        @RequestParam Long noCabang,
        @RequestParam Long id,
        @RequestParam int idCoupon,
        @RequestParam float discountAmount
    ){
        kuponService.kuponApply(id, idCoupon, discountAmount);
        return String.format("redirect:/cabang/view?noCabang=%s", noCabang);
    }
}
