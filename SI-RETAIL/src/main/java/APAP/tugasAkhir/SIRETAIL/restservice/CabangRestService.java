package APAP.tugasAkhir.SIRETAIL.restservice;

import APAP.tugasAkhir.SIRETAIL.model.CabangModel;
import APAP.tugasAkhir.SIRETAIL.rest.CabangDTO;

public interface CabangRestService {
    // pekerjaan harakan
    CabangModel createCabangAPI(CabangDTO cabangDTO);
    // pekerjaan harakan tutup
}
