# Multi Chatting Room

- 다수의 클라이언트가 다수의 채팅방을 이용하여 채팅한다. 
- 아이디와 비밀번호로 로그인한 사용자는 대기방으로 입장
  - 원하는 채팅방을 선택 또는 새로운 채팅 방을 생성하여 채팅 방에 입장
  - 사용자는 3가지 대화 기능 사용
    - 첫번째는 어플리케이션 내의 모든 사용자에게 메시지를 전달
    - 두번째는 현재 채팅 방 내의 사용자에게만 메시지를 전달
    - 세번째는 특정 사용자에게만 메시지를 전달 ( 귓속말 )
  - 사용자는 채팅방을 나있습니다. 
- 서버에서는 Thread Pool을 활용하여 다중 클라이언트 기능을 제공합니다.



<br>

### Java Eclipse

- Server
  - ChatServer
  - RoomManager
  - Room
  - ChatUser

<br>

### Android studio

- Client
  - MainActivity 
    - 사용자는 아이디와 비밀번호를 입력하여 입장한다.
    - 사용자가 입력한 아이디가 채팅 사용자의 이름이 된다. 
  - WaitingRoomActivity
    - 다수의 클라이언트가 새로운 방을 생성할 수 있다. 
    - 클라이언트는 원하는 채팅방을 선택하여 입장할 수 있다 .
  - ClientActivity
    - 채팅방에 입장한 클라이언트끼리 채팅한다.
  - RoomManager
  - Room
    - 각각의 채팅방의 정보를 갖는다.
  - ChatUser
    - 채팅방 혹은 대기방에 있는 클라이언트의 정보를 갖는다. 
      <br>

### Android studio

- Service
  - Server와의 통신을 위해 모든 액티비티에서 new Socket()의 남용을 피하기 위해, Service를 구현한다.
