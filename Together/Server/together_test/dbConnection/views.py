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
                login_success = 2
            elif UserPW == pw_datas[0]:
                print('id and pw are same')
                login_success = 1
            else:
                print('pw is not same')
                login_success = 2

    finally: 
        con.close()

    if login_success == 1:
        print('login success')
        return HttpResponse(1)
    
    else:
        print('login fail')
        if login_success == 2:
            print('pw is not same')
            return HttpResponse(2)
        else:
            print('cannot find id')
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
        json_data = json.dumps(datas, default = json_datetime, ensure_ascii=False)
    
    finally:
        con.close()

    if datas is None:
        print('there is no valid data in database')
        return HttpResponse(0)
    elif len(datas) == 0:
        print('there is no valid data in database')
        return HttpResponse(0)
    else:
        return HttpResponse(json_data)

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
        sql ='select PNum from posting where PID = %s and PAbout = %s'
        curs.execute(sql, (user_id, user_write_content))
        post_data = curs.fetchone()
        sql = 'insert into joingroup values (%s, %s)'
        curs.execute(sql, (int(post_data[0]),user_id))
        con.commit()
        errornum = 2
    
    except con.Error as error :
        con.rollback()
        print(error)
        print('error inserting record into mysql')
        errornum = 0

    finally:
        con.close()

    if errornum ==2:
        print('posting insertion is complete !')
        return HttpResponse(1)
    else:
        print('posting insertion is fail ;0;')
        return HttpResponse(0)

#선택된 게시글에 대한 정보 출력
@csrf_exempt
def select_post(request):
    select_post = request.POST.get('SelectPost', '') 
    now = timezone.localtime(timezone.now()).strftime('%Y-%m-%d-%H:%M:%S')

    try:
        con =pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor(pymysql.cursors.DictCursor)
        sql = 'select * from posting where Pnum = %s and PValidtime > %s'
        curs.execute(sql,(int(select_post),now))

        datas = curs.fetchone()
        json_data = json.dumps(datas, default = json_datetime, ensure_ascii=False)
        '''print(type(datas))
        print(datas)
        print(len(datas))
        print(type(json_data))
        print(json_data)'''
        
    except con.Error as error:
        con.rollback()
        print('Mysql has an error :')
        print(error)
    
    finally:
        con.close()
    
    if datas is None:
        print('There is no item in database')
        return HttpResponse(0)

    elif len(datas) == 0:
        print('There is no item in database')
        return HttpResponse(0)

    else:
        return HttpResponse(json_data)

#작성한 게시글 중 유효시간이 남아있는 게시글을 삭제
@csrf_exempt
def delete_post(request):
    now = timezone.localtime(timezone.now()).strftime('%Y-%m-%d-%H:%M:%S')
    user_id = request.POST.get('userid', '')
    errornum = 0
    try:
        con =pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql = 'delete from posting where PID = %s and Pvalidtime > %s'
        curs.execute(sql,(user_id,now))
        print(curs.rowcount)
        if curs.rowcount == 1:
            errornum =1
        con.commit()

    except con.Error as error:
        con.rollback()
        print('delete error in mysql')
        print(error)

    finally:
        con.close()

    if errornum == 1:
        print('delete success !')
        return HttpResponse(1)
    else:
        print('delete fail ;.;')
        return HttpResponse(0)

#작성한 댓글을 데이터베이스에 저장
@csrf_exempt
def write_comment(request):
    comment_posting_num = request.POST.get('postNum', '')
    comment_id = request.POST.get('commentID', '')
    comment_about = request.POST.get('commentAbout', '')
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
    commented_num = request.POST.get('commentNum', '')
    comment_id = request.POST.get('commentID', '')
    comment_time = request.POST.get('commentTime', '')
    errornum = 0

    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql ='delete from comment where CID = %s and CNum = %s and CTime = %s'
        curs.execute(sql, (comment_id, int(commented_num), comment_time))
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
    commented_num = request.POST.get('commentNum', '')

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

