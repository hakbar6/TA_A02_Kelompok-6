package APAP.tugasAkhir.SIRETAIL.restservice;

import APAP.tugasAkhir.SIRETAIL.model.CabangModel;
import APAP.tugasAkhir.SIRETAIL.model.UserModel;
import APAP.tugasAkhir.SIRETAIL.repository.CabangDb;
import APAP.tugasAkhir.SIRETAIL.rest.CabangDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.ArrayList;

@Service
@Transactional
public class CabangRestServiceImpl implements CabangRestService{

    @Autowired
    private CabangDb cabangDb;

    // pekerjaan harakan
    @Override
    public CabangModel createCabangAPI(CabangDTO cabangDTO) {
        CabangModel cabang = new CabangModel();
        if ( (cabangDTO.namaCabang.equals("")) || (cabangDTO.alamatCabang.equals("")) ||
                (cabangDTO.noTelpCabang.equals("")) || (cabangDTO.ukuranCabang <= 0) ){
            throw new UnsupportedOperationException();
        }else{
            cabang.setNamaCabang(cabangDTO.namaCabang);
            cabang.setAlamatCabang(cabangDTO.alamatCabang);
            cabang.setNoTelpCabang(cabangDTO.noTelpCabang);
            cabang.setUkuranCabang(cabangDTO.ukuranCabang);
            cabang.setStatusCabang(0);
            return cabangDb.save(cabang);
        }
    }

    @Override
    public CabangModel acceptCabang(Long noCabang, UserModel user) {
        CabangModel cabang = cabangDb.getById(noCabang);
        cabang.setStatusCabang(2);
        cabang.setUser(user);
        cabang.setListItem(new ArrayList<>());
        return cabangDb.save(cabang);
    }

    @Override
    public CabangModel rejectCabang(Long noCabang, UserModel user) {
        CabangModel cabang = cabangDb.getById(noCabang);
        cabang.setStatusCabang(1);
        cabang.setUser(user);
        return cabangDb.save(cabang);
    }

    // pekerjaan harakan tutup

    @Override
    public List<CabangModel> retrieveListCabang() {
        return cabangDb.findAll();
    }
}
