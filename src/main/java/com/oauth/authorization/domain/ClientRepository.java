package com.oauth.authorization.domain;

import com.oauth.authorization.domain.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface ClientRepository extends Repository<Client, String> {

    Page<Client> findAll(Pageable pageable);

    Client findByClientId(String clientId);

    void save(Client client);
}
