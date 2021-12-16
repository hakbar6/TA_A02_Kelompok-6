package APAP.tugasAkhir.SIRETAIL.service;

import APAP.tugasAkhir.SIRETAIL.model.ItemCabangModel;
import APAP.tugasAkhir.SIRETAIL.rest.ItemDTO;

import java.util.List;

public interface ItemCabangService {
    void addItem(ItemCabangModel item);
    List<ItemDTO> getAllItem();
    ItemDTO getItem(String uuid);
    void updateSiItem(String uuid, int newStock);
}
