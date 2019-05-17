from django.urls import path
from . import views

urlpatterns = [
    path('dbtest', views.dbtest),
    path('idtest', views.login),
    path('dictest', views.test_dictonary_ver),
]