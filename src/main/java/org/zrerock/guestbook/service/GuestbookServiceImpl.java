package org.zrerock.guestbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zrerock.guestbook.dto.GuestbookDTO;
import org.zrerock.guestbook.dto.PageRequestDTO;
import org.zrerock.guestbook.dto.PageResultDTO;
import org.zrerock.guestbook.entity.GuestBook;
import org.zrerock.guestbook.repository.GuestbookRepository;

import java.util.function.Function;

@Service // 서비스 계층임을 명시
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class GuestbookServiceImpl implements GuestbookService{

    private final GuestbookRepository repository; // jpa 연결
    
    @Override
    public Long register(GuestbookDTO dto) { // 리턴은 gno, 입력은 dto

        log.info("GuestbookServiceImpl.register 메서드 실행...");
        log.info("dto : " + dto);

        GuestBook entity = dtoToEntity(dto); // 화면에서 받은 객체를 db로 전달
        log.info("entity : " + entity);

        repository.save(entity); // jpa로 inssert 처리
        
        return entity.getGno(); // inset된 방명록 번호가 리턴
    }

    @Override
    public PageResultDTO<GuestbookDTO, GuestBook> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());
        //   public Pageable getPageable(Sort sort){
        //
        //        return PageRequest.of(page -1, size, sort);
        //    }

        Page<GuestBook> result = repository.findAll(pageable); // jpa를 이용해서 페이징 처리 + 목록

        Function<GuestBook, GuestbookDTO> fn = (entity -> entityToDto(entity));
        // 함수 생성 <엔티티, dto> fn 이름으로 결과각 들어감
        return new PageResultDTO<>(result, fn);
        // public PageResultDTO(Page<EN> result, Function<EN,DTO> fn)
    }
}
