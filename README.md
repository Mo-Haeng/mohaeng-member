

# mohang-member
```
- 회원 가입, 회원 탈퇴, 회원정보 수정 등
- 로그인, 로그아웃등의 기능 제공
```

카카오 OAuth2 로그인 기능을 추가하였습니다.

해당 과정에서 CORS 오류가 발생하여 어려움을 겪었는데, front에서 get 요청을 ajax 등으로 보내는 방식이 아닌 `a href = "OAuth2 로그인 주소"`처럼
`href` 태그를 활용하였더니 잘 동작하였습니다.

또한 `AuthorizationRequestRepository` 구현체를 통해 `redirect url`을 저장하여, 인증에 성공할 경우 AuthToken을 `redirect url`의 파라미터로 추가하여 넘기는 방식을 통해 인증 성공을 알리도록 하였습니다.

백엔드에서 모든 과정을 처리하기 위해서는 이 방법 이외는 떠올릴 수 없었고, 만약 프론트단에서 `authorization code`를 가져와 백엔드로 넘기는 경우라면 더 깔끔하게 코드를 작성할 수 있을 것 같았습니다.


</br></br>

스프링 시큐리티 설정에 코틀린 DSL을 사용하는 경우, MVC 환경이라면 `authorizeHttpRequests`에서 `antMatcher` 대신 `mvcMatcher`를 사용합니다

이때문에 `h2-console`에 접근이 불가능해지는 오류가 발생하였고, `AntPathRequestMatcher()`를 통해 감싸줌으로써 해결하였습니다.


</br></br>

`AbstractAuthenticationProcessingFilter`를 상속받아 로그인 처리를 진행하는 경우, 해당 과정에서 발생한 예외를 처리하기 위해서는 반드시 `AuthenticationException`를 상속받아야 합니다.

저는 `AuthException` 을 발생시키도록 처리하였는데, FailureHandler에서 처리되지 않아 조금 헤맸었습니다.


</br></br></br>



