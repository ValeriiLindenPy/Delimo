package com.dajdam.daj_dam.user.impl;

import org.springframework.stereotype.Repository;
import com.dajdam.daj_dam.user.User;
import com.dajdam.daj_dam.user.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @Override
    public Optional<User> findOne(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }


    @Override
    public User save(User user) {
        user.setId(getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    private Long getId() {
        Long currentId = users.values().stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0);
        return ++currentId;
    }
}
