package APAP.tugasAkhir.SIRETAIL.restservice;

import APAP.tugasAkhir.SIRETAIL.model.CabangModel;
import APAP.tugasAkhir.SIRETAIL.rest.CabangDTO;

import java.util.List;

public interface CabangRestService {
    // pekerjaan harakan
    CabangModel createCabangAPI(CabangDTO cabangDTO);
    // pekerjaan harakan tutup
    List<CabangModel> retrieveListCabang();
}
