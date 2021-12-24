package APAP.tugasAkhir.SIRETAIL.service;

import java.util.List;

import APAP.tugasAkhir.SIRETAIL.model.ItemCabangModel;
import APAP.tugasAkhir.SIRETAIL.rest.CouponDTO;

public interface KuponService {
    List<CouponDTO> getAllKupon();
    ItemCabangModel kuponApply(Long idItem, int idKupon, float discountAmount);
}
