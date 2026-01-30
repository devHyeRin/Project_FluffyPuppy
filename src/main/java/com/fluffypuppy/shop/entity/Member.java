package com.fluffypuppy.shop.entity;

import com.fluffypuppy.shop.constant.Provider;
import com.fluffypuppy.shop.constant.Role;
import com.fluffypuppy.shop.dto.ItemFormDto;
import com.fluffypuppy.shop.dto.MemberFormDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Data
public class Member extends BaseEntity{
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address1;
    private String address2;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress1(memberFormDto.getAddress1());
        member.setAddress2(memberFormDto.getAddress2());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setPhoneNumber(memberFormDto.getPhoneNumber());
        member.setRole(memberFormDto.getRole());
        member.setProvider(Provider.LOCAL);
        return member;
    }

    public void updateMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncode){
        this.name = memberFormDto.getName();
        this.password =passwordEncode.encode(memberFormDto.getPassword());
        this.address1 = memberFormDto.getAddress1();
        this.address2 = memberFormDto.getAddress2();
        this.phoneNumber = memberFormDto.getPhoneNumber();
    }

}
