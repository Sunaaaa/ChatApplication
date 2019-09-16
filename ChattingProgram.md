# Multi Chatting Room

- 다수의 클라이언트가 다수의 채팅방을 이용하여 채팅한다. 
<br>
### Java Eclipse
- Server
  - Eclipse
    - ChatServer
    - RoomManager
    - Room
    - ChatUser

<br>
### Android studio
- Client
  - Android Studio
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
- Service
