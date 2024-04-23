package com.deportes.deport.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.deportes.deport.security.filter.JwtAuthenticationFilter;
import com.deportes.deport.security.filter.JwtValidationFilter;




@Configuration
public class SecurityConfig {
     @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    @Bean
    AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.authorizeHttpRequests(                                      // ARREGLAR LOS ROLES Y A QUE PARTE 
                (authz)-> authz.                                                // DE LA APP TIENE ACCESO CADA UNO
                // RESTAURANT
                
                // USUARIO 
                requestMatchers(HttpMethod.GET,"/api/users").permitAll().
                requestMatchers(HttpMethod.POST,"/api/users").permitAll(). //.hasRole("SUBTADMIN").
                // PRODUCTO
                requestMatchers(HttpMethod.GET,"/api/products").hasAnyRole("ADMIN","SUBTADMIN","WAITER").
                requestMatchers(HttpMethod.GET,"/api/products/{id}").hasAnyRole("ADMIN", "WAITER").
                requestMatchers(HttpMethod.POST,"/api/products").hasAnyRole("ADMIN").
                requestMatchers(HttpMethod.PUT,"/api/products/{id}").hasAnyRole("ADMIN").
                requestMatchers(HttpMethod.DELETE,"/api/products/{id}").hasAnyRole("ADMIN").
                // ORDEN

                // FACTURA
                
                // STOCK
                
                // MENU
                
                // RESERVACION

                // AREA DE COCINA

                // ESTADISTICAS
                
                anyRequest().authenticated()).
                addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager())).
                addFilter(new JwtValidationFilter(authenticationConfiguration.getAuthenticationManager())).
                // desabilitamos el csrefer para evitar vulneravilidad
                csrf(config->config.disable()).
                cors(cors->cors.configurationSource(configurationSource())).
                // para que la sesion http no tenga estados y todo lo que 
                // sea autenticacion se maneje en el token
                sessionManagement(
                    managemen->managemen.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS)).build();
    }

    @Bean
    CorsConfigurationSource configurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*")); 
        config.setAllowedMethods(Arrays.asList("GET","POST","DELETE","PUT")); // EN Q METODOS SE USA
        config.setAllowedHeaders(Arrays.asList("Authorization","Content-Type")); // CABECERAS
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter(){
        FilterRegistrationBean<CorsFilter> coresBean = new FilterRegistrationBean<>(
            new CorsFilter(configurationSource()));
        coresBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return coresBean;
    }
}
