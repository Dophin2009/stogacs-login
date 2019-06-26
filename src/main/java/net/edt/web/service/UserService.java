package net.edt.web.service;

import net.edt.web.domain.User;
import net.edt.web.exception.EntityNotFoundException;
import net.edt.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
        return userRepository.save(user);
    }

    public User update(UUID id, User user) {
        Optional<User> foundOptional = userRepository.findById(id);
        if (!foundOptional.isPresent()) {
            throw createUserNotFoundException(id);
        }

        user.setId(id);
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

    private EntityNotFoundException createUserNotFoundException(UUID id) {
        return new EntityNotFoundException("User with id " + id + " not found");
    }

}
