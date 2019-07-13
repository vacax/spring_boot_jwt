package edu.pucmm.spring_boot_jwt.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAutorizacionFilter extends OncePerRequestFilter {

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    @Value("${token_jwt}")
    String SECRET = "llave_secreta_aqui_asdasd_adadsasdas_asdasdasdas_1231231_asdasdsa_asdasdasdasd";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("entrando al filtro de autorización local....");
        System.out.println("Servlet Path: "+request.getServletPath());
        System.out.println("Query String: "+request.getQueryString());
        if(request.getServletPath().startsWith("/api/auth")){
            filterChain.doFilter(request, response);
            return;
        }
        
        if(request.getServletPath().startsWith("/api/")){
            if (existeJWTToken(request, response)) {
                Claims claims = validateToken(request);
                if (claims.get("authorities") != null) {
                    setUpSpringAuthentication(claims);
                } else {
                    SecurityContextHolder.clearContext();
                }
                filterChain.doFilter(request, response);
            }else{
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, "Sin permisos");
                return;
            }

        }
        filterChain.doFilter(request, response);
    }

    /**
     * 
     * @param request
     * @return
     */
    private Claims validateToken(HttpServletRequest request) {
        System.out.println("El SECRET es: "+SECRET);
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
    }

    /**
     *
     * @param claims
     */
    private void setUpSpringAuthentication(Claims claims) {

        List authorities = (List) claims.get("roles");
        List<SimpleGrantedAuthority> listaAuto = new ArrayList<>();
        listaAuto.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,listaAuto);
        SecurityContextHolder.getContext().setAuthentication(auth);

    }

    /**
     * 
     * @param request
     * @param res
     * @return
     */
    private boolean existeJWTToken(HttpServletRequest request, HttpServletResponse res) {
        String authenticationHeader = request.getHeader(HEADER);
        if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
            return false;
        return true;
    }

}
