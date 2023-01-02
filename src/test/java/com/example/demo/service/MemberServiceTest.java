package com.example.demo.service;


import com.example.demo.dto.MemberFormDto;
import com.example.demo.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest(){
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);

        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getAddress(), savedMember.getAddress());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getRole(), savedMember.getRole());
    }

    @Test
    @DisplayName("중복 회원가입 테스트")
    public void saveDuplicateMemberTest(){
        Member member1 = createMember(); // 같은 정보가 입력됨
        Member member2 = createMember(); // 같은 정보가 입력됨
        memberService.saveMember(member1);

        // saveMember시 발생하는 예외의 타입을 첫번째 인자에 넣어준다.
        // assertThrows로 발생하는 예외를 모든 예외의 조상 클래스인 Throwble 타입으로 반환
        Throwable e = assertThrows(IllegalStateException.class,()->{
            memberService.saveMember(member2);
        });

        // Throwble 타입의 예외 메시지와 같은지 검사
        // 첫번째 객체와 두번째 객체가 같으면 테스트 통과
        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }
}
