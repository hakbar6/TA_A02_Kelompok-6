package APAP.tugasAkhir.SIRETAIL.controller;

import APAP.tugasAkhir.SIRETAIL.rest.ItemDTO;
import APAP.tugasAkhir.SIRETAIL.service.ItemCabangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ItemController {

    @Qualifier("itemCabangServiceImpl")
    @Autowired
    private ItemCabangService itemCabangService;

    // pekerjaan evan
    @GetMapping("/test")
    public String testAPIGETITEM(
        Model model
    ){
        List<ItemDTO> result = itemCabangService.getAllItem();
        return "home";
    }
    // pekerjaan evan tutup
}
