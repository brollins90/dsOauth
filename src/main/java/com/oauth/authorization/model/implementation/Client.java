package com.oauth.authorization.model.implementation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brollins on 12/1/2015.
 */
public class Client {

    private String clientId;
    private String clientName;
    private String clientSecret;
    private String clientPostLogoutRedirectUrl;
    private String clientRedirectUrl;

    private List<String> AllowedScopes;

    private Flow flow;

    public Client() {
        AllowedScopes = new ArrayList<>();
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getClientPostLogoutRedirectUrl() {
        return clientPostLogoutRedirectUrl;
    }

    public void setClientPostLogoutRedirectUrl(String clientPostLogoutRedirectUrl) {
        this.clientPostLogoutRedirectUrl = clientPostLogoutRedirectUrl;
    }

    public String getClientRedirectUrl() {
        return clientRedirectUrl;
    }

    public void setClientRedirectUrl(String clientRedirectUrl) {
        this.clientRedirectUrl = clientRedirectUrl;
    }

    public List<String> getAllowedScopes() {
        return AllowedScopes;
    }

    public void addAllowedScopes(String scope) {
        AllowedScopes.add(scope);
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }
}
