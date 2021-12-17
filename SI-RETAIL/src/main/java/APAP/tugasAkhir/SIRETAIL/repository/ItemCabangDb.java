package APAP.tugasAkhir.SIRETAIL.repository;


import APAP.tugasAkhir.SIRETAIL.model.CabangModel;
import APAP.tugasAkhir.SIRETAIL.model.ItemCabangModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemCabangDb extends JpaRepository<ItemCabangModel, Long> {
    Optional<ItemCabangModel> findById(Long id);
    Optional<ItemCabangModel> findByUuid(String UUID);
    List<ItemCabangModel> findItemCabangModelByCabang(CabangModel cabang);
}
