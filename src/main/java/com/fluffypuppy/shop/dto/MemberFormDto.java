package com.fluffypuppy.shop.dto;

import com.fluffypuppy.shop.constant.Provider;
import com.fluffypuppy.shop.constant.Role;
import com.fluffypuppy.shop.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

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
    @Length(min = 8, max = 16, message = "비밀번호는 8~16자 사이로 입력해주세요.")
    private String password;
    private String confirmPassword;

    @NotEmpty(message = "주소는 필수 항목입니다.")
    private String address1;
    private String address2;

    @NotEmpty(message = "전화번호는 필수 항목입니다.")
    private String phoneNumber;

    private Role role;

    // SNS 로그인을 통해 들어온 유저를 식별하기 위한 필드
    private String picture;
    private Provider provider;

    private static ModelMapper modelMapper = new ModelMapper();

    // 엔티티 -> DTO 변환 메서드
    public static MemberFormDto of(Member member){
        MemberFormDto dto = modelMapper.map(member, MemberFormDto.class);
        // SNS 유저인 경우 비밀번호 필드를 비워서 보안 유지 및 에러 방지
        if(member.getProvider() != Provider.LOCAL) {
            dto.setPassword(null);
        }
        return dto;
    }

}
