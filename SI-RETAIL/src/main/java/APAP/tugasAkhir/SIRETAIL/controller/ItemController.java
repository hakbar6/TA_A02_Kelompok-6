package APAP.tugasAkhir.SIRETAIL.controller;

import APAP.tugasAkhir.SIRETAIL.model.CabangModel;
import APAP.tugasAkhir.SIRETAIL.model.ItemCabangModel;
import APAP.tugasAkhir.SIRETAIL.rest.ItemDTO;
import APAP.tugasAkhir.SIRETAIL.service.CabangService;
import APAP.tugasAkhir.SIRETAIL.service.CabangServiceImpl;
import APAP.tugasAkhir.SIRETAIL.service.ItemCabangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ItemController {

    @Qualifier("itemCabangServiceImpl")
    @Autowired
    private ItemCabangService itemCabangService;

    @Qualifier("cabangServiceImpl")
    @Autowired
    CabangService cabangService;

    // pekerjaan evan
    @GetMapping("/item/viewall") //menampilkan halaman untuk item dan semua item yang ada
    public String getItem(
        Model model
    ){
        List<ItemDTO> result = itemCabangService.getAllItem();
        model.addAttribute("item", result);
        return "home";
    }

    @GetMapping("/cabang/addItem/{noCabang}") //halaman untuk menambahkan item dari item --> this goes to form
    public String addItem (Model model, @PathVariable Long noCabang) {
        ItemCabangModel item = new ItemCabangModel();
        CabangModel cabang = cabangService.getCabang(noCabang);
        List<ItemDTO> listItem = itemCabangService.getAllItem();

        item.setCabang(cabang);
        model.addAttribute("cabang", cabang);
        model.addAttribute("listItem", listItem);
        model.addAttribute("newItem", item);
        model.addAttribute("noCabang", noCabang);
        return "form-add-item";
    }
/*
    @PostMapping(value = "/cabang/addItem2/{noCabang}", params = { "save" }) //
    public String addItemPost2 (
        @ModelAttribute ItemCabangModel item,
        @RequestParam("namaItem") String namaItem,
        @PathVariable Long noCabang,
        String uuid, Model model){
        
        List<ItemDTO> listItem = itemCabangService.getAllItem();
        ItemCabangModel itemSelected = new ItemCabangModel();

        for(ItemDTO lstItem : listItem){
            if(lstItem.uuid.equals(item.getUuid())){
                itemCabangService.penggunaanBarang(itemSelected.getUuid(), itemSelected.getStokItem(), itemSelected.getCabang().getNoCabang());
            }
        }
        model.addAttribute("cabang", item.getCabang());
        model.addAttribute("nama", itemSelected.getNamaItem());
        return "add-item";
    }
*/ //ver 17/12/2021 pagi
    @PostMapping(value = "/cabang/addItem/{noCabang}", params = { "save" }) //
    public String addItemPost (
        @ModelAttribute ItemCabangModel item,
        @RequestParam("namaItem") String namaItem,
        @PathVariable Long noCabang,
        String uuid, Model model){
        
        List<ItemDTO> listItem = itemCabangService.getAllItem();
        ItemCabangModel itemSelected = new ItemCabangModel();

        for(ItemDTO lstItem : listItem){
            if(lstItem.uuid.equals(item.getUuid())){
                if(lstItem.stok > item.getStokItem()){
                    ItemCabangModel itemCabangIsExist = itemCabangService.getItemInCabang(item.getUuid(), item.getCabang());
                    if(itemCabangIsExist != null){
                        int itemExist = itemCabangIsExist.getStokItem() + item.getStokItem();
                        itemCabangIsExist.setStokItem(itemExist);
                    }else{
                        itemSelected.setUuid(item.getUuid());
                        itemSelected.setCabang(item.getCabang());
                        itemSelected.setNamaItem(item.getNamaItem());
                        itemSelected.setHargaItem(item.getHargaItem());
                        itemSelected.setStokItem(item.getStokItem());
                        itemSelected.setKategori(item.getKategori());
                        itemSelected.setId_promo(1);
                        itemCabangService.addItemCabang(itemSelected);
                    }

                    int stockUpdated = lstItem.stok - item.getStokItem();
                    itemCabangService.updateStock(item.getUuid(), stockUpdated);
                }
            }
        }
        model.addAttribute("cabang", item.getCabang());
        model.addAttribute("nama", itemSelected.getNamaItem());
        model.addAttribute("listItemInCabang", item.getCabang().getListItem());
        return "add-item";
    }
    // pekerjaan evan tutup
}
