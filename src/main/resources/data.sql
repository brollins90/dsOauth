INSERT INTO Client(clientId, clientName, clientSecret, clientPostLogoutRedirectUrl, clientRedirectUrl, allowedScopes, flow, clientType)
           VALUES ('clientid1', 'clientid1', 'xoxoxo', 'http://localhost:5000/login/oauthlogout', 'http://localhost:5000/login/oauthcallback', 'username', 'AuthorizationCode', 'Confidential');

INSERT INTO User(username, password, name, email, phone)
          VALUES ('user1', 'password', 'Users actual Name', 'user1@example.com', '111-111-1111');
INSERT INTO User(username, password, name, email, phone)
          VALUES ('user2', 'password', 'Users2 actual Name', 'user2@example.com', '2222-111-1111');
