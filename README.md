Android Studio version 8.0

자세한 코드 설명은 주석으로 모두 설명하였습니다.

Together 어플은 선문대학교 학생들의 외부로 나갈 때의 불편함과 비용 부담을 줄이고자 만들어진 어플리케이션입니다.

실행 방법
어플리케이션 다운로드 및 안드로이드 스튜디오 ADK로 실행

Together 사용서
1. 회원가입 및 로그인
2. 게시글 목록 화면에서 자신에게 맞는 출발지와 목적지가 있는 게시글이 있다면 그 게시글을 클릭하여 접속.
3. 원하는 게시글이 없다면 맨 하단에 게시글 작성 버튼으로 게시글 작성.
4. 작성 시 메모, 출발지 목적지 제한인원 필수 작성, 미작성시 게시글 작성이 안됌.(메세지로 알려줌)
5. 게시글 참여 또는 작성 후 각 게시글마다 참여한 사용자들끼리 댓글을 통해 대화가능.
6. 게시글 작성자는 참여인원들이 다 모였을때 완료버튼 클릭, 클릭 시 게시글은 삭제됌.
7. 완료버튼 후 콜택시 번호, 콜밴 전화, 사다리 게임을 할 수 있는 버튼으로 자유롭게 이동수단 및 비용 나누기를 할 수 있음.

개발에 사용한 언어 및 툴
Android, Java, Xml, Python, Django, Mysql

Android Studio

1. Together_CommentItem - 댓글 ListView Item 클래스.
2. Together_CommentList_Adapter - 댓글 ListView Adapter 클래스.
3. Together_FindInfo - ID/Pw를 찾는 화면 클래스.
4. Together_Listview - 게시글 목록 클래스.
5. Together_Listview_Adapter - 게시글 목록 ListView Adapter 클래스.
6. Together_Listview_PostingItem - 게시글 목록 ListView Item 클래스.
7. Together_Login ㅡ 로그인 화면 클래스.
8. Together_MatchComplete ㅡ 게시글에 접속인원들이 다 모였을때 게시글 작성자가 택시, 콜밴, 사다리게임을 할 수 있는 버튼이 있는 클래스.
9. Together_Posting - 게시글을 작성하는 클래스.
10. Together_SelectPostingReader - 게시글에 참여하는 사용자들이 보는 게시글 클래스.
11. Together_SelectPostiongWriter - 작성자가 작성한 게시글을 보는 게시글 클래스.
12. Together_SignupMember - 회원가입 페이지.
13. TogetherMainActivity - 회원 가입 및 로그인 버튼이 있는 초기화면.
14. URL_make - 서버의 연결할 주소를 가지는 클래스.

서버 및 DB
manage.py가 있는 최상위 app에서 py manager.py runserver 0.0.0.0:포트번호

서버와 DB 연동
mysqlclient, pymysql 라이브러리 사용.
