package org.zerock.b01.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Member;
import org.zerock.b01.dto.MemberSecurityDTO;
import org.zerock.b01.repository.MemberRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    // 스프링 시큐리티 객체의 실제로 인증을 처리하는 UserDetailsService 인터페이스의 구현체가 있다
    // 이 구현체를 내맘대로 Custom 처리하는 클래스
    // UserDetailsService는 loadUserByUsername() 이라는 메서드를 하나 가지고 있음
    // 실제 인증을 처리할 때 호출되는 부분임

    // 필드
    private final PasswordEncoder passwordEncoder;
    private  final MemberRepository memberRepository;
    
   /* // 기본생성자
    public CustomUserDetailService(){
        this.passwordEncoder = new BCryptPasswordEncoder();
    } // 현재 클래스가 동작시에 기본적으로 암호처리 객체를 생성*/
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        // 로그인 시 id를 담당하는 메서드
        // 리턴 객체는 UserDetails라는 객체임 -> 인증과 관련된 정보를 저장하는 역할!!!!!!

        log.info("loadUserByUsername : " + username);

        Optional<Member> result = memberRepository.getWithRoles(username);

        if(result.isEmpty()){
            throw new UsernameNotFoundException("해당 유저가 없습니다...");
        }

        Member member = result.get();

        MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                member.getMid(),
                member.getMpw(),
                member.getEmail(),
                member.isDel(),
                false,
                member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name())).collect(Collectors.toList())
                );

        log.info("memberSecurityDTO : " + memberSecurityDTO);
        return memberSecurityDTO;
    }
}
