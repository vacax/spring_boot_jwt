package edu.pucmm.spring_boot_jwt.encapsulaciones;

/**
 *
 * @param usuario
 * @param password
 */
public record AuthRequest(String usuario, String password) {
}
