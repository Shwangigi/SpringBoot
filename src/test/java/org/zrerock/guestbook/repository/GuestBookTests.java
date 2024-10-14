package org.zrerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zrerock.guestbook.entity.GuestBook;
import org.zrerock.guestbook.entity.QGuestBook;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestBookTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies(){
        // 300개 정도 입력 테스트

        IntStream.rangeClosed(1,300).forEach(i -> {

            GuestBook guestBook = GuestBook.builder()
                    .title("제목..." + i )
                    .content("내용..." + i)
                    .writer("user" + (i%10))
                    .build();
            System.out.println(guestbookRepository.save(guestBook));
            // 리포지토리의 jpo 내장된 메서드인 save로 insert 처리함
        });
    }

    @Test
    public void updateTest(){

        Optional<GuestBook> result = guestbookRepository.findById(300L);
        // select * from guestbook where gno =300

        if(result.isPresent()){ // Optional 값이 null이 아니면

            GuestBook guestBook = result.get(); // 검색한 300 객체를 가져와 guestbook에 넣음
            guestBook.changeTitle("수정된 제목...");
            guestBook.changeContent("수정된 내용...");
            guestbookRepository.save(guestBook);
            // save -> update set...
        }
    }
  /*  Hibernate:
    select
    gb1_0.gno,
    gb1_0.content,
    gb1_0.moddate,
    gb1_0.regdate,
    gb1_0.title,
    gb1_0.writer
            from
    guest_book gb1_0
    where
    gb1_0.gno=?
    Hibernate:
    select
    gb1_0.gno,
    gb1_0.content,
    gb1_0.moddate,
    gb1_0.regdate,
    gb1_0.title,
    gb1_0.writer
            from
    guest_book gb1_0
    where
    gb1_0.gno=?
    Hibernate:
    update
            guest_book
    set
    content=?,
    moddate=?,
    title=?,
    writer=?
    where
    gno=?*/

    @Test
    public void testQuery(){
        // 쿼리 dsl을 이용해서 단일 검색용 -> 0페이지, 10개, 정렬(gno를 기준으로 내림차순), 제목=1이 들어있는 조건

        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());
        // 0 페이지, 10개, 정렬(gno.내림)

        QGuestBook qGuestBook = QGuestBook.guestBook; // 쿼리dsl로 객체 생성

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder(); // where 조건이 있냐? 없냐

        BooleanExpression expression = qGuestBook.title.contains(keyword); // 제목에 1을 넣음
        // 주의 : import com.querydsl.core.types.dsl.BooleanExpression;

        builder.and(expression); // 검색 where문 붙임

        Page<GuestBook> result = guestbookRepository.findAll(builder, pageable); // 찾은 값을 적용
        // builder -> where, pageable -> 페이징 + 정렬

        result.stream().forEach(guestBook -> {
            System.out.println(guestBook);
        }); // 결과 콘솔 출력
      /*  Hibernate:
        select
        gb1_0.gno,
                gb1_0.content,
                gb1_0.moddate,
                gb1_0.regdate,
                gb1_0.title,
                gb1_0.writer
        from
        guest_book gb1_0
        where
        gb1_0.title like ? escape '!'
        order by
        gb1_0.gno desc
        limit
                ?, ?
        Hibernate:
        select
        count(gb1_0.gno)
        from
        guest_book gb1_0
        where
        gb1_0.title like ? escape '!'
        GuestBook(gno=291, title=제목...291, content=내용...291, writer=user1)
        GuestBook(gno=281, title=제목...281, content=내용...281, writer=user1)
        GuestBook(gno=271, title=제목...271, content=내용...271, writer=user1)
        GuestBook(gno=261, title=제목...261, content=내용...261, writer=user1)
        GuestBook(gno=251, title=제목...251, content=내용...251, writer=user1)
        GuestBook(gno=241, title=제목...241, content=내용...241, writer=user1)
        GuestBook(gno=231, title=제목...231, content=내용...231, writer=user1)
        GuestBook(gno=221, title=제목...221, content=내용...221, writer=user1)
        GuestBook(gno=219, title=제목...219, content=내용...219, writer=user9)
        GuestBook(gno=218, title=제목...218, content=내용...218, writer=user8)*/
    }

    @Test
    public void testQueryMulti(){
        // 제목과 내용을  where문으로 다중검색

        Pageable pageable = PageRequest.of(0,7,Sort.by("gno").descending());

        QGuestBook qGuestBook = QGuestBook.guestBook;

        String keyword = "2";

        BooleanBuilder builder = new BooleanBuilder(); // 조건이 있냐> where 생성용

        BooleanExpression exTitle = qGuestBook.title.contains(keyword); // 조건 1

        BooleanExpression exContent = qGuestBook.content.contains(keyword); // 조건 2

        // 조건들을 합체
        BooleanExpression exAll = exTitle.or(exContent); // WHERE title or content

        builder.and(exAll); // 조건문 합체

        builder.and(qGuestBook.gno.gt(0L)); // pk에 index를 활용해서 빠른 추출

        Page<GuestBook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestBook -> {
            System.out.println(guestBook);
        });

       /* Hibernate:
        select
        gb1_0.gno,
                gb1_0.content,
                gb1_0.moddate,
                gb1_0.regdate,
                gb1_0.title,
                gb1_0.writer
        from
        guest_book gb1_0
        where
                (
                        gb1_0.title like ? escape '!'
        or gb1_0.content like ? escape '!'
        )
        and gb1_0.gno>?
                order by
        gb1_0.gno desc
        limit
                ?, ?
        Hibernate:
        select
        count(gb1_0.gno)
        from
        guest_book gb1_0
        where
                (
                        gb1_0.title like ? escape '!'
        or gb1_0.content like ? escape '!'
        )
        and gb1_0.gno>?
                GuestBook(gno=299, title=제목...299, content=내용...299, writer=user9)
        GuestBook(gno=298, title=제목...298, content=내용...298, writer=user8)
        GuestBook(gno=297, title=제목...297, content=내용...297, writer=user7)
        GuestBook(gno=296, title=제목...296, content=내용...296, writer=user6)
        GuestBook(gno=295, title=제목...295, content=내용...295, writer=user5)
        GuestBook(gno=294, title=제목...294, content=내용...294, writer=user4)
        GuestBook(gno=293, title=제목...293, content=내용...293, writer=user3)*/

    }
}
