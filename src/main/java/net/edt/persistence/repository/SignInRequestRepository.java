package net.edt.persistence.repository;

import net.edt.persistence.domain.SignInRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SignInRequestRepository extends JpaRepository<SignInRequest, UUID> {
}
