package edu.pucmm.spring_boot_jwt.encapsulaciones;

public record AuthResponse(String token, long expiresIn ) {
}
