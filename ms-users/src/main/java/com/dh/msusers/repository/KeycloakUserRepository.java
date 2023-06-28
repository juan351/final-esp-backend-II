package com.dh.msusers.repository;

import com.dh.msusers.model.User;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class KeycloakUserRepository implements IUserRepository {

    private final Keycloak keycloakClient;
    @Value("${dh.keycloak.realm}")
    private String realm;


    @Override
    public User findById(String id) {
        UserRepresentation user = keycloakClient.realm(realm).users().get(id).toRepresentation();
        return toUser(user);
    }


    private User toUser(UserRepresentation userRepresentation) {
        String nationality = null;
        try {
            nationality = userRepresentation.getAttributes().get("nacionalidad").get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new User(userRepresentation.getId(), userRepresentation.getUsername(), userRepresentation.getEmail(), userRepresentation.getFirstName());
    }
}
