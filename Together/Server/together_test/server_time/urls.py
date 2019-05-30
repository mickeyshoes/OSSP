from django.urls import path

from . import views

urlpatterns = [
    path('print_time', views.index),
    path('insert_time',views.insert_time),
    path('insert_time_1', views.insert_time_plus_1),
    path('pick_time', views.pick_time_remain),
    ]