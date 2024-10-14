package org.zrerock.guestbook.service;

import org.zrerock.guestbook.dto.GuestbookDTO;
import org.zrerock.guestbook.dto.PageRequestDTO;
import org.zrerock.guestbook.dto.PageResultDTO;
import org.zrerock.guestbook.entity.GuestBook;
import org.zrerock.guestbook.entity.QGuestBook;

public interface GuestbookService {
    
    // C R U D 추상메서드 생성
    
    // 등록 메서드
    Long register(GuestbookDTO dto); // dto를 받아 처리함

    PageResultDTO<GuestbookDTO, GuestBook> getList(PageRequestDTO requestDTO);
    // PageResultDTO 요청을 받아서 PageResultDTO 결과를 출력
    // public PageResultDTO(Page<EN> result, Function<EN,DTO> fn)

    // dto를 엔티티로 변환, 엔테테를 dto호 변환하는 코드를 추가
    default GuestBook dtoToEntity(GuestbookDTO dto){ // default 추상메서드로 처리 안함
        GuestBook entity = GuestBook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }

    default GuestbookDTO entityToDto(GuestBook entity){
        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate()) // db에 있는 날짜 정보를 가져와야함
                .moddate(entity.getModDate()) // db에 있는 날짜 정보를 가져와야함
                .build();
        return dto;
    }
}
