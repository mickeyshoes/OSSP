from django.urls import path
from . import views

urlpatterns = [
    path('dbtest', views.dbtest),
    path('idtest', views.login),
    path('dictest', views.test_dictonary_ver),
    path('reqtest', views.request_test),
    path('app_login', views.app_login),
    path('find_id', views.find_id),
    path('find_pw', views.find_pw),
    path('add_user', views.add_user),
    path('check_id', views.check_id),
]