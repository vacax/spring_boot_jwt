package edu.pucmm.spring_boot_jwt;

import edu.pucmm.spring_boot_jwt.entidades.Estudiante;
import edu.pucmm.spring_boot_jwt.entidades.Usuario;
import edu.pucmm.spring_boot_jwt.repositorios.EstudianteRepository;
import edu.pucmm.spring_boot_jwt.repositorios.UsuarioRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
public class SpringBootJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJwtApplication.class, args);
    }

    /**
     * Arranque
     * @param usuarioRepository
     * @return
     */
    @Bean
    public CommandLineRunner loadData(UsuarioRepository usuarioRepository, EstudianteRepository estudianteRepository, PasswordEncoder passwordEncoder){
        System.out.println("Inicializando los datos.");
        return args -> {

            //Cargando la información.
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setNombre("Administrador");
            admin.setRoles(Arrays.asList("ROLE_ADMIN", "ROLE_USER", "ROLE_CONSULTA"));
            admin.setAdministrador(true);
            usuarioRepository.save(admin);

            Usuario consulta = new Usuario();
            consulta.setUsername("consulta");
            consulta.setPassword(passwordEncoder.encode("consulta"));
            consulta.setNombre("Consulta");
            consulta.setRoles(Arrays.asList("ROLE_CONSULTA"));
            usuarioRepository.save(consulta);

            Usuario usuario = new Usuario();
            usuario.setUsername("usuario");
            usuario.setPassword(passwordEncoder.encode("usuario"));
            usuario.setNombre("Usuario");
            usuario.setRoles(Arrays.asList("ROLE_USER"));
            usuarioRepository.save(usuario);

            //Creando estudiantes de forma aleatoria.
            Faker faker =  new Faker();
            System.out.println("La ");
            for (int i = 0; i < 50; i++) {
                estudianteRepository.save(new Estudiante(faker.random().nextInt(10000000, 99999999), faker.name().fullName()));
            }
            estudianteRepository.save(new Estudiante(20011136, "Carlos Camacho"));
        };
    }
}
