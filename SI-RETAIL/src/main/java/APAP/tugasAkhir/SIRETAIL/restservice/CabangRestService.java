package APAP.tugasAkhir.SIRETAIL.restservice;

import APAP.tugasAkhir.SIRETAIL.model.CabangModel;
import APAP.tugasAkhir.SIRETAIL.model.UserModel;
import APAP.tugasAkhir.SIRETAIL.rest.CabangDTO;

public interface CabangRestService {
    // pekerjaan harakan
    CabangModel createCabangAPI(CabangDTO cabangDTO);
    CabangModel acceptCabang(Long noCabang, UserModel user);
    CabangModel rejectCabang(Long noCabang, UserModel user);
    // pekerjaan harakan tutup
}
