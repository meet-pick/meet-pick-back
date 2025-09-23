package org.jpetto.meetpickback.global.loginUser;

import lombok.RequiredArgsConstructor;
import org.jpetto.meetpickback.account.account.entity.Account;
import org.jpetto.meetpickback.account.account.repository.AccountRepository;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
/* HandlerMethodArgumentResolver -> 컨트롤러 메서드의 파라미터를 자동으로 해석하고 값을 주입하는 인터페이스*/
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final AccountRepository accountRepository;

    // 이 리졸버가 해당 파라미터를 처리할 수 있는지 판단함
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 로그인 어노테이션이 달려있고 Account 클래스일 것 -> 그 외로는 리졸버에서 파라미터를 처리하면 안됨 (false)
        return parameter.hasParameterAnnotation(LoginUser.class) && parameter.getParameterType().equals(Account.class);
    }

    // 실제로 파라미터 값을 생성/반환
    @Override
    public Object resolveArgument(
            MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        // 스프링 시큐리티에서 인증정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 객체가 없거나 인증 되지않은 객체인 경우 null 반환
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetails userDetails)) {
            return null;
        }

        String username = userDetails.getUsername();

        return accountRepository.findByUsername(username).orElse(null);
    }
}
