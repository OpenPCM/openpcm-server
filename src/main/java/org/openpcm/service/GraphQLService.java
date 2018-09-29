package org.openpcm.service;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.openpcm.dao.RoleRepository;
import org.openpcm.service.fetchers.AllRolesDataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Service
public class GraphQLService {

    @Autowired
    RoleRepository roleRepository;

    @Value("classpath:roles.graphql")
    Resource resource;

    private GraphQL graphQL;

    @Autowired
    private AllRolesDataFetcher allBooksDataFetcher;

    @PostConstruct
    private void loadSchema() throws IOException {
        // get the schema
        final File schemaFile = resource.getFile();
        // parse schema
        final TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);
        final RuntimeWiring wiring = buildRuntimeWiring();
        final GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring().type("Query", typeWiring -> typeWiring.dataFetcher("allRoles", allBooksDataFetcher)).build();
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }

}
