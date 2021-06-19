# Netty sample project

## 구현 내용
- Netty TCP Server 구현   : netty-server
- Netty TCP Client 구현   : netty-client
- Spring REST Client 구현 : spring-rest-client

## 해결하고자 했던 문제
### Case 1. Netty Client - Netty Server 간 통신
- Netty Client와 Server 간 비동기 통신을 직접 구현
```
               요청(Asynchronous)
               ---------------->
Netty Client                       Netty Server
               <----------------
               응답(Asynchronous)
```

### Case 2. Spring Client - Netty Server 간 통신

- Spring REST Controller가 사용자로부터 요청을 받아, Netty-Server와 비동기 통신을 한 후 다시 사용자에게 동기적으로 응답하는 과정을 구현
```
       요청(Synchronous)                        요청(Asynchronous)
       ---------------->                         ---------------->  
사용자                   Spring REST Controller                    Netty Server
       <----------------                        <----------------       
       응답(Synchronous)                        응답(Asynchronous)
```

## 참고자료
- https://netty.io/wiki/user-guide-for-4.x.html
- http://tutorials.jenkov.com/netty/installation.html