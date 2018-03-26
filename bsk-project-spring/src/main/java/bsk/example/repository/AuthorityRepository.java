package bsk.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bsk.example.domain.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	Authority findByAuthority(String authority);
}
