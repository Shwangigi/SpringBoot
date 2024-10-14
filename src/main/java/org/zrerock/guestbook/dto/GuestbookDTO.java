package org.zrerock.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder // setter 대체용
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GuestbookDTO {

    // 엔티티에 있는 정보를 객체화 시킴

    private Long gno;
    private String title, content, writer;
    private LocalDateTime regDate, moddate;
}
