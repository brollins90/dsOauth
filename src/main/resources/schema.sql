CREATE TABLE IF NOT EXISTS client (clientId VARCHAR(MAX) PRIMARY KEY, clientName VARCHAR(MAX), clientSecret VARCHAR(MAX), clientPostLogoutRedirectUrl VARCHAR(MAX), clientRedirectUrl VARCHAR(MAX), allowedScopes VARCHAR(MAX), flow VARCHAR(MAX), clientType VARCHAR(MAX));

CREATE TABLE IF NOT EXISTS user(username VARCHAR(MAX) PRIMARY KEY, password VARCHAR(MAX), name VARCHAR(MAX), email VARCHAR(MAX), phone VARCHAR(MAX));
