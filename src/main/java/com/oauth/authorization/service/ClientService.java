package com.oauth.authorization.service;

import com.oauth.authorization.domain.Client;

public interface ClientService {

    Client findClient(String clientId);
    void addClient(Client client);
    void updateClient(Client client, String clientId);
}
