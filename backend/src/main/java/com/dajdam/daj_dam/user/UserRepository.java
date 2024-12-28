package com.dajdam.daj_dam.user;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    Optional<User> findOne(Long userId);

    User save(User user);

    void delete(Long id);

    void clear();

    User update(User oldUser);
}
