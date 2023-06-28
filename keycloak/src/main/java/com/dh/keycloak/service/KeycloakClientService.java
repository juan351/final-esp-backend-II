package com.dh.keycloak.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.*;
import org.springframework.stereotype.Service;
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class KeycloakClientService {

    private final Keycloak keycloak;

    public KeycloakClientService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void createRealmAndClient(String reino, String clientId, String clientId2, String clientSecret, List<String> roles) {
        // Creamos el reino
        RealmsResource realmsResource = keycloak.realms();
        RealmRepresentation realm = new RealmRepresentation();
        realm.setRealm(reino);
        realm.setEnabled(true);
        keycloak.realms().create(realm);
        System.out.println("Reino creado exitosamente");

        // Seteamos roles a nivel Reino
        RolesResource rolesResource = realmsResource.realm(reino).roles();
        RoleRepresentation rolAdmin = new RoleRepresentation();
        rolAdmin.setName("app_admin");
        RoleRepresentation rolUser = new RoleRepresentation();
        rolUser.setName("app_user");
        List<RoleRepresentation> rolesReino = List.of(rolAdmin, rolUser);
        for (RoleRepresentation rol : rolesReino) {
            RoleRepresentation roleRepresentation = new RoleRepresentation();
            roleRepresentation.setName(rol.getName());
            rolesResource.create(roleRepresentation);
        }

        // Creamos el grupo PROVIDERS
        GroupRepresentation groupProviders = new GroupRepresentation();
        groupProviders.setName("PROVIDERS");
        realmsResource.realm(reino).groups().add(groupProviders);

        // Creamos el usuario user1
        UserRepresentation user1 = new UserRepresentation();
        user1.setId("f06f63e6-3022-4abb-a6e4-9baa2061e934");
        user1.setUsername("user1");
        user1.setEnabled(true);
        user1.setEmail("user1@example.com");
        user1.setFirstName("User");
        user1.setLastName("One");
        user1.setRealmRoles(List.of("app_user", "app_admin"));

        // Establecemos la contraseña para user1
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue("123456");
        passwordCred.setTemporary(false);
        user1.setCredentials(List.of(passwordCred));

        // Creamos user1
        realmsResource.realm(reino).users().create(user1);

        // Creamos el usuario Juan para agregar a Providers
        UserRepresentation userJuan = new UserRepresentation();
        userJuan.setId("juan-id"); // Aquí debes poner un ID único para Juan
        userJuan.setUsername("juan");
        userJuan.setEnabled(true);
        userJuan.setEmail("juan@example.com");
        userJuan.setFirstName("Juan");
        userJuan.setGroups(List.of("PROVIDERS"));


        CredentialRepresentation passwordCredJuan = new CredentialRepresentation();
        passwordCredJuan.setType(CredentialRepresentation.PASSWORD);
        passwordCredJuan.setValue("123456");
        passwordCredJuan.setTemporary(false);
        userJuan.setCredentials(List.of(passwordCredJuan));


        realmsResource.realm(reino).users().create(userJuan);
        System.out.println("Usuario Juan creado exitosamente");

        /* Obtenemos el grupo Proveedores
        GroupRepresentation groupProveedores = realmsResource.realm(reino).groups().groups().stream()
                .filter(group -> group.getName().equals("PROVIDERS"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Grupo PROVIDERS no encontrado"));

        // Agregamos a Juan al grupo Proveedores
        UserRepresentation userJuanMember = realmsResource.realm(reino).users().get("juan").toRepresentation();
        GroupResource groupResource = realmsResource.realm(reino).groups().group(groupProveedores.getId());
        groupResource.members().add(userJuanMember);
        System.out.println("Usuario Juan agregado al grupo PROVIDERS exitosamente");*/



        // Verificamos que se haya creado el reino y creamos los clientes
        RealmRepresentation realmRepresentation = realmsResource.realm(reino).toRepresentation();
        String realmName = realmRepresentation.getRealm();
        if(Objects.equals(realmName, reino)) {
            RealmResource realmResource = keycloak.realm(reino);
            ClientsResource clientsResource = realmResource.clients();
            ClientsResource gwResource = realmResource.clients();

            // Creamos clientes
            ClientRepresentation client = new ClientRepresentation();
            client.setClientId(clientId);
            client.setSecret(clientSecret);
            client.setServiceAccountsEnabled(true);
            client.setDirectAccessGrantsEnabled(true);
            client.setEnabled(true);

            ClientRepresentation client2 = new ClientRepresentation();
            client2.setClientId(clientId2);
            client2.setSecret(clientSecret);
            client2.setServiceAccountsEnabled(true);
            client2.setDirectAccessGrantsEnabled(true);
            client2.setEnabled(true);

            // Creamos gateway
            ClientRepresentation gateway = new ClientRepresentation();
            String url = "http://localhost:8090";
            gateway.setClientId("api-gateway-client");
            gateway.setSecret("secret");
            gateway.setRootUrl(url);
            gateway.setWebOrigins(List.of("/*"));
            gateway.setRedirectUris(List.of(url+"/*"));
            gateway.setAdminUrl(url);
            gateway.setEnabled(true);
            gateway.setServiceAccountsEnabled(true);
            gateway.setDirectAccessGrantsEnabled(true);



            // Verificamos que se hayan creado los clientes y creamos los roles a nivel Cliente
            Response response = clientsResource.create(client);
            Response response2 = clientsResource.create(client2);
            Response responseGW = gwResource.create(gateway);
            if (response.getStatus() == 201 && response2.getStatus() == 201 && responseGW.getStatus() == 201) {
                // Obtiene el ultimo segmento de la url, en este caso seria el clientId
                String createdClientId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                System.out.println("ClientId: " + createdClientId + ". Roles: " + roles);

                String createdClientId2 = response2.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                System.out.println("ClientId: " + createdClientId2 + ". Roles: " + roles);
                // Lo mismo pero con gw
                String createdClientIdGW = responseGW.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                System.out.println("GW: " + createdClientIdGW + ". Roles: " + roles);

                // Obtenemos id de clientes
                ClientResource clientResource = clientsResource.get(createdClientId);
                ClientResource clientResource2 = clientsResource.get(createdClientId2);
                ClientResource clientResourceGW = gwResource.get(createdClientIdGW);
                for (String rol : roles) {
                    RoleRepresentation roleRepresentation = new RoleRepresentation();
                    roleRepresentation.setName(rol);
                    roleRepresentation.setClientRole(true);
                    roleRepresentation.setContainerId(createdClientId);

                    clientResource.roles().create(roleRepresentation);
                    clientResource2.roles().create(roleRepresentation);
                    clientResourceGW.roles().create(roleRepresentation);
                }
                System.out.println("Recorrido completo");
            } else {
                System.out.println("Error: " + response);
            }

            addRoles(reino, clientId);
        }


    }

    public void addGroupsToToken(String realm, String scope){
        RealmResource realmResource = keycloak.realm(realm);
        List<ClientScopeRepresentation> scopes = realmResource.clientScopes().findAll();
        ClientScopeRepresentation clientScope = scopes.stream()
                .filter(cs -> cs.getName().equals(scope))
                .findFirst()
                .orElse(null);

        String id = clientScope.getId();

        ProtocolMapperRepresentation groupMembership = new ProtocolMapperRepresentation();
        groupMembership.setName("group");
        groupMembership.setProtocol("openid-connect");
        groupMembership.setProtocolMapper("oidc-group-membership-mapper");
        groupMembership.getConfig().put("full.path", "false");
        groupMembership.getConfig().put("access.token.claim", "true");
        groupMembership.getConfig().put("id.token.claim", "true");
        groupMembership.getConfig().put("userinfo.token.claim", "true");
        groupMembership.getConfig().put("claim.name", "groups");

        ClientScopeResource clientScopeResource = realmResource.clientScopes().get(id);

        clientScopeResource.getProtocolMappers().createMapper(groupMembership);

        ClientScopeRepresentation updatedClientScope = clientScopeResource.toRepresentation();
        clientScopeResource.update(updatedClientScope);
    }

    public void addRoles(String realmName, String clientId){
        RealmResource realm = keycloak.realm(realmName);
        String realmManagementId = realm.clients().findByClientId("realm-management").get(0).getId();
        String platformAdministrationId = realm.clients().findByClientId(clientId).get(0).getId();
        String serviceUserId = realm.clients().get(platformAdministrationId).getServiceAccountUser().getId();
        List<RoleRepresentation> availableRoles = realm.users().get(serviceUserId).roles().clientLevel(realmManagementId).listAvailable();
        List<RoleRepresentation> rolesToAssign = availableRoles.stream().filter(r -> "manage-users".equalsIgnoreCase(r.getName()) || "view-users".equalsIgnoreCase(r.getName()) || "query-users".equalsIgnoreCase(r.getName())).collect(Collectors.toList());
        realm.users().get(serviceUserId).roles().clientLevel(realmManagementId).add(rolesToAssign);
    }
}
