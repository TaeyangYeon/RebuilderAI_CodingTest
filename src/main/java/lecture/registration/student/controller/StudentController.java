package lecture.registration.student.controller;

import lecture.registration.login.repository.LoginRepository;
import lecture.registration.student.dto.EnrollmentDto;
import lecture.registration.student.dto.LectureDto;
import lecture.registration.student.service.RegistrationLectureService;
import lecture.registration.student.service.RegistrationLectureServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 수강 신청 기간 검증 및 강의 목록 조회 및 신청 기능 담당 컨트롤러
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("student")
public class StudentController {

  /**
   * 수강 신청 기간 검증 및 강의 목록 조회 및 신청 기능을 담은 service 클래스 주입
   * 로그인 한 사용자 id 조회 기능을 담은 login 클래스 주입
   *
   */
  private final RegistrationLectureService registrationLectureService;
  private final LoginRepository loginRepository;
  DateFormat dateParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  /**
   * 강의 목록 패이지 응답
   */
  @GetMapping("lectureList")
  public String getLectureList(Model model, HttpServletRequest request) {

    //등록이 완려된 강의 목록 List
    List<LectureDto> lectureList = registrationLectureService.lectureList();
    model.addAttribute("lectureList", lectureList);

    //수강 신청 가능 여부 확인 (String 타입으로 담음)
    model.addAttribute("registrationAble", registrationLectureService.registrationDate());

    //수강 신청 완료한 강의 수량
    model.addAttribute("lectureQuan", registrationLectureService.registedLectureList(userId(request)).size());

    //학생의 강의목록 페이지 응답
    return "student/lectureList";
  }

  /**
   * 수강 신청을 완료한 강의 목록
   */
  @GetMapping("registedLectureList")
  public String getRegistrationLectureList(Model model, HttpServletRequest request) {
    List<LectureDto> lectureList = registrationLectureService.registedLectureList(userId(request));
    model.addAttribute("lectureList", lectureList);
    return "student/registedLectureList";
  }

  /**
   * 수강 신청
   */
  @PostMapping("registration")
  public ResponseEntity<String> registration(@RequestBody Long lectureId, HttpServletRequest request) {

    String nowDate = dateParse.format(new Date());
    EnrollmentDto enrollmentDto = new EnrollmentDto(userId(request), lectureId, nowDate);

    try {
      registrationLectureService.registration(enrollmentDto);
    } catch (RegistrationLectureServiceImpl.RegistedLectureLimitException e) {
      return ResponseEntity.badRequest().body("RegistedMax");
    } catch (RegistrationLectureServiceImpl.AleadyRegistedLectureException e) {
      return ResponseEntity.badRequest().body("AleadyRegistration");
    } catch (RegistrationLectureServiceImpl.RegistrationLimitReachedException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("StudentLimitReached");
    }
    return ResponseEntity.ok("Success");
  }

  /**
   * 신청 완료 목록에서 수강 신청 철회
   */
  @DeleteMapping("deleteLecture")
  public ResponseEntity<String> deleteLecture(@RequestBody Long lectureId, HttpServletRequest request) {

    try {
      registrationLectureService.deleteEnrollment(lectureId, userId(request));
    } catch (RegistrationLectureServiceImpl.NoDeleteEnrollmentException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("CantDelete");
    }
    return ResponseEntity.ok("Success");
  }

  private Long userId(HttpServletRequest request) {
    return loginRepository.getUserIdFromToken(request);
  }
}
