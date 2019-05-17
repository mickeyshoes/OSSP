from django.shortcuts import render
from django.http import HttpResponse
import pymysql

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
    


