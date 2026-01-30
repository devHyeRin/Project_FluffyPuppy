package com.fluffypuppy.shop.service;

import com.fluffypuppy.shop.dto.MemberFormDto;
import com.fluffypuppy.shop.entity.Member;
import com.fluffypuppy.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberUpdateService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long updateMember(MemberFormDto memberFormDto) throws EntityNotFoundException {
        Member member = memberRepository.findById(memberFormDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));

        member.updateMember(memberFormDto, passwordEncoder);

        return member.getId();
    }
}
