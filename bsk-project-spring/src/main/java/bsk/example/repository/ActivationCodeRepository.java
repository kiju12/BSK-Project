package bsk.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bsk.example.domain.ActivationCode;

@Repository
public interface ActivationCodeRepository extends JpaRepository<ActivationCode, Long> {

}
