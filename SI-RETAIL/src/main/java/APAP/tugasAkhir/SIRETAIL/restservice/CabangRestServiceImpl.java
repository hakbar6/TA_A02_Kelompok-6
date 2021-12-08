package APAP.tugasAkhir.SIRETAIL.restservice;

import APAP.tugasAkhir.SIRETAIL.model.CabangModel;
import APAP.tugasAkhir.SIRETAIL.repository.CabangDb;
import APAP.tugasAkhir.SIRETAIL.rest.CabangDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CabangRestServiceImpl implements CabangRestService{

    @Autowired
    private CabangDb cabangDb;

    @Override
    public CabangModel createCabangAPI(CabangDTO cabangDTO) {
        System.out.println("masuk");
        CabangModel cabang = new CabangModel();
        cabang.setNamaCabang(cabangDTO.namaCabang);
        cabang.setAlamatCabang(cabangDTO.alamatCabang);
        cabang.setNoTelpCabang(cabangDTO.noTelpCabang);
        cabang.setUkuranCabang(cabangDTO.ukuranCabang);
        cabang.setStatusCabang(0);
        return cabangDb.save(cabang);
    }
}
