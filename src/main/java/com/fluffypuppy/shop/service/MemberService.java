package com.fluffypuppy.shop.service;

import com.fluffypuppy.shop.dto.MemberFormDto;
import com.fluffypuppy.shop.entity.Member;
import com.fluffypuppy.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /*회원 저장*/
    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);

    }
    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 가입된 회원입니다.");
                });
    }

    /*아이디 중복확인*/
    public boolean isUsernameAvailable(String email) {
        return memberRepository.findByEmail(email).isEmpty();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword() != null ? member.getPassword() : "") // SNS 유저는 비번이 없으므로 빈값 처리
                .authorities(member.getRole().getKey()) // "ROLE_USER" 또는 "ROLE_ADMIN" 전달
                .build();
    }
    
    /*회원정보 수정*/
    @Transactional(readOnly = true)
    public MemberFormDto getMemberDtlByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        return MemberFormDto.of(member);
    }

}
