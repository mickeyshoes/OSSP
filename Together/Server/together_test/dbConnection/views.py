from django.shortcuts import render
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from django.utils import timezone
import pymysql
import mysql.connector
from mysql.connector import Error
from mysql.connector import errorcode
import json
import datetime

# Create your views here.

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

#입력받은 정보로 로그인을 시도한다
@csrf_exempt
def app_login(request):
    UserID = request.POST.get('UserID', '')
    UserPW = request.POST.get('UserPW', '')
    login_success = 0 #error가 생기면 0을 리턴

    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql = 'select UID from userinfo where UID =%s'
        curs.execute(sql,UserID)
        id_data = curs.fetchone()
        
        if id_data is None:
            print('cannot find id')
            login_success =0
        
        else:
            print('id is in database')
            sql = 'select UPw from userinfo where Uid =%s'
            curs.execute(sql,UserID)
            pw_datas = curs.fetchone()

            if pw_datas is None:
                print('pw is not same')
                login_success = 0
            elif UserPW == pw_datas[0]:
                print('id and pw are same')
                login_success = 1
            else:
                print('pw is not same')
                login_success = 0

    finally: 
        con.close()

    if login_success == 1:
        print('login success')
        return HttpResponse(1)
    
    else:
        print('login fail')
        return HttpResponse(0)

#입력받은 정보를 통해 등록된 ID를 조회한다.
@csrf_exempt
def find_id(request):
    userName = '김덕배'
    userTel = '002-4435-3322'
    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql = 'select UID from userinfo where UName =%s and UTel =%s'
        curs.execute(sql,(userName, userTel))

        datas = curs.fetchone()
        print_datas = datas

    finally: 
        con.close()

    if print_datas == None:
        print('cannot find id')
        return HttpResponse(0)
    else:
        return HttpResponse(print_datas[0])

#입력받은 정보를 통해 해당 ID의 비밀번호를 조회한다.
@csrf_exempt
def find_pw(request):
    userid = "helloworld"
    usertel = "002-4435-3322"
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

#입력받은 사용자의 개인정보를 데이터베이스에 저장
@csrf_exempt
def add_user(request):
    input_uid = 'and_phy'
    input_upw = '1357'
    input_uname = '김구글'
    input_utel ='041-224-2234'
    errornum = 0 #error가 생기면 값을 0으로 바꾸고 0을 리턴

    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql = 'insert into userinfo values (%s, %s, %s, %s)'
        curs.execute(sql,(input_uid, input_upw, input_uname, input_utel))
        con.commit()
        errornum = 1

    except con.Error as error :
        con.rollback()
        print('error inserting record into mysql')
        print(error)
        errornum = 0
        
    finally: 
        con.close()

    if(errornum ==1):
        return HttpResponse(1)
    else:
        return HttpResponse(0)

#입력받은 사용자의 아이디가 데이터베이스에 이미 등록되어 있는지 확인
@csrf_exempt
def check_id(request):
    inputID = 'helloworld1'

    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql = 'select UID from userinfo where UID = %s'
        curs.execute(sql,inputID)

        datas = curs.fetchone()
        print_datas = datas
    finally:
        con.close()
    
    if print_datas is None:
        print('cannot find duplicated id')
        return HttpResponse(1)
    else:
        print('already existed')
        return HttpResponse(0)

#작성된 게시글들 중에서 유효한 게시글들만 가져옴(dictonary type)
@csrf_exempt
def existed_post(request):
    now = timezone.localtime(timezone.now()).strftime('%Y-%m-%d-%H:%M:%S')

    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor(pymysql.cursors.DictCursor)
        sql ='select * from posting where PValidtime > %s'
        curs.execute(sql, now)
        datas = curs.fetchall()
    
    finally:
        con.close()

    if datas is None:
        print('there is no valid data in database')
        return HttpResponse(0)
    else:
        return HttpResponse(datas)

#데이터베이스에 사용자가 입력한 게시글 삽입
@csrf_exempt
def write_post(request):
    user_id = 'helloworld'
    user_write_content = '뭘 이렇게 많이 적었누'
    user_posttime = timezone.localtime(timezone.now()).strftime('%Y-%m-%d-%H:%M:%S')
    user_validtime = timezone.localtime(timezone.now() + timezone.timedelta(hours=1)).strftime('%Y-%m-%d-%H-%M-%S')
    user_departure = '아산역'
    user_arrival = '천안역'
    user_limit = 4
    errornum = 0

    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql = 'insert into posting(PID, PAbout, PPosttime, PValidtime, PDeparture, PArrival, PLimit) values (%s,%s,%s,%s,%s,%s,%s)'
        curs.execute(sql, (user_id, user_write_content, user_posttime, user_validtime, user_departure, user_arrival, int(user_limit)))
        con.commit()
        errornum = 1
    
    except con.Error as error :
        con.rollback()
        print(error)
        print('error inserting record into mysql')
        errornum = 0

    finally:
        con.close()

    if(errornum ==1):
        print('posting insertion is complete !')
        return HttpResponse(1)
    else:
        print('posting insertion is fail ;0;')
        return HttpResponse(0)


def select_post(request):
    select_post = 4

    try:
        con =pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor(pymysql.cursors.DictCursor)
        sql = 'select * from posting where Pnum = %s'
        curs.execute(sql,int(select_post))

        datas = curs.fetchall()
        json_data = json.dumps(datas, default = json_datetime, ensure_ascii=False)
        print(type(datas))
        print(datas)
        print(len(datas))
        print(type(json_data))
        print(json_data)
        
    
    except con.Error as error:
        con.rollback()
        print('Mysql has an error :')
        print(error)
    
    finally:
        con.close()
    
    if len(datas) == 0 :
        print('There is no item in database')
        return HttpResponse(0)

    else:
        return HttpResponse(json_data)

@csrf_exempt
def test_connect(request):
    datas = json.loads(request.body)
    keys = list(datas.keys())
    print(datas)
    print(type(keys))
    print(datas.get(keys[0]))
    return HttpResponse(str(datas))

#데이터베이스에서 가져온 datetime type을 json타입으로 변환    
def json_datetime(value):
    if isinstance(value,datetime.datetime):
        return value.strftime('%Y-%m-%d-%H:%M:%S')


    






