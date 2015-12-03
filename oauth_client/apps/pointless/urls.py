from django.conf.urls import url
#from apps.pointless import views

from django.views.generic import TemplateView
#from apps.portfolio import views
#from django.views import generic
#from apps.portfolio.models import Project
from django.contrib.auth.decorators import login_required #, permission_required

urlpatterns = [
    url(r'^$', login_required(TemplateView.as_view(template_name='pointless/index.html')),name='home'),
    url(r'^profile/$', login_required(TemplateView.as_view(template_name='pointless/profile.html')),name='profile'),
    url(r'^gallery/$', login_required(TemplateView.as_view(template_name='pointless/gallery.html')),name='gallery'),
    #url(r'^$', views.IndexView.as_view(),name="portfolio-index"),
    #url(r'^(?P<pk>[0-9]+)/$', views.DetailView.as_view(), name='portfolio-detail'),
]
