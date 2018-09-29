package org.openpcm.service.fetchers;

import java.util.List;

import org.openpcm.dao.RoleRepository;
import org.openpcm.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

@Component
public class AllRolesDataFetcher implements DataFetcher<List<Role>> {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<Role> get(DataFetchingEnvironment environment) {
        return Lists.newArrayList(roleRepository.findAll());
    }

}
