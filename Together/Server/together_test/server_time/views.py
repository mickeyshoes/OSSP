from django.shortcuts import render
from django.http import HttpResponse
from django.utils import timezone

def index(request):
    now = timezone.localtime(timezone.now()).strftime('%Y-%m-%d-%H-%M-%S')
    return HttpResponse(now)

# Create your views here.
