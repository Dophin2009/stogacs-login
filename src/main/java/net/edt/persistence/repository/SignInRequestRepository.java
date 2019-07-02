package net.edt.persistence.repository;

import net.edt.persistence.domain.SignInRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignInRequestRepository extends JpaRepository<SignInRequest, String> {
}
