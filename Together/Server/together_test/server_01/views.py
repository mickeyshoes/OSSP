from django.shortcuts import render
from django.http import HttpResponse

def index(request):
    return HttpResponse("Hello, django test01")
# Create your views here.
