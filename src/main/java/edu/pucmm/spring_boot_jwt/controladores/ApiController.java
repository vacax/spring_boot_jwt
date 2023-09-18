package edu.pucmm.spring_boot_jwt.controladores;

import edu.pucmm.spring_boot_jwt.entidades.Estudiante;
import edu.pucmm.spring_boot_jwt.entidades.Usuario;
import edu.pucmm.spring_boot_jwt.repositorios.EstudianteRepository;
import edu.pucmm.spring_boot_jwt.repositorios.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class ApiController {


    private EstudianteRepository estudianteRepository;


    public ApiController(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }


    @GetMapping(path = "/estudiante/")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Estudiante> listaEstudiante(){
        return estudianteRepository.findAll();
    }

    @GetMapping(path = "/estudiante/{id}")
    @PreAuthorize("hasRole('CONSULTA')")
    public Estudiante getEstudiante(@PathVariable int id){
        return estudianteRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe"));
    }

    @GetMapping(path = "/")
    @PreAuthorize("hasRole('USER')")
    public String getUsuario(){
        return "Presentando información por el Rol de User";
    }


}
