package APAP.tugasAkhir.SIRETAIL.service;

import APAP.tugasAkhir.SIRETAIL.model.CabangModel;

public interface CabangService {
    CabangModel addCabang (CabangModel cabang);
    CabangModel updateCabang (CabangModel cabang);
    void deleteCabang (Long noCabang);
    CabangModel getCabang(Long noCabang);
}
