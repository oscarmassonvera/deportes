package com.deportes.deport.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.deportes.deport.entities.Users;

@Repository
public interface IUsersRepo extends CrudRepository<Users,Long> {
    Users findByUsername(String username);
}
