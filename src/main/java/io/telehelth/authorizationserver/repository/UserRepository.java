package io.telehelth.authorizationserver.repository;

import io.telehelth.authorizationserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
