package org.openpcm.graphql.service;

import java.util.List;

import org.openpcm.model.Role;
import org.openpcm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

@Component
public class RoleQuery implements GraphQLQueryResolver {

    @Autowired
    private RoleService roleService;

    public List<Role> findAllRoles() {
        return roleService.readAll();
    }

    public Role getRole(Long id) {
        return roleService.read(id);
    }

}
