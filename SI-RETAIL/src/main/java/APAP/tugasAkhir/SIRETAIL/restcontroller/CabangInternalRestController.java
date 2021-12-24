package APAP.tugasAkhir.SIRETAIL.restcontroller;


import APAP.tugasAkhir.SIRETAIL.model.CabangModel;
import APAP.tugasAkhir.SIRETAIL.model.UserModel;
import APAP.tugasAkhir.SIRETAIL.rest.BaseResponse;
import APAP.tugasAkhir.SIRETAIL.rest.CabangDTO;
import APAP.tugasAkhir.SIRETAIL.restservice.CabangRestService;
import APAP.tugasAkhir.SIRETAIL.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.text.ParseException;

@RestController
@RequestMapping("/internal/api/cabang")
public class CabangInternalRestController {

    @Qualifier("cabangRestServiceImpl")
    @Autowired
    private CabangRestService cabangRestService;

    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;

    @PostMapping("/accept/{noCabang}")
    public String acceptPermintaan(
            Authentication authentication,
            Model model,
            @PathVariable Long noCabang
    ){
        String username = ((UserDetails)authentication.getPrincipal()).getUsername();
        UserModel user = userService.getUserByUsername(username);
        CabangModel cabang = cabangRestService.acceptCabang(noCabang, user);
        return cabang.getNamaCabang();
    }

    @PostMapping("/reject/{noCabang}")
    public String rejectPermintaan(
            Authentication authentication,
            Model model,
            @PathVariable Long noCabang
    ){
        String username = ((UserDetails)authentication.getPrincipal()).getUsername();
        UserModel user = userService.getUserByUsername(username);
        CabangModel cabang = cabangRestService.rejectCabang(noCabang, user);
        return cabang.getNamaCabang();
    }

    // pekerjaan harakan tutup
}
