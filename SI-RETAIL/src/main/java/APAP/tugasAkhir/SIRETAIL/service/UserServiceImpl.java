package APAP.tugasAkhir.SIRETAIL.service;

import APAP.tugasAkhir.SIRETAIL.model.UserModel;
import APAP.tugasAkhir.SIRETAIL.repository.UserDB;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDB userDB;

    @Override
    public UserModel addUser(UserModel user) {
        String pass = encrypt(user.getPassword());
        user.setPassword(pass);
        return userDB.save(user);
    }

    @Override
    public List<UserModel> daftarUser() {
        return userDB.findAll();
    }

    @Override
    public String confirmPasswordWhenCreate (String password){
        String error = "none";
        boolean digitInPassword = false;
        boolean passwordContainsLetter = false;
        boolean passwordContainsdigit = false;
        boolean passwordContainsspecial = false;

        passwordContainsLetter = password.matches(".*[A-Za-z]+.*");
        passwordContainsdigit = password.matches("[0-9]");
        passwordContainsspecial = password.matches("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

        if(password.length()>=8){
            for (char i : password.toCharArray()) {
                if (Character.isDigit(i)){
                    digitInPassword = true;
                    return "update-password-berhasil";
                }
            }
        }

        if(passwordContainsLetter == false){
            error = "Password harus terdiri atas minimal 1 buah huruf, Proses pembuatan password dibatalkan";
        }

        if(passwordContainsdigit == false){
            error = "Password harus terdiri atas minimal 1 buah angka, Proses pembuatan password dibatalkan";
        }

        if(passwordContainsspecial == false){
            error = "Password harus terdiri atas minimal 1 buah karakter spesial, Proses pembuatan password dibatalkan";
        }

        if((password.length() <= 7)){
            error = "Panjang password harus terdiri dari minimal 8 karakter (huruf dan angka). Silahkan mengulang proses update password.";
        }
        return error;

    }

    @Override
    public UserModel updateUser(UserModel userBaru) {
        return userDB.save(userBaru);
    }

    @Override
    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }


}
