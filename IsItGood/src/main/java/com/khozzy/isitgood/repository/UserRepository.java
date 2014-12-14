package com.khozzy.isitgood.repository;

import com.khozzy.isitgood.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByLogin(String login);
}
