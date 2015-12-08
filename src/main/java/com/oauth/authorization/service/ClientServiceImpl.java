package com.oauth.authorization.service;

import com.oauth.authorization.domain.Client;
import com.oauth.authorization.domain.ClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component("clientService")
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client findClient(String clientId) {
        Assert.hasLength(clientId, "clientId must not be empty");
        return this.clientRepository.findByClientId(clientId);
    }

	@Override
	public void addClient(Client client) {
        Assert.hasLength(client.getClientId(), "clientId must not be empty");
		Assert.isNull(this.clientRepository.findByClientId(client.getClientId()), "clientId must be unique");
		clientRepository.save(client);
	}

	@Override
	public void updateClient(Client client, String clientId) {
		// TODO Auto-generated method stub
		
	}
}
