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
    UserName = request.POST.get('UserName', '')
    UserTel = request.POST.get('UserTel', '')
    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql = 'select UID from userinfo where UName =%s and UTel =%s'
        curs.execute(sql,(UserName, UserTel))

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
    UserID = request.POST.get('UserID', '')
    UserTel = request.POST.get('UserTel', '')
    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql = 'select UPw from userinfo where UID =%s and UTel =%s'
        curs.execute(sql,(UserID, UserTel))

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
    input_uid = request.POST.get('UserID', '')
    input_upw = request.POST.get('UserPW', '')
    input_uname = request.POST.get('UserName', '')
    input_utel = request.POST.get('UserTel', '')
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
    inputID = request.POST.get('UserID', '')

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
    user_id = request.POST.get('UserID', '')
    user_write_content = request.POST.get('UserWrite', '')
    user_posttime = timezone.localtime(timezone.now()).strftime('%Y-%m-%d-%H:%M:%S')
    user_validtime = timezone.localtime(timezone.now() + timezone.timedelta(hours=1)).strftime('%Y-%m-%d-%H-%M-%S')
    user_departure = request.POST.get('UserDeparture', '')
    user_arrival = request.POST.get('UserArrival', '')
    user_limit = request.POST.get('UserLimit', '')
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

    if errornum ==1:
        print('posting insertion is complete !')
        return HttpResponse(1)
    else:
        print('posting insertion is fail ;0;')
        return HttpResponse(0)

#선택된 게시글에 대한 정보 출력
@csrf_exempt
def select_post(request):
    select_post = request.POST.get('SelectPost', '')

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

#작성한 댓글을 데이터베이스에 저장
@csrf_exempt
def write_comment(request):
    comment_posting_num = 1
    comment_id = 'helloworld'
    comment_about = '지금 바로 가시나요??'
    now = timezone.localtime(timezone.now()).strftime('%Y-%m-%d-%H:%M:%S')
    errornum = 0

    try:
        con =pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql = 'insert into comment values (%s,%s,%s,%s)'
        curs.execute(sql,(comment_posting_num,comment_id,now,comment_about))
        con.commit()
        errornum = 1

    except con.Error as error :
        con.rollback()
        print('insert error in comment table')
        print(error)

    finally:
        con.close()

    if errornum == 1 :
        print('commit complete !')
        return HttpResponse(1)
    else:
        print('commit fail in comment table')
        return HttpResponse(0)    

#작성했던 댓글을 삭제한다.
@csrf_exempt
def delete_comment(request):
    commented_num = 1
    comment_id = 'helloworld'
    comment_time = '2019-06-06-00:02:16'
    print_time = datetime.datetime.strptime(comment_time, '%Y-%m-%d-%H:%M:%S')
    errornum = 0

    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql ='delete from comment where CID = %s and CNum = %s and CTime = %s'
        curs.execute(sql, (comment_id, int(commented_num), print_time))
        if curs.rowcount == 1:
            errornum =1
        con.commit()
        
    except con.Error as error:
        con.rollback()
        print('tuples cannot delete')
        print(error)
    
    finally:
        con.close()

    if errornum == 1:
        print('delete transection is success')
        return HttpResponse(1)
    else:
        print('delete transection is fail')
        return HttpResponse(0)

#해당 게시글에 작성된 댓글을 가져온다.
@csrf_exempt
def existed_comment(request):
    commented_num = 1

    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor(pymysql.cursors.DictCursor)
        sql ='select * from comment where CNum = %s'
        curs.execute(sql,(commented_num))
        datas = curs.fetchall()
        
    finally:
        con.close()

    if len(datas) != 0:
        print('select datas are success!')
        json_datas = json.dumps(datas, default= json_datetime, ensure_ascii=False)
        return HttpResponse(json_datas)
    else:
        print('there are none datas in your input conditions')
        return HttpResponse(0)

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


    






