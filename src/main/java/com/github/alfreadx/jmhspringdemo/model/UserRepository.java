package com.github.alfreadx.jmhspringdemo.model;

import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
