package org.openpcm.graphql.service;

import org.openpcm.model.Role;
import org.openpcm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;

@Component
public class RoleMutation implements GraphQLMutationResolver {

    @Autowired
    private RoleService roleService;

    public Role createRole(Role role) {
        return roleService.create(role);
    }
}
