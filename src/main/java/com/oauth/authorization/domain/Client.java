package com.oauth.authorization.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false)
    private String clientSecret;

    @Column(nullable = false)
    private String clientPostLogoutRedirectUrl;

    @Column(nullable = false)
    private String clientRedirectUrl;

    @ElementCollection
    private List<String> AllowedScopes;

    @Enumerated(EnumType.STRING)
    private Flow flow;

    @Enumerated(EnumType.STRING)
    private ClientType clientType;
    
    public Client() {
        AllowedScopes = new ArrayList<>();
        AllowedScopes.add("name");
        AllowedScopes.add("email");
        AllowedScopes.add("phone");
        clientType = ClientType.Confidential;
        flow = Flow.AuthorizationCode;
    }

    public Client(String clientId, String clientName, String clientSecret, String clientPostLogoutRedirectUrl,
                  String clientRedirectUrl, List<String> allowedScopes, Flow flow, ClientType clientType) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientSecret = clientSecret;
        this.clientPostLogoutRedirectUrl = clientPostLogoutRedirectUrl;
        this.clientRedirectUrl = clientRedirectUrl;
        AllowedScopes = allowedScopes;
        this.flow = flow;
        this.clientType = clientType;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getClientPostLogoutRedirectUrl() {
        return clientPostLogoutRedirectUrl;
    }

    public String getClientRedirectUrl() {
        return clientRedirectUrl;
    }

    public List<String> getAllowedScopes() {
        return AllowedScopes;
    }

    public Flow getFlow() {
        return flow;
    }

    public ClientType getClientType() {
        return clientType;
    }

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public void setClientPostLogoutRedirectUrl(String clientPostLogoutRedirectUrl) {
		this.clientPostLogoutRedirectUrl = clientPostLogoutRedirectUrl;
	}

	public void setClientRedirectUrl(String clientRedirectUrl) {
		this.clientRedirectUrl = clientRedirectUrl;
	}

	public void setAllowedScopes(List<String> allowedScopes) {
		AllowedScopes = allowedScopes;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
	}

	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}
    
}
