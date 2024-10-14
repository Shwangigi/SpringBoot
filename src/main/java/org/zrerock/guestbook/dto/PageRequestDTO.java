package org.zrerock.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
// @NoArgsConstructor 현재 기본 생성자를 수동으로 만들었음
@Data
public class PageRequestDTO {
    // 리스트 요청 시 페이징 처리 사용하는 데이터를 재사용
    // 페이지 번호, 목록의 개수 , 검색 조건 등 ...

    private int page;
    private int size;

    public PageRequestDTO(){ // 기본생성자
        this.page = 1;   // 기본 페이지 번호
        this.size = 10;   // 기본게시물 수
    }

    public Pageable getPageable(Sort sort){

        return PageRequest.of(page -1, size, sort);
    }

}
