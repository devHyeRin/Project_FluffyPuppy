package com.fluffypuppy.shop.controller;

import com.fluffypuppy.shop.dto.MemberFormDto;
import com.fluffypuppy.shop.entity.Member;
import com.fluffypuppy.shop.service.MailService;
import com.fluffypuppy.shop.service.MemberService;
import com.fluffypuppy.shop.service.MemberUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final MemberUpdateService memberUpdateService;
    private final MailService mailService;

    String confirm="";
    boolean confirmCheck = false;

    /*회원가입 페이지 이동*/
    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    /*회원가입 정보 저장*/
    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }
        if (memberFormDto.getPassword() != null && !memberFormDto.getPassword().equals(memberFormDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "비밀번호가 일치하지 않습니다.");
            return "member/memberForm";
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        }
        catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/members/login";
    }

    /*회원가입 아이디 중복체크*/
    @GetMapping("/checkUsername")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam("username") String username) {
        boolean isUsernameAvailable = memberService.isUsernameAvailable(username);

        Map<String, Boolean> response = new HashMap<>();
        response.put("result", isUsernameAvailable);

        return ResponseEntity.ok(response);
    }


    /*로그인, 로그아웃 맵핑*/
    @GetMapping(value = "/login")
    public String loginMember(){
        return "member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "member/memberLoginForm";
    }

    //인증메일 보내기
    @PostMapping("/{email}/emailConfirm")
    public @ResponseBody ResponseEntity emailConfirm(@PathVariable("email") String email) throws Exception{
        confirm = mailService.sendSimpleMessage(email);
        return new ResponseEntity<String>("인증 메일을 전송했습니다. 메일함을 확인해주세요.", HttpStatus.OK);
    }

    //인증메일에 보낸 코드 체크
    @PostMapping("/{code}/codeCheck")
    public @ResponseBody ResponseEntity codeConfirm(@PathVariable("code") String code) throws Exception{
        if(code.equals(confirm)){
            confirmCheck=true;
            return new ResponseEntity<String> ("인증되었습니다.", HttpStatus.OK);
        }
        return new ResponseEntity<String> ("인증 코드를 올바르게 입력해주세요.", HttpStatus.BAD_REQUEST);
    }

    /*내 정보 수정*/
    @GetMapping(value = "/mypage")
    public String mypageForm(Model model, Principal principal){
        String email = principal.getName();
        try {
            MemberFormDto memberFormDto = memberService.getMemberDtlByEmail(email);
            model.addAttribute("memberFormDto", memberFormDto);
        }catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 회원입니다.");
            model.addAttribute("memberFormDto", new MemberFormDto());
            return "member/mypage";
        }
        return "member/mypage";
    }

    /*내정보 수정 저장*/
    @PostMapping(value = "/mypage/{memberId}")
    public String memberUpdate(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "member/mypage";
        }
        try {
            memberUpdateService.updateMember(memberFormDto);
        }catch (IllegalStateException e){
            model.addAttribute("errorMessage",  e.getMessage());
            return "member/mypage";
        }
        return "redirect:/";
    }

}



