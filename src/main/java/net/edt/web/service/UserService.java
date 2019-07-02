package net.edt.web.service;

import net.edt.persistence.domain.Role;
import net.edt.persistence.domain.SignInRequest;
import net.edt.persistence.domain.User;
import net.edt.web.exception.EntityAlreadyExistsException;
import net.edt.web.exception.EntityNotFoundException;
import net.edt.persistence.repository.SignInRequestRepository;
import net.edt.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SignInRequestRepository signInRequestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public User create(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EntityAlreadyExistsException("Email already in use");
        }

        replaceSignInRequests(user.getSignInRequests());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
