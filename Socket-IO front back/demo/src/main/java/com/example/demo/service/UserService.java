package com.example.demo.service;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final Set<String> connectedUsers = new HashSet<>();

    public void addUser(String username) {
        connectedUsers.add(username);
    }

    public void removeUser(String username) {
        connectedUsers.remove(username);
    }

    public Set<String> getAllUsers() {
        return connectedUsers;
    }
}
