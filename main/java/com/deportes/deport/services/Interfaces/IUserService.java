package com.deportes.deport.services.Interfaces;

import java.util.List;

import com.deportes.deport.entities.Users;

public interface IUserService {
    public Users createUser(Users user);
    public Users updateUser(Users user);
    public void deleteUser(Long userId);
    public Users getUserById(Long userId);
    public List<Users> getAllUsers();
}
