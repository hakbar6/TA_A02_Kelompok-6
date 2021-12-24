package APAP.tugasAkhir.SIRETAIL.controller;

import APAP.tugasAkhir.SIRETAIL.model.CabangModel;
import APAP.tugasAkhir.SIRETAIL.model.ItemCabangModel;
import APAP.tugasAkhir.SIRETAIL.model.UserModel;
import APAP.tugasAkhir.SIRETAIL.repository.CabangDb;
import APAP.tugasAkhir.SIRETAIL.repository.ItemCabangDb;
import APAP.tugasAkhir.SIRETAIL.rest.CouponDTO;
import APAP.tugasAkhir.SIRETAIL.rest.ItemDTO;
import APAP.tugasAkhir.SIRETAIL.service.CabangService;
import APAP.tugasAkhir.SIRETAIL.service.ItemCabangService;
import APAP.tugasAkhir.SIRETAIL.service.KuponService;
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

    @Autowired
    private KuponService kuponService;

    @GetMapping("/item/additem/{noCabang}")
    public String addItem(
        @PathVariable Long noCabang,
        Authentication authentication,
        Model model
    ){
        CabangModel cabang = cabangService.getCabang(noCabang);
        String username = ((UserDetails)authentication.getPrincipal()).getUsername();
        UserModel user = userService.getUserByUsername(username);
        
        if(cabang.getUser().getUsername().equals(user.getUsername()) || user.getRole().getRole().equals("Kepala Retail")
                || user.getRole().getRole().equals("Manager Cabang")){
            List<ItemDTO> listItem = itemCabangService.getAllItem();
            cabang.setListItem(new ArrayList<ItemCabangModel>());
            cabang.getListItem().add(new ItemCabangModel());
            model.addAttribute("items",listItem);
            model.addAttribute("cabang",cabang);
            model.addAttribute("noCabang",noCabang);
            return "form-add-item2";
        }
        else{
            return "error/403";
        }
    }

    @PostMapping(value="/item/additem/{noCabang}", params = "save")
    public String postMethodName(
        RedirectAttributes red,
        @PathVariable Long noCabang,
        @ModelAttribute CabangModel cabang,
        Model model
    ) {
        for (int i = 0 ; i < cabang.getListItem().size() ; i++){
            ItemCabangModel itemCabangModel = new ItemCabangModel();
            ItemDTO iteminSiItem = itemCabangService.getItem(cabang.getListItem().get(i).getUuid());
            if (cabang.getListItem().get(i).getStokItem() > iteminSiItem.stok) {
                red.addFlashAttribute("pesanError", "Stok " + iteminSiItem.nama + " tidak cukup, stok maksimal = " + iteminSiItem.stok);
                return "redirect:/cabang/view?noCabang="+cabang.getNoCabang();
            }if(iteminSiItem.stok > cabang.getListItem().get(i).getStokItem()){
                ItemCabangModel itemHasExist = itemCabangService.getItemInCabang(cabangService.getCabang(cabang.getNoCabang()), cabang.getListItem().get(i).getUuid());
                if(itemHasExist != null){
                    //ini tu udah bener jadinya nambah, tapi entah kenapa di htmlnya masih sama
                    itemHasExist.setStokItem(itemHasExist.getStokItem() + cabang.getListItem().get(i).getStokItem());
                    itemCabangService.addItem(itemHasExist);
                }else{
                    itemCabangModel.setCabang(cabangService.getCabang(cabang.getNoCabang()));
                    itemCabangModel.setNamaItem(iteminSiItem.nama);
                    itemCabangModel.setHargaItem(iteminSiItem.harga);
                    itemCabangModel.setKategori(iteminSiItem.kategori);
                    itemCabangModel.setUuid(iteminSiItem.uuid);
                    itemCabangModel.setStokItem(cabang.getListItem().get(i).getStokItem());
                    itemCabangModel.setId_promo(0);
                    itemCabangService.addItem(itemCabangModel);
                }
                itemCabangService.updateSiItem(iteminSiItem.uuid, iteminSiItem.stok - cabang.getListItem().get(i).getStokItem());
            }
            model.addAttribute("noCabang", cabang.getNoCabang());
            model.addAttribute("namaCabang", cabang.getNamaCabang());
            model.addAttribute("itemname", itemCabangModel.getNamaItem());
        }
        return "add-item";
    }

    @PostMapping(value="/item/additem/{noCabang}", params = "addRow")
    public String addRowCabang(
        @ModelAttribute CabangModel cabang,
        @PathVariable Long noCabang,
        Model model
    ) {
        cabang.getListItem().add(new ItemCabangModel());
        if(cabang.getListItem() == null || cabang.getListItem().size() == 0){
            cabang.setListItem(new ArrayList<ItemCabangModel>());
        }
        List<ItemDTO> listItem = itemCabangService.getAllItem();
        model.addAttribute("items",listItem);
        model.addAttribute("cabang",cabang);
        model.addAttribute("noCabang",noCabang);
        return "form-add-item2";
    }

    @RequestMapping(value="/item/additem/{noCabang}", method = RequestMethod.POST, params = "deleteRow")
    public String deleteRow(
        @ModelAttribute CabangModel cabang,
        @PathVariable Long noCabang,
        @RequestParam(value = "deleteRow", required = true) int index,
        Model model
    ){
        cabang.getListItem().remove(index);
        List<ItemDTO> listItem = itemCabangService.getAllItem();
        model.addAttribute("items",listItem);
        model.addAttribute("cabang",cabang);
        model.addAttribute("noCabang",noCabang);
        return "form-add-item2";
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