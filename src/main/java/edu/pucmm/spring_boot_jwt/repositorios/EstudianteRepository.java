package edu.pucmm.spring_boot_jwt.repositorios;

import edu.pucmm.spring_boot_jwt.entidades.Estudiante;
import edu.pucmm.spring_boot_jwt.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {


}
