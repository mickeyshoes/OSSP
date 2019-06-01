from django.urls import path
from . import views

urlpatterns = [
    path('dictest', views.test_dictonary_ver),
    path('reqtest', views.request_test),
    path('app_login', views.app_login),
    path('find_id', views.find_id),
    path('find_pw', views.find_pw),
    path('add_user', views.add_user),
    path('check_id', views.check_id),
    path('write_post', views.write_post),
    path('existed_post', views.existed_post),
    path('test_connect', views.test_connect),
]