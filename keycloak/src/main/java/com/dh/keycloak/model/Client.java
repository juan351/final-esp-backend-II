package com.dh.keycloak.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Client {

    private String clientId;

    private String clientSecret;

    private List<String> roles;

}