#1번을 넣어오면 그룹에 참여한 인원수, 2번을 넣어오면 그룹에 포함되어 있는 인원 출력
@csrf_exempt
def count_join_member(request):

    join_group = request.POST.get('PNum', '')
    select_num = request.POST.get('SelectNum', '')
    sql=''

    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        if select_num == 1:
            curs = con.cursor()
            sql = 'select COUNT(JNum) from joingroup where JNum = %s'
        elif select_num == 2:
            curs = con.cursor(pymysql.cursors.DictCursor)
            sql = 'select JID from joingroup where JNum = %s'
        else:
            print('error in sql select')
            return HttpResponse(0)
        
        curs.execute(sql, int(join_group))
        datas = curs.fetchall()

    except con.Error as error:
        print(error)

    finally:
        con.close()

    if len(datas) == 0 :
        print('cannot find datas in joingroup')
        return HttpResponse(0)
    else:
        if select_num == 1: #반환이 tuple 이다.
            print('select in joingroup is success')
            datas = list(datas)
            print(type(datas[0]))
            return HttpResponse(datas[0])

        else:
            print('select in joingroup is success')
            return HttpResponse(datas)

#사용자가 선택한 그룹에 참여하는 경우
@csrf_exempt
def join_group(request):
    post_number = request.POST.get('PNum', '')
    user_id = request.POST.get('UID', '')
    now = timezone.localtime(timezone.now()).strftime('%Y-%m-%d-%H:%M:%S')
    errornum = 0

    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql = 'select PNum from posting where PNum = %s and PValidtime > %s'
        curs.execute(sql,(int(post_number), now))
        post_data = curs.fetchone()

        if post_data is None:
            print('This post is not available that you clicked to participate')
            return HttpResponse(4) # 참여하고자 한 게시글이 유효시간을 벗어난경우
        else:
            sql = 'select JID from joingroup where JNum in (select PNum from posting where PValidtime > %s) and JID = %s'
            curs.execute(sql, (now, user_id))
            join_data = curs.fetchone()

            if join_data is not None:
                print('You are already existed another group ')
                return HttpResponse(3) # 다른 게시글에 이미 참여가 되어 있는경우
            else:
                sql = 'select COUNT(JNum) from joingroup where JNum = %s'
                curs.execute(sql, (int(post_number)))
                count_num = curs.fetchone()
                sql = 'select PLimit from posting where PNum = %s'
                curs.execute(sql, (int(post_number)))
                limit_num = curs.fetchone()

                if count_num[0] >= limit_num[0] :
                    print('A group that you chosen is full')
                    return HttpResponse(2) # 선택한 그룹이 가득 찬 경우
                else:
                    sql = 'insert into joingroup values (%s, %s)'
                    curs.execute(sql, (int(post_number), user_id))
                    con.commit()
                    errornum =1

    except con.Error as error :
        con.rollback()
        print('error in sql insert')
        print(error)

    finally:
        con.close()

    if errornum ==1 :
        print('insert success')
        return HttpResponse(1)
    else:
        print('insert fail')
        return HttpResponse(0)

#참여햇던 그룹에서 나오는 경우
@csrf_exempt
def leave_group(request):
    selected_group = 5
    selected_ID = 'kikikim'

    try:
        con = pymysql.connect(host='localhost', user='gohomie', password='qwerty', db='together_database', charset='utf8')
        curs = con.cursor()
        sql = 'delete from joingroup where JNum = %s and JID = %s'
        curs.execute(sql, (int(selected_group), selected_ID))
        con.commit()
    
    except con.Error as error:
        con.rollback()
        print('There is an error during delete trasection')
        print(error)
    
    finally:
        con.close()
    
    if curs.rowcount == 1:
        print('delete success !')
        return HttpResponse(1)
    else:
        print('delete fail')
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


    






