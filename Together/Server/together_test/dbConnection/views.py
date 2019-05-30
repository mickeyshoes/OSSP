from django.shortcuts import render
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
import pymysql
import mysql.connector
from mysql.connector import Error
from mysql.connector import errorcode

# Create your views here.

# 데이터 하나만 가져오는 것 테스트
def dbtest(request):
    con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
    curs = con.cursor()
    sql = 'select * from userinfo'
    curs.execute(sql)

    datas = curs.fetchall()
    print_datas = datas
    con.close()
    return HttpResponse(print_datas)

def login(request):
    con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
    curs = con.cursor()
    sql ='select * from userinfo where UID =%s'
    curs.execute(sql,('test_admin'))

    datas = curs.fetchone()
    print_datas = datas
    con.close()
    return HttpResponse(print_datas)

def admit_user(request):
    con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
    curs = con.cursor()
    sql ='insert into userinfo values(%s,%s,%s,%s)'
    curs.execute(sql,'and_phy','1357','김구글','041-224-2234')

    datas = curs.fetchall()
    return 0

def test_dictonary_ver(request):
    con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
    curs = con.cursor(pymysql.cursors.DictCursor)
    sql = 'select * from userinfo'
    curs.execute(sql)

    datas = curs.fetchall()
    print_dic = datas
    con.close()
    return HttpResponse(print_dic)

def request_test(request):
    
    request_int = request.POST.get('request_int', '')
    request_int = request_int+ request_int
    return HttpResponse(request_int)

def app_login(request):
    userID = "milkis"
    UserPW = "12354"
    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql = 'select UPw from userinfo where UID =%s'
        curs.execute(sql,userID)

        datas = curs.fetchone()
        print_datas= datas
        print(print_datas[0])

    finally: 
        con.close()

    if(UserPW == print_datas[0]):
        print('login success')
        return HttpResponse(1)
    
    else:
        print('login fail')
        return HttpResponse(0)

def find_id(request):
    userName = '롯데'
    userTel = '080-730-1472'
    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql = 'select UID from userinfo where UName =%s and UTel =%s'
        curs.execute(sql,(userName, userTel))

        datas = curs.fetchone()
        print_datas = datas

    finally: 
        con.close()

    if(print_datas == None):
        print('cannot find id')
        return HttpResponse(0)
    else:
        return HttpResponse(print_datas[0])

def find_pw(request):
    userid = "milkis"
    usertel = "080-730-1472"
    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql = 'select UPw from userinfo where UID =%s and UTel =%s'
        curs.execute(sql,(userid, usertel))

        datas = curs.fetchone()
        print_datas = datas
    finally: 
        con.close()

    if(print_datas == None):
        print('cannot find pw')
        return HttpResponse(0)
    else:
        return HttpResponse(print_datas[0])

def add_user(request):
    UID ='scrawbars'
    UPw ='0948752'
    UName = '박죠스'
    UTel ='010-7788-0094'
    errornum = 1 #error가 생기면 값을 0으로 바꾸고 0을 리턴

    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql = 'insert into userinfo values (%s, %s, %s, %s)'
        curs.execute(sql,(UID, UPw, UName, UTel))
        con.commit()

    except con.Error as error :
        con.rollback()
        print('error inserting record into mysql')
        errornum = 0
        
    finally: 
        con.close()

    if(errornum !=1):
        return HttpResponse(0)
    else:
        return HttpResponse(1)

def check_id(request):
    inputID = 'helloworld'

    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql = 'select UID from userinfo where UID = %s'
        curs.execute(sql,inputID)

        datas = curs.fetchone()
        print_datas = datas
    finally:
        con.close()
    
    if(print_datas == None):
        print('cannot find duplicated id')
        return HttpResponse(1)
    else:
        print('already existed')
        return HttpResponse(0)

#def change_pw(request):

    






