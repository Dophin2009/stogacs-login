package net.edt.web.repository;

import net.edt.web.domain.SignInRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SignInRequestRepository extends JpaRepository<SignInRequest, UUID> {
}
