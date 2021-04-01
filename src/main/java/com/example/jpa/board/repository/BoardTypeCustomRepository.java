package com.example.jpa.board.repository;

import com.example.jpa.board.model.BoardTypeCount;
import lombok.AllArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@AllArgsConstructor
@Repository
public class BoardTypeCustomRepository {

    private EntityManager entityManager;

    public List<BoardTypeCount> getBoardTypeCount() {
        String sql = " select bt.id, bt.board_name, bt.reg_date, bt.using_yn, " +
                "(select count(*) from board b where b.board_type_id = bt.id) as board_count " +
                "from board_type as bt ";

        //List<BoardTypeCount> list = entityManager.createNativeQuery (sql).getResultList ();

        // 네이티브 쿼리 쓸때에는 오브젝트로 받기 때문에 오브젝트로 받은걸 생성자로 모델 객체로 바꿔줌 (더 이뻐보이게 리턴하기 위해)
        //List<Object[]> result = entityManager.createNativeQuery (sql).getResultList ();
        //List<BoardTypeCount> list = result.stream ().map (e -> new BoardTypeCount (e)).collect(Collectors.toList());

        // 위에서 하는건 생성자를 만들어서 한땀 한땀 매칭 시켜야 되는데, 아래와 같이 qlrm 라이브러리 써서 매핑 시키면 간단하다.
        Query query = entityManager.createNativeQuery (sql);
        JpaResultMapper jpaResultMapper = new JpaResultMapper ();
        List<BoardTypeCount> list = jpaResultMapper.list (query, BoardTypeCount.class);

        return list;
    }
}
