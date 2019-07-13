package edu.pucmm.spring_boot_jwt.controladores;

import edu.pucmm.spring_boot_jwt.entidades.Usuario;
import edu.pucmm.spring_boot_jwt.entidades.repositorios.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/")
public class ApiController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Value("${token_jwt}")
    private String llaveSecreta;

    @PostMapping("/auth")
    public ResponseEntity<String> auth(@RequestParam("username") String username, @RequestParam("password") String password){
        String token = "";
        Usuario usuario = usuarioRepository.findByUsername(username);
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        if(usuario==null && !usuario.getPassword().equals(bCryptPasswordEncoder.encode(password))){
           return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        token = generarToken(usuario);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @RequestMapping("/")
    public String index(){
       return "Hola Mundo con JWT";
    }

    /**
     *
     * @param usuario
     * @return
     */
    private String generarToken(Usuario usuario) {

        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(usuario.getNombre())
                .claim("roles",usuario.getRoles())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        llaveSecreta.getBytes()).compact();

        return "Bearer " + token;
    }
}
