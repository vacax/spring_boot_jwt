package edu.pucmm.spring_boot_jwt;

import edu.pucmm.spring_boot_jwt.entidades.Usuario;
import edu.pucmm.spring_boot_jwt.entidades.repositorios.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
public class SpringBootJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJwtApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(UsuarioRepository usuarioRepository){
        System.out.println("Inicializando los datos.");
        return args -> {
            //
            BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
            //Cargando la información.
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(bCryptPasswordEncoder.encode("admin"));
            admin.setNombre("Administrador");
            admin.setRoles(Arrays.asList("ROLE_ADMIN"));
            admin.setAdministrador(true);
            usuarioRepository.save(admin);
        };
    }
}
