package net.edt.persistence.repository;

import net.edt.persistence.domain.SignInSessionCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignInSessionCodeRepository extends JpaRepository<SignInSessionCode, String> {

}
