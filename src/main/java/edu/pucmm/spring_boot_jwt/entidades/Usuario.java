package edu.pucmm.spring_boot_jwt.entidades;


import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario implements Serializable {

    @Id
    String username;
    String password;
    String nombre;
    @ElementCollection(targetClass = String.class,fetch = FetchType.EAGER)
    List<String> roles;
    boolean administrador;


}
