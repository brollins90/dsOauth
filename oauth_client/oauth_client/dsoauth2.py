from social.backends.oauth import BaseOAuth2

class DSOAuth2(BaseOAuth2):
    """Github OAuth authentication backend"""
    name = 'dsoauth2'
    #AUTHORIZATION_URL = 'http://django-oauth-toolkit.herokuapp.com/o/authorize/'
    AUTHORIZATION_URL = 'http://localhost:8080/oauth/authorize/'
    #AUTHORIZATION_URL = 'http://ds.transvec.com/oauth/authorize/'

    #ACCESS_TOKEN_URL = 'http://django-oauth-toolkit.herokuapp.com/o/token/'
    ACCESS_TOKEN_URL = 'http://localhost:8080/oauth/token/'
    #ACCESS_TOKEN_URL = 'http://ds.transvec.com/oauth/token/'

    REDIRECT_STATE = False
    ACCESS_TOKEN_METHOD = 'POST'
    SCOPE_SEPARATOR = ','
    DEFAULT_SCOPE = ['email']
    EXTRA_DATA = [
        ('id', 'id'),
        ('expires', 'expires')
    ]

    def get_user_details(self, response):
        print('get_user_details response',response)
        print('extra data',self.EXTRA_DATA)
        """Return user details from Github account"""
        rvals = {'username': response.get('access_token')[15:],
                'email': response.get('email') or '',
                'first_name': response.get('access_token')[15:]}
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

