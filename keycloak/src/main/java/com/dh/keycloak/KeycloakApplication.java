package com.dh.keycloak;

import com.dh.keycloak.service.KeycloakClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.List;

@SpringBootApplication
public class KeycloakApplication implements CommandLineRunner {

	@Autowired
	private KeycloakClientService keycloakClientService;

	public static void main(String[] args) {
		SpringApplication.run(KeycloakApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		keycloakClientService.createRealmAndClient("e-commerce", "ms-users-client", "ms-bills-client", "secret", List.of("admin", "user"));
		keycloakClientService.addGroupsToToken("e-commerce", "profile");
		System.exit(0);
	}

}
