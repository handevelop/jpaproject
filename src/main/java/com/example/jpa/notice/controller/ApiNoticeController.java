package com.example.jpa.notice.controller;

import com.example.jpa.notice.Exception.DuplicateNoticeException;
import com.example.jpa.notice.Exception.NoticeNotFoundException;
import com.example.jpa.notice.entity.Notice;
import com.example.jpa.notice.model.NoticeDeleteInput;
import com.example.jpa.notice.model.NoticeInput;
import com.example.jpa.notice.model.ResponseError;
import com.example.jpa.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiNoticeController {

    // @Autowired 대신 @RequiredArgsConstructor 를 사용해서 생성자를 주입.
    private final NoticeRepository noticeRepository;

//    @GetMapping("/notice")
//    public String noticeString() {
//        return "공지사항 입니다.";
//    }

//    @GetMapping("/notice")
//    public NoticeModel noticeString() {
//        LocalDateTime localDateTime = LocalDateTime.of (2021, 3, 2, 0, 0, 0);
//
//        NoticeModel noticeModel =  new NoticeModel ();
//        noticeModel.setId (1);
//        noticeModel.setTitle ("공지사항 입니다.");
//        noticeModel.setContents ("공지사항 내용입니다.");
//        noticeModel.setRegDate (localDateTime);
//
//        return noticeModel;
//    }

//    @GetMapping("/notice")
//    public List<NoticeModel> noticeString() {
//        List<NoticeModel> noticeModelList = new ArrayList<> ();
//
//        NoticeModel noticeModel1 = new NoticeModel ();
//        noticeModel1.setId (1);
//        noticeModel1.setTitle ("공지사항1 입니다.");
//        noticeModel1.setContents ("공지사항1 내용입니다.");
//        noticeModel1.setRegDate (LocalDateTime.of (2021, 3, 1, 0, 0, 0));
//
//        noticeModelList.add (noticeModel1);
//
//        // 빌더패턴 활용
//        noticeModelList.add (NoticeModel.builder ()
//                .id (2)
//                .title ("공지사항2 입니다.")
//                .contents ("공지사항2 내용입니다.")
//                .regDate (LocalDateTime.of (2021, 3, 2, 0, 0, 0))
//                .build ());
//
//        return noticeModelList;
//    }

//    @GetMapping("/notice")
//    public List<NoticeModel> noticeString() {
//        List<NoticeModel> noticeModelList = new ArrayList<> ();
//
//        return noticeModelList;
//    }

//    @GetMapping("/notice/count")
//    public int noticeCount() {
//        return 10;
//    }

//    @PostMapping("/notice")
//    public NoticeModel noticeWrite(@RequestParam String title, @RequestParam String content) {
//        NoticeModel noticeModel = new NoticeModel ();
//
//        noticeModel.setId (1);
//        noticeModel.setTitle (title);
//        noticeModel.setContents (content);
//        noticeModel.setRegDate (LocalDateTime.now ());
//
//        return noticeModel;
//    }

//    @PostMapping("/notice2")
//    public NoticeModel noticeWrite2(NoticeModel param) {
//        NoticeModel noticeModel = new NoticeModel ();
//
//        noticeModel.setId (2);
//        noticeModel.setTitle (param.getTitle ());
//        noticeModel.setContents (param.getContents ());
//        noticeModel.setRegDate (LocalDateTime.now ());
//
//        return noticeModel;
//    }

//    @PostMapping("/notice3")
//    public NoticeModel noticeWrite3(@RequestBody NoticeModel param) {
//        NoticeModel noticeModel = new NoticeModel ();
//
//        noticeModel.setId (3);
//        noticeModel.setTitle (param.getTitle ());
//        noticeModel.setContents (param.getContents ());
//        noticeModel.setRegDate (LocalDateTime.now ());
//
//        return noticeModel;
//    }

//    @PostMapping("/notice4")
//    public NoticeModel noticeWrite4(@RequestBody NoticeModel param) {
//        NoticeModel noticeModel = new NoticeModel ();
//
//        noticeModel.setId (3);
//        noticeModel.setTitle (param.getTitle ());
//        noticeModel.setContents (param.getContents ());
//        noticeModel.setRegDate (LocalDateTime.now ());
//
//        return noticeModel;
//    }

//    @PostMapping("/notice")
//    public Notice addNotice(@RequestBody NoticeInput noticeInput) {
//
//        Notice notice = Notice.builder ()
//                .title (noticeInput.getTitle ())
//                .contents (noticeInput.getContents ())
//                .regDate (LocalDateTime.now ())
//                .build ();
//
//        noticeRepository.save (notice);
//
//        return notice;
//    }

//    @PostMapping("/notice")
//    public Notice addNotice(@RequestBody NoticeInput noticeInput) {
//
//        Notice notice = Notice.builder ()
//                .title (noticeInput.getTitle ())
//                .contents (noticeInput.getContents ())
//                .regDate (LocalDateTime.now ())
//                .hits (0)
//                .likes (0)
//                .build ();
//
//        Notice result = noticeRepository.save (notice);
//        return result;
//    }

    // ddl-auto: create-drop -> 서버 생성시 데이터를 drop(날림)
    // generate-ddl: true -> ddl 을 생성하도록 함
//    @GetMapping("/notice/{id}")
//    public Notice addNotice(@PathVariable Long id) {
//
//        Optional<Notice> notice = noticeRepository.findById (id);
//
//        if (notice.isPresent ()) {
//            return notice.get ();
//        }
//
//        return null;
//    }

//    @PutMapping("/notice/{id}")
//    public void modifyNotice(@PathVariable Long id, @RequestBody NoticeInput noticeInput) {
//
//        Optional<Notice> notice = noticeRepository.findById (id);
//
//        if (notice.isPresent ()) {
//
//            notice.get ().setTitle (noticeInput.getTitle ());
//            notice.get ().setContents (noticeInput.getContents ());
//            notice.get ().setModDate (LocalDateTime.now ());
//            noticeRepository.save (notice.get ());
//        }
//    }

    @ExceptionHandler(NoticeNotFoundException.class)
    public ResponseEntity<String> handlerNoticeNotFoundException(NoticeNotFoundException exception) {

        return new ResponseEntity<> (exception.getMessage (), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateNoticeException.class)
    public ResponseEntity<String> handlerDuplicationNoticeException(DuplicateNoticeException exception) {

        return new ResponseEntity<> (exception.getMessage (), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/notice/{id}")
    public void modifyNotice(@PathVariable Long id, @RequestBody NoticeInput noticeInput) {

//        Optional<Notice> notice = noticeRepository.findById (id);
//
//        if (!notice.isPresent ()) {
//            // 예외 발생
//            throw new NoticeNotFoundException ("공지사항의 글이 존재하지 않습니다.");
//        }
//
//        // 게시글이 있을 때
//        notice.get ().setTitle (noticeInput.getTitle ());
//        notice.get ().setContents (noticeInput.getContents ());
//        notice.get ().setModDate (LocalDateTime.now ());
//        noticeRepository.save (notice.get ());

        Notice notice = noticeRepository.findById (id)
                .orElseThrow (() -> new NoticeNotFoundException ("공지사항의 글이 없다."));

        notice.setTitle (noticeInput.getTitle ());
        notice.setContents (noticeInput.getContents ());
        notice.setUpdateDate (LocalDateTime.now ());
        noticeRepository.save (notice);
    }

    // PATCH 는 부분적인 수정을 할 때 쓰
//    @PatchMapping("/notice/{id}/hits")
//    public void updateNoticeHit(@PathVariable Long id) {
//
//        Notice notice = noticeRepository.findById (id)
//                .orElseThrow (() -> new NoticeNotFoundException ("공지사항의 글이 없다."));
//
//        notice.setHits (notice.getHits () + 1);
//        noticeRepository.save (notice);
//    }

//    @DeleteMapping("/notice/{id}")
//    public void deleteNotice(@PathVariable Long id) {
//
//        Notice notice = noticeRepository.findById (id)
//                .orElseThrow (() -> new NoticeNotFoundException ("삭제할 글이 없다."));
//
//        noticeRepository.delete (notice);
//    }

//    @DeleteMapping("/notice")
//    public void deleteNoticeList(@RequestBody NoticeDeleteInput noticeDeleteInput) {
//
//        List<Notice> noticeList = noticeRepository.findByIdList (noticeDeleteInput.getIdList ())
//                .orElseThrow (() -> new NoticeNotFoundException ("삭제할 글이 없다."));
//
//        noticeList.stream ().forEach (e -> {
//            e.setDelYn ("Y");
//            e.setModDate (LocalDateTime.now ());
//        });
//
//        noticeRepository.saveAll (noticeList);
//    }

    @DeleteMapping("/notice/all")
    public void deleteAll() {

        noticeRepository.deleteAll ();
    }

//    @PostMapping("/notice")
//    public ResponseEntity<Object> addNotice(@RequestBody @Valid NoticeInput noticeInput, Errors errors) {
//
////        if (errors.hasErrors ()) {
////            List<ResponseError> responseErrorList = new ArrayList<> ();
////
////            errors.getAllErrors ().stream ().forEach (e -> {
////
////                ResponseError responseError = new ResponseError ();
////                responseError.setMessage (e.getDefaultMessage ());
////                responseError.setField (((FieldError)e).getField ());
////                responseErrorList.add (responseError);
////            });
////
////            return new ResponseEntity<> (responseErrorList, HttpStatus.BAD_REQUEST);
////        }
//
//        // 위 소스를 아래처럼 리팩토링
//        if (errors.hasErrors ()) {
//            List<ResponseError> responseErrorList = new ArrayList<> ();
//
//            errors.getAllErrors ().stream ().forEach (e -> {
//                responseErrorList.add (ResponseError.of ((FieldError) e));
//            });
//            return new ResponseEntity<> (responseErrorList, HttpStatus.BAD_REQUEST);
//        }
//
//
//        Notice notice = Notice.builder ()
//                .title (noticeInput.getTitle ())
//                .contents (noticeInput.getContents ())
//                .hits (0)
//                .likes (0)
//                .regDate (LocalDateTime.now ())
//                .build ();
//
//        noticeRepository.save (notice);
//
//        return ResponseEntity.ok ().build ();
//
//    }


    @GetMapping("/notice/{cnt}")
    public Page<Notice> getNotice(@PathVariable int cnt) {

        // 등록일 기준 내림차순 정렬 한 걸 가져옴
        Page<Notice> result = noticeRepository.findAll (PageRequest.of (0, cnt, Sort.Direction.DESC, "regDate"));

        return result;
    }

    /**
     * 30번 문제
     * @param noticeInput
     * @param errors
     * @return
     */
    @PostMapping("/notice")
    public ResponseEntity<Object> addNotice(@RequestBody @Valid NoticeInput noticeInput, Errors errors) {

        // 파라미터 validation
        if (errors.hasErrors ()) {
            List<ResponseError> responseErrorList = new ArrayList<> ();

            errors.getAllErrors ().stream ().forEach (e -> {
                responseErrorList.add (ResponseError.of ((FieldError) e));
            });
            return new ResponseEntity<> (responseErrorList, HttpStatus.BAD_REQUEST);
        }

        // 중복체크
//        LocalDateTime checkTime = LocalDateTime.now ().minusMinutes (1);
//
//        Optional<List<Notice>> noticeList = noticeRepository.findByTitleAndContentsAndRegDateIsGreaterThanEqual (noticeInput.getTitle (), noticeInput.getContents (), checkTime);
//
//        if (noticeList.isPresent ()) {
//            if (noticeList.get ().size () > 0) {
//                throw new DuplicateNoticeException ("동일한 내용이 1분 이내에 등록되었습니다.");
//            }
//
//        }

        // 위에꺼를 리팩토링
        // 현재시간보다 1분 작은거
        LocalDateTime checkTime = LocalDateTime.now ().minusMinutes (1);

        int duplNoticeCnt = noticeRepository.countByTitleAndContentsAndRegDateIsGreaterThanEqual (noticeInput.getTitle (), noticeInput.getContents (), checkTime);

        if (duplNoticeCnt > 0) {
            throw new DuplicateNoticeException ("동일한 내용이 1분 이내에 등록되었습니다.");
        }


        noticeRepository.save(Notice.builder ()
                .title (noticeInput.getTitle ())
                .contents (noticeInput.getContents ())
                .hits (0)
                .likes (0)
                .regDate (LocalDateTime.now ())
                .build ());

        return ResponseEntity.ok ().build ();

    }
}
