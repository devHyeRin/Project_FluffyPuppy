package com.fluffypuppy.shop.entity;

import com.fluffypuppy.shop.constant.Provider;
import com.fluffypuppy.shop.constant.Role;
import com.fluffypuppy.shop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Member extends BaseEntity{
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;    // SNS 유저는 null 허용

    private String address1;
    private String address2;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String picture;       // SNS 로그인을 위한 필드

    @Enumerated(EnumType.STRING)
    private Provider provider;   // LOCAL, GOOGLE, KAKAO, NAVER

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress1(memberFormDto.getAddress1());
        member.setAddress2(memberFormDto.getAddress2());
        member.setPassword(passwordEncoder.encode(memberFormDto.getPassword()));
        member.setPhoneNumber(memberFormDto.getPhoneNumber());
        member.setRole(Role.USER);
        member.setProvider(Provider.LOCAL);
        return member;
    }


    public void updateMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        this.name = memberFormDto.getName();

        // 로컬 유저(비밀번호가 들어오는 경우)만 비밀번호 업데이트
        if(memberFormDto.getPassword() != null && !memberFormDto.getPassword().isEmpty()){
            this.password = passwordEncoder.encode(memberFormDto.getPassword());
        }

        this.address1 = memberFormDto.getAddress1();
        this.address2 = memberFormDto.getAddress2();
        this.phoneNumber = memberFormDto.getPhoneNumber();
    }

    // SNS 정보 업데이트 메서드
    public Member update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

}
