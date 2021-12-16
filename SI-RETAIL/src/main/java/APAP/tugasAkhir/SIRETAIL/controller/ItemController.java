package APAP.tugasAkhir.SIRETAIL.controller;

import APAP.tugasAkhir.SIRETAIL.model.ItemCabangModel;
import APAP.tugasAkhir.SIRETAIL.repository.CabangDb;
import APAP.tugasAkhir.SIRETAIL.rest.ItemDTO;
import APAP.tugasAkhir.SIRETAIL.service.CabangService;
import APAP.tugasAkhir.SIRETAIL.service.ItemCabangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.ProcessBuilder.Redirect;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ItemController {

    @Qualifier("itemCabangServiceImpl")
    @Autowired
    private ItemCabangService itemCabangService;

    @Autowired 
    CabangService cabangService;

    // pekerjaan evan
    @GetMapping("/test")
    public String testAPIGETITEM(
        Model model
    ){
        List<ItemDTO> result = itemCabangService.getAllItem();
        return "home";
    }
    // pekerjaan evan tutup

    @GetMapping("/item/additem/{noCabang}")
    public String addItem(
        @PathVariable Long noCabang,
        Model model
    ){
        List<ItemDTO> result = itemCabangService.getAllItem();
        System.out.println("additem");
        for (ItemDTO itemDTO : result) {
            System.out.println(itemDTO.nama);
        }
        model.addAttribute("items", result);
        model.addAttribute("noCabang", noCabang);
        return "form-add-item";
    }

    @PostMapping(value="/item/additem")
    public String postMethodName(
        @RequestParam("noCabang") Long noCabang,
        @RequestParam("itemuuid") String uuid,
        @RequestParam("itemstok") int stok,
        RedirectAttributes red,
        Model model
    ) {
        ItemDTO iteminSiItem = itemCabangService.getItem(uuid);
        if (stok > iteminSiItem.stok) {
            red.addFlashAttribute("pesanError", "Stok " + iteminSiItem.nama + " tidak cukup, stok maksimal = " + iteminSiItem.stok);
            return "redirect:" +"/cabang/view?noCabang="+noCabang;
        }
        else{
            ItemCabangModel itemCabangModel = new ItemCabangModel();
            itemCabangModel.setCabang(cabangService.getCabang(noCabang));
            itemCabangModel.setNamaItem(iteminSiItem.nama);
            itemCabangModel.setHargaItem(iteminSiItem.harga);
            itemCabangModel.setKategori(iteminSiItem.kategori);
            itemCabangModel.setUuid(uuid);
            itemCabangModel.setStokItem(stok);
            System.out.println("tambah");
            itemCabangService.addItem(itemCabangModel);
            itemCabangService.updateSiItem(uuid, iteminSiItem.stok-stok);
        }
        System.out.println(stok);
        System.out.println(noCabang+2);
        return "redirect:" +"/cabang/view?noCabang="+noCabang;
    }
    
}
