from social.backends.oauth import BaseOAuth2

class DSOAuth2(BaseOAuth2):
    """Github OAuth authentication backend"""
    name = 'dsoauth2'
    AUTHORIZATION_URL = 'http://django-oauth-toolkit.herokuapp.com/o/authorize/'
    ACCESS_TOKEN_URL = 'http://django-oauth-toolkit.herokuapp.com/o/token/'
    REDIRECT_STATE = False
    ACCESS_TOKEN_METHOD = 'POST'
    SCOPE_SEPARATOR = ','
    EXTRA_DATA = [
        ('id', 'id'),
        ('expires', 'expires')
    ]

    def get_user_details(self, response):
        print('get_user_details response',response)
        """Return user details from Github account"""
        rvals = {'username': response.get('login'),
                'email': response.get('email') or '',
                'first_name': response.get('name')}
        print('rvals',rvals)
        return rvals

    def user_data(self, access_token, *args, **kwargs):
        print('user_data_called')
        return None
        #"""Loads user data from service"""
        #try:
            #url = 'https://api.github.com/user?' + urlencode({
            #'access_token': access_token
            #})
            #return json.load(self.urlopen(url))
        #except ValueError:
            #return None

