package APAP.tugasAkhir.SIRETAIL.controller;

import APAP.tugasAkhir.SIRETAIL.model.CabangModel;
import APAP.tugasAkhir.SIRETAIL.model.ItemCabangModel;
import APAP.tugasAkhir.SIRETAIL.model.UserModel;
import APAP.tugasAkhir.SIRETAIL.repository.CabangDb;
import APAP.tugasAkhir.SIRETAIL.rest.ItemDTO;
import APAP.tugasAkhir.SIRETAIL.service.CabangService;
import APAP.tugasAkhir.SIRETAIL.service.ItemCabangService;
import APAP.tugasAkhir.SIRETAIL.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
public class ItemController {
    HashMap<String, Integer> enumKategori;

    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;

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
        @ModelAttribute CabangModel cabang,
        Model model
    ){
        List<ItemDTO> result = itemCabangService.getAllItem();
        List<ItemCabangModel> listItemInCabang = cabang.getListItem();
        // System.out.println("additem");
        // for (ItemDTO itemDTO : result) {
        //     System.out.println(itemDTO.nama);
        // }
        model.addAttribute("itemCabang", listItemInCabang);
        model.addAttribute("items", result);
        model.addAttribute("noCabang", noCabang);
        model.addAttribute("namaCabang", cabangService.getCabang(noCabang).getNamaCabang());
        return "form-add-item2";
    }

    @PostMapping(value="/item/additem", params = "save")
    public String postMethodName(
        @RequestParam("noCabang") Long noCabang,
        @RequestParam("itemuuid") String uuid,
        @RequestParam("itemstok") int stok,
        RedirectAttributes red,
        @ModelAttribute CabangModel cabang,
        Model model
    ) {
        ItemCabangModel itemCabangModel = new ItemCabangModel();
        ItemDTO iteminSiItem = itemCabangService.getItem(uuid);
        if (stok > iteminSiItem.stok) {
            red.addFlashAttribute("pesanError", "Stok " + iteminSiItem.nama + " tidak cukup, stok maksimal = " + iteminSiItem.stok);
            return "redirect:/cabang/view?noCabang="+noCabang;
        }if(iteminSiItem.stok > stok){
            ItemCabangModel itemHasExist = itemCabangService.getItemInCabang(cabangService.getCabang(noCabang), itemCabangModel.getUuid());
            if(itemHasExist != null){
                //Masih belum bisa nangkep kalau barangnya sama
                int newStock = itemHasExist.getStokItem() + stok;
                itemHasExist.setStokItem(newStock);
            }else{
                itemCabangModel.setCabang(cabangService.getCabang(noCabang));
                itemCabangModel.setNamaItem(iteminSiItem.nama);
                itemCabangModel.setHargaItem(iteminSiItem.harga);
                itemCabangModel.setKategori(iteminSiItem.kategori);
                itemCabangModel.setUuid(uuid);
                itemCabangModel.setStokItem(stok);
                itemCabangModel.setId_promo(1);
                itemCabangService.addItem(itemCabangModel);
                
            }
            itemCabangService.updateSiItem(uuid, iteminSiItem.stok-stok);
        }
        model.addAttribute("noCabang", noCabang);
        model.addAttribute("namaCabang", cabangService.getCabang(noCabang).getNamaCabang());
        model.addAttribute("itemname", itemCabangModel.getNamaItem());
        return "add-item";
    }

    @PostMapping(value="/item/additem", params = "addRow")
    public String addRowCabang(
        @RequestParam("noCabang") Long noCabang,
        @ModelAttribute CabangModel cabang,
        BindingResult bindingResult,
        Model model
    ) {
        if(cabang.getListItem() == null || cabang.getListItem().size() == 0){
            cabang.setListItem(new ArrayList<ItemCabangModel>());
        }
        List<ItemDTO> listItemCabang = itemCabangService.getAllItem();
        List<ItemCabangModel> listItemInCabang = cabang.getListItem();
        listItemInCabang.add(new ItemCabangModel());

        model.addAttribute("itemCabang", listItemInCabang);
        model.addAttribute("items", listItemCabang);
        model.addAttribute("noCabang", noCabang);
        model.addAttribute("namaCabang", cabangService.getCabang(noCabang).getNamaCabang());
        return "form-add-item2";
    }
/*
    @RequestMapping(value="/item/additem", method = RequestMethod.POST, params = "deleteRow")
    public String deleteRow(
        @RequestParam("noCabang") Long noCabang,
        Authentication authentication,
        final BindingResult bindingResult,
        final HttpServletRequest req,
        Model model
    ){
        String username = ((UserDetails)authentication.getPrincipal()).getUsername();
        UserModel user = userService.getUserByUsername(username);
        List<ItemDTO> listItemCabang =itemCabangService.getAllItem();

        final Integer idRow = Integer.valueOf(req.getParameter("deleteRow"));
        cabangService.getCabang(noCabang).getListItem().remove(idRow.intValue());

        model.addAttribute("listItem", listItemCabang);
        model.addAttribute("noCabang", noCabang);


        cabangService.addCabang(cabangService.getCabang(noCabang), user);
        return "form-add-item";
    }
*/
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