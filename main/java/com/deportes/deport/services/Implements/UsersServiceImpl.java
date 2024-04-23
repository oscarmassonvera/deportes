package com.deportes.deport.services.Implements;

import com.deportes.deport.entities.Users;

import com.deportes.deport.repositories.IUsersRepo;
import com.deportes.deport.services.Interfaces.IUserService;

import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsersServiceImpl implements IUserService {

    @Autowired
    private IUsersRepo usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder; // Inyecta el bean PasswordEncoder

    @Override
    public Users createUser(Users user) {
        try {
            // Encripta la contraseña antes de guardar el usuario
            String encodedPassword = ((PasswordEncoder) passwordEncoder).encode(user.getPassword());
            user.setPassword(encodedPassword);
            return usersRepository.save(user);
        } catch (ConstraintViolationException e) {
            throw new IllegalArgumentException("Error de validación al crear usuario: " + e.getMessage());
        }
    }

    @Override
    public Users updateUser(Users user) {
        if (user.getId() == null || !usersRepository.existsById(user.getId())) {
            throw new UsernameNotFoundException("El usuario no existe");
        }
        try {
            return usersRepository.save(user);
        } catch (ConstraintViolationException e) {
            throw new IllegalArgumentException("Error de validación al actualizar usuario: " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(Long userId) {
        if (!usersRepository.existsById(userId)) {
            throw new UsernameNotFoundException("El usuario no existe");
        }
        usersRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Users getUserById(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con ID: " + userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Users> getAllUsers() {
        return (List<Users>) usersRepository.findAll();
    }
}
