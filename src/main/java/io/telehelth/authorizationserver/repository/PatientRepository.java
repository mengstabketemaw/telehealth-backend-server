package io.telehelth.authorizationserver.repository;

import io.telehelth.authorizationserver.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient,Long> {
}
