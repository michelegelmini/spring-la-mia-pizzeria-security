package org.lessons.java.pizzeria.repo;


import java.util.Optional;

import org.lessons.java.pizzeria.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	public Optional<User> findByUsername(String username);
	
	
}
