package net.edt.persistence.service;

import net.edt.persistence.domain.AuthToken;
import net.edt.persistence.domain.Role;
import net.edt.persistence.domain.SignInRequest;
import net.edt.persistence.domain.User;
import net.edt.util.SymbolsGenerator;
import net.edt.persistence.repository.AuthTokenRepository;
import net.edt.persistence.repository.SignInRequestRepository;
import net.edt.persistence.repository.UserRepository;
import net.edt.web.exception.EntityAlreadyExistsException;
import net.edt.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SignInRequestRepository signInRequestRepository;

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getFromId(UUID id) {
        Optional<User> found = userRepository.findById(id);
        if (!found.isPresent()) {
            throw createUserNotFoundException(id);
        }
        return found.get();
    }

    public User getFromEmail(String email) {
        Optional<User> found = userRepository.findByEmail(email);
        if (!found.isPresent()) {
            throw new EntityNotFoundException("User with email '" + email + "' not found");
        }
        return found.get();
    }

    public User create(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EntityAlreadyExistsException("Email already in use");
        }

        replaceSignInRequests(user.getSignInRequests());
        user.setPassword(encoder.encode(user.getPassword()));
        user.getRoles().add(Role.USER);

        return userRepository.save(user);
    }

    public User update(UUID id, User user) {
        Optional<User> foundOptional = userRepository.findById(id);
        if (!foundOptional.isPresent()) {
            throw createUserNotFoundException(id);
        }

        user.setId(id);
        replaceSignInRequests(user.getSignInRequests());
        return userRepository.save(user);
    }

    public User remove(UUID id) {
        Optional<User> found = userRepository.findById(id);
        if (!found.isPresent()) {
            throw createUserNotFoundException(id);
        }
        userRepository.delete(found.get());
        return found.get();
    }

    public AuthToken createToken(UUID id, int daysDuration) {
        User user = getFromId(id);
        AuthToken authToken = new AuthToken();
        authToken.setUser(user);
        authToken.setExpirationDate(LocalDateTime.now().plusDays(daysDuration));
        user.setAuthToken(authToken);

        String token = SymbolsGenerator.generate(
                AuthToken.TOKEN_LENGTH,
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&*_-+=".toCharArray());
        authToken.setToken(token);
        return authTokenRepository.save(authToken);
    }

    private void replaceSignInRequests(Set<SignInRequest> requests) {
        Set<SignInRequest> replacements = new HashSet<>();
        for (SignInRequest req : requests) {
            if (req.getId() != null) {
                Optional<SignInRequest> foundRequest = signInRequestRepository.findById(req.getId());
                if (!foundRequest.isPresent()) {
                    throw new EntityNotFoundException("SignInRequest with id '" + req.getId() + "' not found");
                }

                replacements.add(foundRequest.get());
            }
            requests.remove(req);
        }
        requests.addAll(replacements);
    }

    private EntityNotFoundException createUserNotFoundException(UUID id) {
        return new EntityNotFoundException("User with id '" + id + "' not found");
    }

}
