package ro.univ.medical.gestiune_pacienti;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePasswords {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("=== HASH-URI BCrypt GENERATE ACUM ===");
        System.out.println();
      //  System.out.println(encoder.encode("doctor123"));
       // System.out.println(encoder.encode("asistenta456"));


    }
}
