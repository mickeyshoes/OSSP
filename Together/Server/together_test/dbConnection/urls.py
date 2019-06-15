from django.urls import path
from . import views

urlpatterns = [
    path('dictest', views.test_dictonary_ver),
    path('reqtest', views.request_test),
    path('app_login', views.app_login), # 로그인
    path('find_id', views.find_id), # 아이디 찾기
    path('find_pw', views.find_pw), # 비밀번호 찾기
    path('add_user', views.add_user), # 회원 등록
    path('check_id', views.check_id), # 아이디 중복 확인
    path('write_post', views.write_post), # 게시글 작성
    path('existed_post', views.existed_post), # 유효시간이 남은 게시글들
    path('test_connect', views.test_connect),
    path('select_post', views.select_post), # 게시글 선택
    path('write_comment', views.write_comment), # 댓글 작성
    path('delete_comment',views.delete_comment), # 댓글 삭제
    path('existed_comment', views.existed_comment), # 특정 게시글에 작성된 댓글
    path('delete_post', views.delete_post), # 게시글 삭제
    path('count_join_member', views.count_join_member), #게시글(그룹)에 참여한 인원 수 및 참여한 회원 확인
    path('join_group', views.join_group), # 선택한 그룹에 참여하는 경우
    path('leave_group', views.leave_group), # 참여했던 그룹에서 나오는 경우
    path('find_postnum', views.find_postnum),
]