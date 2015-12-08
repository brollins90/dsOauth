package com.oauth.authorization.domain;

import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, String> {

    Client findByClientId(String clientId);
    void addClient(Client client);
    void updateClient(Client client, String clientId);
}
