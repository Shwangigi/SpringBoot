package org.zrerock.guestbook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {
    // 리스트 응답 시 페이징
    // 라스트 처리 결과를 Page<Entity> 타입으로 변환
    // 화면에 출력 필요한 페이지 정보를 구성

    private List<DTO> dtoList; // 제너릭으로 받은 dto를 리스트 처리

    // 총페이지 번호
    private int totalPage;

    // 현재 페이지 번호
    private int page;

    // 목록 사이즈
    private int size;

    // 시작페이지 번호, 끝 페이지 번호
    private int start, end;

    // 이전 , 다음
    private boolean prev, next;

    // 페이지 번호 목록
    private List<Integer> pageList;
    
    public PageResultDTO(Page<EN> result, Function<EN,DTO> fn){ // 엔테테를 함수형으로 처리
        // Function<EN,DTO> fn -> 엔티티 객체들을 dto로 변환해주는 기능
        // 나중에 어떤 종류의 Page<E> 타입이 생성되더라도, 처리 가능함
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();   // 68쪽 참고 (총 페이지)

        makePageList(result.getPageable());   // 아래쪽에 메서드 생성
    }

    private void makePageList(Pageable pageable){
        // 페이지 리스트 생성용 전용 코드

        this.page = pageable.getPageNumber() +  1; // 0부터 시작함
        this.size = pageable.getPageSize();

        // 계산용 페이지
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;

        start = tempEnd - 9;

        prev = start > 1;// 시작페이지가 1보다 크면 있음

        end = totalPage > tempEnd ? tempEnd : totalPage;
        //       조건              ?    참   :    거짓
        next = totalPage > tempEnd;  // next 버튼 (다음 페이지 용 코드)

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
        // 페이징용 가로 리스트 결과        next(t/f) 1,2,3,4,5,6,7,8,9,10 next(t/f)
    }
}
