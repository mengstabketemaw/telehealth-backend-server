package io.telehelth.authorizationserver.repository;

import io.telehelth.authorizationserver.entity.Admin;
import io.telehelth.authorizationserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin,Long> {
    Optional<Admin> findByUser(User user);
}
