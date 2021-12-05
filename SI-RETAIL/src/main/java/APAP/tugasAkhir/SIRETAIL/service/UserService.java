package APAP.tugasAkhir.SIRETAIL.service;

import APAP.tugasAkhir.SIRETAIL.model.UserModel;
import org.apache.catalina.User;

import java.util.List;

public interface UserService {
    UserModel addUser(UserModel user);
    List<UserModel> daftarUser();
    public String encrypt(String password);
    UserModel updateUser(UserModel userBaru);
    String confirmPasswordWhenCreate (String password);
}
