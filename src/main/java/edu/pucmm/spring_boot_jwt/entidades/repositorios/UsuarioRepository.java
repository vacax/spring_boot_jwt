package edu.pucmm.spring_boot_jwt.entidades.repositorios;

import edu.pucmm.spring_boot_jwt.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Usuario findByUsername(String username);

}
