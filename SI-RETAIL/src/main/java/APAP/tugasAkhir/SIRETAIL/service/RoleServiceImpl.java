package APAP.tugasAkhir.SIRETAIL.service;

import APAP.tugasAkhir.SIRETAIL.model.RoleModel;
import APAP.tugasAkhir.SIRETAIL.repository.RoleDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    private RoleDb roleDb;


    @Override
    public List<RoleModel> listRole() {
        return roleDb.findAll();
    }
}
