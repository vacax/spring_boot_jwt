package edu.pucmm.spring_boot_jwt.config;

import edu.pucmm.spring_boot_jwt.servicios.JwtService;
import edu.pucmm.spring_boot_jwt.servicios.UsuarioService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
/*import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;*/
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTAutorizacionFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private UsuarioService userDetailsService;

    /**
     *
     * @param jwtService
     * @param userDetailsService
     */
    public JWTAutorizacionFilter(JwtService jwtService, UsuarioService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filtro utilizado para validar el
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            //Puedo traer los roles desde la base datos, pero en un esquema JWT se quiere ser descentralizado
            //UserDetails userDetails = userDetailsService.loadUserByUsername(username);


            //Obtengo los roles directamente de los Claims
            String roles = jwtService.extractClaim(token, "roles");

            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (String role : roles.split(",")) {
                logger.info("Role Filtro : "+role);
                grantedAuthorities.add(new SimpleGrantedAuthority(role));
            }

            logger.info("Usuario del JWT: "+jwtService.extractUsername(token));
            UserDetails userDetails = new User(jwtService.extractUsername(token),"null",true, true, true, true, grantedAuthorities);

            if (jwtService.validateToken(token)) {

                //En caso que busquemos los roles desde la base de datos.
                //UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                //Pasando directamente
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, grantedAuthorities);

                //
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

}
