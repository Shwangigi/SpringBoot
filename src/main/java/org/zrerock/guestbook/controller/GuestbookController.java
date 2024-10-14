package org.zrerock.guestbook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zrerock.guestbook.dto.PageRequestDTO;
import org.zrerock.guestbook.dto.PageResultDTO;
import org.zrerock.guestbook.service.GuestbookService;

@Controller
@Log4j2
@RequestMapping("guestbook")
@RequiredArgsConstructor // 생성자 자동 주입 final용
public class GuestbookController {
    
    private final GuestbookService service; // @RequiredArgsConstructor용 필드

    @GetMapping("/")
    public String index(){
        // http://localhost:80/로 왔을 때 list로 보낼 것임
        return "redirect:/guestbook/list";
    }

    @GetMapping("/list")
    public String list(PageRequestDTO pageRequestDTO, Model model){
        log.info("GuestbookController.list메서드 실행");

        model.addAttribute("result", service.getList(pageRequestDTO));
        return "/guestbook/list";
    }
}
