package org.jpetto.meetpickback.auth.repository;

import org.jpetto.meetpickback.auth.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
     boolean existsByUsername(String username);
}
