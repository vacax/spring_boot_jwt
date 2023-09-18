package edu.pucmm.spring_boot_jwt.controladores;

import edu.pucmm.spring_boot_jwt.encapsulaciones.AuthRequest;
import edu.pucmm.spring_boot_jwt.encapsulaciones.AuthResponse;
import edu.pucmm.spring_boot_jwt.entidades.Usuario;
import edu.pucmm.spring_boot_jwt.repositorios.UsuarioRepository;
import edu.pucmm.spring_boot_jwt.servicios.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
public class AutenticarController {


    private UsuarioRepository usuarioRepository;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private Logger logger =  LoggerFactory.getLogger(AutenticarController.class);
    private PasswordEncoder passwordEncoder;

    public AutenticarController(UsuarioRepository usuarioRepository, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }


    @RequestMapping("/")
    public String index(){
        return "Hola Mundo con JWT";
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> auth(@RequestParam("username") String username, @RequestParam("password") String password){

        Usuario usuario = usuarioRepository.findByUsername(username);
        if(usuario==null && !usuario.getPassword().equals(passwordEncoder.encode(password))){
           return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        AuthResponse token = jwtService.generateToken(username);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/generateToken")
    public AuthResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        logger.info("Petición generación de Token: "+authRequest.toString());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.usuario(), authRequest.password()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.usuario());
        } else {
            throw new UsernameNotFoundException("Usuario invalido...");
        }
    }

}
