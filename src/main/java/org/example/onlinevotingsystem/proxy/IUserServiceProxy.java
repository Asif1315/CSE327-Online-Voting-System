package org.example.onlinevotingsystem.proxy;

import org.example.onlinevotingsystem.models.User;

public interface IUserServiceProxy {
    User findByUsername(String username);

    User findAuthenticatedUser();
}