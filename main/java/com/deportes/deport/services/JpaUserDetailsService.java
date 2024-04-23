package com.deportes.deport.services;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deportes.deport.entities.Rol;
import com.deportes.deport.entities.Users;
import com.deportes.deport.repositories.IUsersRepo;

@Service
public class JpaUserDetailsService  implements UserDetailsService{

    @Autowired
    private IUsersRepo repository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // BUSCA EL USERNAME EN EL REPOSITORIO
        Users user = repository.findByUsername(username);
        
        // VALIDA SI EL USUARIO EXISTE
        if (user == null) {
            // SI EL USUARIO NO EXISTE, LANZAR UNA EXCEPCIÓN
            throw new UsernameNotFoundException(String.format("El usuario '%s' no existe", username));
        }
        
        // OBTENEMOS LOS ROLES
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getRol() == Rol.PREMIUM) {
            authorities.add(new SimpleGrantedAuthority("ROLE_PREMIUM"));
        } else if (user.getRol() == Rol.BASIC) {
            authorities.add(new SimpleGrantedAuthority("ROLE_BASIC"));
        }  
    
        // RETORNAR UN OBJETO USERDETAILS CON LA INFORMACIÓN DEL USUARIO
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.isEnabled(),
            true, // CUENTA NO CADUCADA
            true, // CREDENCIALES NO CADUCADAS
            true, // CUENTA NO BLOQUEADA
            authorities
        );
    }

}
