package APAP.tugasAkhir.SIRETAIL.service;

import APAP.tugasAkhir.SIRETAIL.model.CabangModel;
import APAP.tugasAkhir.SIRETAIL.model.UserModel;

import java.util.List;

public interface CabangService {
    CabangModel addCabang (CabangModel cabang, UserModel user);
    CabangModel updateCabang (CabangModel cabang);
    void deleteCabang (Long noCabang);
    CabangModel getCabang(Long noCabang);
    CabangModel acceptCabang(Long noCabang, UserModel user);
    CabangModel rejectCabang(Long noCabang, UserModel user);
    List<CabangModel> getListCabang();
    List<CabangModel> getListCabangByUser(UserModel user);
    List<CabangModel> getListPermintaan();
}
