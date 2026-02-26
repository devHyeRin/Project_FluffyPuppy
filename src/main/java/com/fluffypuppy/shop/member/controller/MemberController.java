package com.fluffypuppy.shop.member.controller;

import com.fluffypuppy.shop.member.dto.MemberFormDto;
import com.fluffypuppy.shop.member.entity.Member;
import com.fluffypuppy.shop.service.MailService;
import com.fluffypuppy.shop.member.service.MemberService;
import com.fluffypuppy.shop.member.service.MemberUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
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
        return "member/signup";
    }

    /*회원가입 정보 저장*/
    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "member/signup";
        }
        if (memberFormDto.getPassword() != null && !memberFormDto.getPassword().equals(memberFormDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "비밀번호가 일치하지 않습니다.");
            return "member/signup";
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        }
        catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/signup";
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
        return "member/login";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "member/login";
    }

    //인증메일 보내기
    @PostMapping("/{email}/emailConfirm")
    public @ResponseBody ResponseEntity<Map<String, String>> emailConfirm(@PathVariable("email") String email) {
        Map<String, String> res = new HashMap<>();
        try {
            confirm = mailService.sendSimpleMessage(email);
            res.put("message", "인증 메일을 전송했습니다. 메일함을 확인해주세요.");
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("message", "메일 발송 서버에 문제가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    //인증메일에 보낸 코드 체크
    @PostMapping("/{code}/codeCheck")
    public @ResponseBody ResponseEntity<Map<String, String>> codeConfirm(@PathVariable("code") String code) {
        Map<String, String> res = new HashMap<>();

        if (code.equals(confirm)) {
            confirmCheck = true;
            res.put("message", "이메일 인증이 완료되었습니다.");
            return ResponseEntity.ok(res); // 200 OK
        } else {
            confirmCheck = false;
            res.put("message", "인증 코드가 올바르지 않습니다. 다시 확인해주세요.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
    }

    /*내 정보 수정*/
    @GetMapping(value = "/mypage")
    public String mypageForm(Model model, Authentication authentication) {

        String email = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            email = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else if (authentication.getPrincipal() instanceof OAuth2User) {
            email = ((OAuth2User) authentication.getPrincipal()).getAttribute("email");
        }

        try {
            MemberFormDto memberFormDto = memberService.getMemberDtlByEmail(email);
            model.addAttribute("memberFormDto", memberFormDto);
        } catch(EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 회원입니다.");
            return "member/mypage";
        }
        return "member/mypage";
    }

    /*내정보 수정 저장*/
    @PostMapping(value = "/mypage")
    public String memberUpdate(@Valid MemberFormDto memberFormDto,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes rttr){

        if (memberFormDto.getPassword() != null && !memberFormDto.getPassword().isEmpty()) {
            if (!memberFormDto.getPassword().equals(memberFormDto.getConfirmPassword())) {
                bindingResult.rejectValue("confirmPassword", "passwordIncorret", "비밀번호가 일치하지 않습니다.");
            }
        }

        if (bindingResult.hasErrors()) {
            boolean isOnlyPasswordError = bindingResult.getFieldErrors().stream()
                    .allMatch(error -> (error.getField().equals("password") || error.getField().equals("confirmPassword"))
                            && (memberFormDto.getPassword() == null || memberFormDto.getPassword().isEmpty()));

            if (!isOnlyPasswordError) {
                return "member/mypage";
            }
        }
        try {
            memberUpdateService.updateMember(memberFormDto);
            rttr.addFlashAttribute("msg", "수정이 완료되었습니다.");
        }catch (Exception e){
            model.addAttribute("errorMessage",  "정보 수정 중 오류가 발생했습니다.");
            return "member/mypage";
        }
        return "redirect:/members/mypage";
    }

}



