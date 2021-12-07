package APAP.tugasAkhir.SIRETAIL.service;

import APAP.tugasAkhir.SIRETAIL.model.RoleModel;
import APAP.tugasAkhir.SIRETAIL.model.UserModel;
import org.apache.catalina.User;

import java.util.List;

public interface UserService {
    UserModel addUser(UserModel user);
    List<UserModel> daftarUser();
    public String encrypt(String password);
    UserModel updateUser(UserModel userBaru);
    UserModel getUserByUsername(String name);
    String confirmPasswordWhenCreate (String password);
    UserModel getUserNameLogin();
    String confirmPasswordWhenUpdate (String oldPassword, String newPassword, String confirmedNewPassword);
    void updatePassword(UserModel user, String newPassword);
}
