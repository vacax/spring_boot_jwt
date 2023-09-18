package edu.pucmm.spring_boot_jwt.config;


import edu.pucmm.spring_boot_jwt.servicios.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SeguridadConfig {

    private UsuarioService usuarioService;
    private JWTAutorizacionFilter jwtAutorizacionFilter;

    /**
     *
     * @param usuarioService
     * @param jwtAutorizacionFilter
     */
    public SeguridadConfig(UsuarioService usuarioService, JWTAutorizacionFilter jwtAutorizacionFilter) {
        this.usuarioService = usuarioService;
        this.jwtAutorizacionFilter = jwtAutorizacionFilter;
    }

    /**
     * Delegando el control del codificador de las contraseñas.
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authenticador Provider utilizando JPA.
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configurando dos filtros diferentes, en este caso para no almacenar las peticiones en la sesion
     * para el caso de JWT en Spring Boot.
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    @Order(1) //indica el orden del bean en inicializar
    public SecurityFilterChain securityFilterApi(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests( authorization -> {
                    try {
                        authorization
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/**")).authenticated()
                                .and().sessionManagement()
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                .and()
                                .authenticationProvider(authenticationProvider())
                                .addFilterBefore(jwtAutorizacionFilter, UsernamePasswordAuthenticationFilter.class);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .build();
    }

    /**
     * Filtro que tradicional para trabajar con las llamadas MVC de Spring Boot
     * Puede ver el endpoint http://localhost:8080/contador para visualizar el almacenamiento
     * en la sesión.
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChainGeneral(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests( authorization -> authorization
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/auth/**")).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.permitAll())
                .build();
    }

    /**
     *
     * @param config
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
