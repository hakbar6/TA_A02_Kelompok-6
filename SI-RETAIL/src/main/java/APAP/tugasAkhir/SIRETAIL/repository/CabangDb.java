package APAP.tugasAkhir.SIRETAIL.repository;


import APAP.tugasAkhir.SIRETAIL.model.CabangModel;
import APAP.tugasAkhir.SIRETAIL.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CabangDb extends JpaRepository<CabangModel, Long> {
    Optional<CabangModel> findByNoCabang(Long noCabang);
    List<CabangModel> findAllByUser(UserModel user);
}
