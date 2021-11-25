package APAP.tugasAkhir.SIRETAIL.service;

import APAP.tugasAkhir.SIRETAIL.model.CabangModel;
import APAP.tugasAkhir.SIRETAIL.repository.CabangDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CabangServiceImpl implements CabangService{
    @Autowired
    private CabangDb cabangDb;


    @Override
    public CabangModel addCabang(CabangModel cabang) {
        cabang.setStatusCabang(2);
        return cabangDb.save(cabang);
    }

    @Override
    public CabangModel updateCabang(CabangModel cabang) {
        return cabangDb.save(cabang);
    }

    @Override
    public void deleteCabang(Long noCabang) {
        Optional<CabangModel> cabang = cabangDb.findByNoCabang(noCabang);
        if (cabang.isPresent()){
            CabangModel cb = cabang.get();
            if (cb.getListItem().isEmpty()){
                cabangDb.delete(cb);
            }else{
                throw new UnsupportedOperationException();
            }
        }else{
            throw new NoSuchElementException();
        }
    }

    @Override
    public CabangModel getCabang(Long noCabang) {
        Optional<CabangModel> cabang = cabangDb.findByNoCabang(noCabang);
        if (cabang.isPresent()){
            return cabang.get();
        }else{
            throw new NoSuchElementException();
        }
    }


}