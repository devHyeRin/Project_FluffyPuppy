package com.fluffypuppy.shop.controller;

import com.fluffypuppy.shop.dto.*;
import com.fluffypuppy.shop.entity.Item;
import com.fluffypuppy.shop.entity.Notice;
import com.fluffypuppy.shop.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    /*공지사항 등록*/
    @GetMapping(value = "/admin/notice/new")
    public String noticeForm(Model model){
        model.addAttribute("noticeFormDto",new NoticeFormDto());
        return "notice/noticeForm";
    }

    /*공지사항 등록 처리*/
    @PostMapping(value = "/admin/notice/new")
    public String noticeNew(@Valid NoticeFormDto noticeFormDto, BindingResult bindingResult, Model model, @RequestParam("noticeImgFile") List<MultipartFile> noticeImgFileList){
        if(bindingResult.hasErrors()){
            return "notice/noticeForm";
        }
        try{
            noticeService.saveNotice(noticeFormDto, noticeImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage", "공지사항 등록 중 에러가 발생하였습니다.");
            return "notice/noticeForm";
        }
        return "redirect:/admin/notices";
    }

    /*공지사항 수정 url*/
    @GetMapping(value = "/admin/notice/{noticeId}")
    public String noticeDtl(@PathVariable("noticeId")Long noticeId, Model model){
        try {
            NoticeFormDto noticeFormDto = noticeService.getNoticeDtl(noticeId);
            model.addAttribute("noticeFormDto", noticeFormDto);
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 공지사항입니다.");
            model.addAttribute("noticeFormDto", new NoticeFormDto());
            return "notice/noticeForm";
        }
        return "notice/noticeForm";
    }
    /*공지사항 수정 업데이트*/
    @PostMapping(value = "/admin/notice/{noticeId}")
    public String noticeUpdate(@Valid NoticeFormDto noticeFormDto, BindingResult bindingResult,
                               @RequestParam("noticeImgFile") List<MultipartFile> noticeImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "notice/noticeForm";
        }
        try {
            noticeService.updateNotice(noticeFormDto, noticeImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "notice/noticeForm";
        }
        return "redirect:/admin/notices";
    }

    /*관리자 공지사항 조회*/
    @GetMapping(value = {"/admin/notices", "/admin/notices/{page}"})
    public String noticeManage(NoticeSearchDto noticeSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Notice> notices = noticeService.getAdminNoticePage(noticeSearchDto, pageable);
        model.addAttribute("notices", notices);
        model.addAttribute("noticeSearchDto", noticeSearchDto);
        model.addAttribute("maxPage", 5);
        return "notice/noticeMng";
    }


    /*사용자 공지사항 조회*/
    @GetMapping(value = {"/notices", "/notices/{page}"})
    public String noticeUser(NoticeSearchDto noticeSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Notice> notices = noticeService.getUserNoticePage(noticeSearchDto, pageable);
        model.addAttribute("notices", notices);
        model.addAttribute("noticeSearchDto", noticeSearchDto);
        model.addAttribute("maxPage", 5);
        return "notice/noticeUser";
    }

    /*사용자 공지사항 id별 조회*/
    @GetMapping(value = "/notices/notice/{noticeId}")
    public String noticeUserDtl(Model model, @PathVariable("noticeId") Long noticeId) {
        NoticeFormDto noticeFormDto = noticeService.getUserNoticeDtl(noticeId);
        model.addAttribute("notice", noticeFormDto);
        return "notice/noticeDtl";
    }

}
