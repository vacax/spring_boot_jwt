package edu.pucmm.spring_boot_jwt.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Usuario implements Serializable {

    @Id
    String username;
    String password;
    String nombre;
    @ElementCollection(targetClass = String.class,fetch = FetchType.EAGER)
    List<String> roles;
    boolean administrador;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isAdministrador() {
        return administrador;
    }

    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
