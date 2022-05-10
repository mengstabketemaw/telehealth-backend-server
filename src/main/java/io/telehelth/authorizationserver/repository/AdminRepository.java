package io.telehelth.authorizationserver.repository;

import io.telehelth.authorizationserver.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Long> {
}
