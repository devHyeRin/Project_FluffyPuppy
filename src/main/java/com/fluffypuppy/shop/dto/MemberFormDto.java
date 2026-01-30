package com.fluffypuppy.shop.dto;

import com.fluffypuppy.shop.constant.Role;
import com.fluffypuppy.shop.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MemberFormDto {

    private Long id;

    @NotBlank(message = "이름은 필수 항목입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 항목입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 영문자, 특수문자 포함 16자 이내로 입력해주세요.")
    private String password;
    private String confirmPassword;

    @NotEmpty(message = "주소는 필수 항목입니다.")
    private String address1;
    private String address2;

    @NotEmpty(message = "전화번호는 필수 항목입니다.")
    private String phoneNumber;

    private Role role;

    public Role getRole(){
        return role;
    }

    public void setRole(Role role){
        this.role = role;
    }

    private static ModelMapper modelMapper = new ModelMapper();
    public static MemberFormDto of(Member member){
        return modelMapper.map(member, MemberFormDto.class);
    }

}
