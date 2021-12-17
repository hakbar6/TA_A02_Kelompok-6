package APAP.tugasAkhir.SIRETAIL.service;

import APAP.tugasAkhir.SIRETAIL.model.CabangModel;
import APAP.tugasAkhir.SIRETAIL.model.ItemCabangModel;
import APAP.tugasAkhir.SIRETAIL.repository.ItemCabangDb;
import APAP.tugasAkhir.SIRETAIL.rest.ItemDTO;

import java.util.List;
import java.util.Optional;

public interface ItemCabangService {
    List<ItemDTO> getAllItem();
    Optional<ItemCabangModel> getItemByUuid(String uuid);
    ItemCabangModel getItemByUuid2 (String uuid);
    boolean penggunaanBarang (String uuid, int penggunaan, Long noCabang);
    ItemCabangModel getItemInCabang (String uuid, CabangModel cabang);
    void addItemCabang (ItemCabangModel item);
    ItemCabangModel getItemByUuidImprv (String UUID);
    ItemCabangModel getItemById (Long id);
    List<ItemCabangModel> getListItem();
    List<ItemCabangModel> getAllListItem (List<ItemDTO> listItem, CabangModel cabang);
    ItemDTO updateStock(String uuid, int stok);
}
