package APAP.tugasAkhir.SIRETAIL.repository;

import APAP.tugasAkhir.SIRETAIL.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDb extends JpaRepository<RoleModel, Long> {

}
