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
import java.util.HashMap;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
public class ItemController {
    HashMap<String, Integer> enumKategori;

    ItemController(){
        enumKategori = new HashMap<>();
        enumKategori.put("BUKU", 1);
        enumKategori.put("DAPUR", 2);
        enumKategori.put("MAKANAN & MINUMAN", 3);
        enumKategori.put("ELEKTRONIK", 4);
        enumKategori.put("FASHION", 5);
        enumKategori.put("KECANTIKAN & PERAWATAN DIRI", 6);
        enumKategori.put("FILM & MUSIK", 7);
        enumKategori.put("GAMING", 8);
        enumKategori.put("GADGET", 9);
        enumKategori.put("KESEHATAN", 10);
        enumKategori.put("RUMAH TANGGA", 11);
        enumKategori.put("FURNITURE", 12);
        enumKategori.put("ALAT & PERANGKAT KERAS", 13);
        enumKategori.put("WEDDING", 14);
    }

    @Qualifier("itemCabangServiceImpl")
    @Autowired
    private ItemCabangService itemCabangService;

    @Autowired 
    CabangService cabangService;

    @GetMapping("/item/additem/{noCabang}")
    public String addItem(
        @PathVariable Long noCabang,
        Model model
    ){
        List<ItemDTO> result = itemCabangService.getAllItem();
        // System.out.println("additem");
        // for (ItemDTO itemDTO : result) {
        //     System.out.println(itemDTO.nama);
        // }
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
            return "redirect:/cabang/view?noCabang="+noCabang;
        }
        else{
            ItemCabangModel itemCabangModel = new ItemCabangModel();
            itemCabangModel.setCabang(cabangService.getCabang(noCabang));
            itemCabangModel.setNamaItem(iteminSiItem.nama);
            itemCabangModel.setHargaItem(iteminSiItem.harga);
            itemCabangModel.setKategori(iteminSiItem.kategori);
            itemCabangModel.setUuid(uuid);
            itemCabangModel.setStokItem(stok);
            itemCabangService.addItem(itemCabangModel);
            itemCabangService.updateSiItem(uuid, iteminSiItem.stok-stok);
        }
        model.addAttribute("noCabang", noCabang);
        return "add-item";
    }

    @GetMapping(value = "/item/requestitem/{noCabang}")
    public String requestItem(
        @PathVariable Long noCabang,
        Model model
    ){
        List<ItemDTO> result = itemCabangService.getAllItem();
        model.addAttribute("items", result);
        model.addAttribute("noCabang", noCabang);
        return "form-request-item";
    }

    @PostMapping(value = "/item/requestitem")
    public String postRequestItem(
        @RequestParam("noCabang") Long noCabang,
        @RequestParam("itemuuid") String uuid,
        @RequestParam("itemstok") int stok,
        Model model
    ){
        ItemDTO item = itemCabangService.getItem(uuid);
        // System.out.println(enumKategori.get(item.kategori) + " " + item.kategori);
        itemCabangService.requestItem(uuid, enumKategori.get(item.kategori), stok, noCabang);
        return "request-item";
    }

    @PostMapping(value = "/item/delete")
    public String deleteItem(
        @RequestParam Long id,
        @RequestParam String namaitem,
        Model model
    ){
        itemCabangService.deleteItemFromDB(id);
        model.addAttribute("nama", namaitem);
        return "delete-item";
    }
}