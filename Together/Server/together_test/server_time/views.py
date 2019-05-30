from django.shortcuts import render
from django.http import HttpResponse
from django.utils import timezone
import pymysql
from mysql.connector import Error, errorcode


def index(request):
    nowp = timezone.localtime(timezone.now() + timezone.timedelta(hours=21)).strftime('%Y-%m-%d-%H-%M-%S')
    print(nowp)
    now = timezone.localtime(timezone.now()).strftime('%Y-%m-%d-%H-%M-%S')
    print(type(now))
    nowpp = timezone.localtime(timezone.now())
    print(type(nowpp))
    return HttpResponse(now)

def insert_time(request):
    now = timezone.localtime(timezone.now()).strftime('%Y-%m-%d-%H:%M:%S')
    errornum =1

    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql = 'insert into time_test(time) values (%s)'
        curs.execute(sql,now)
        con.commit()

    except con.Error as error :
        con.rollback()
        print('insert in time has a error')
        errornum =0

    finally:
        con.close()

    if(errornum !=0):
        print('insert success')
        return HttpResponse(1)
    else:
        print('insert error')
        return HttpResponse(0)

def insert_time_plus_1(request) :
    nowp = timezone.localtime(timezone.now() + timezone.timedelta(hours=2)).strftime('%Y-%m-%d-%H-%M-%S')
    errornum =1

    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql = 'update time_test SET timeplus1 = (%s) where postnum =1'
        curs.execute(sql,nowp)
        con.commit()

    except con.Error as error :
        con.rollback()
        print('insert in time has a error')
        errornum =0

    finally:
        con.close()

    if(errornum !=0):
        print('insert success')
        return HttpResponse(1)
    else:
        print('insert error')
        return HttpResponse(0)
    

def pick_time_remain(request):
    now = timezone.localtime(timezone.now())
    errornum =1

    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql = 'select postnum from time_test where time < (%s)'
        curs.execute(sql,now)
        
        datas = curs.fetchall()
        print_datas = datas
        print(type(print_datas))

    except con.Error as error :
        con.rollback()
        print('compare time has a error')
        errornum =0

    finally:
        con.close()

    if(errornum !=0):
        print('compare success')
        return HttpResponse(print_datas)
    else:
        print('compare error')
        return HttpResponse(0)



