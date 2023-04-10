package lecture.registration.manager.controller;

import lecture.registration.manager.dto.RegistrationDateDto;
import lecture.registration.manager.service.RegistrationManageService;
import lecture.registration.professor.dto.LectureDto;
import lecture.registration.professor.dto.ProfessorDto;
import lecture.registration.professor.service.ProfessorLectureService;
import lecture.registration.student.dto.StudentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 운영 기능을 담당하는 controller 클래스
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("manager")
public class ManagerController {

  /**
   * 운영에 필요한 기능 메서드가 담긴 service 클래스 주입
   * 학생 조회 기능 메거드가 담긴 service 클래스 주입
   */
  private final RegistrationManageService registrationManageService;
  private final ProfessorLectureService professorLectureService;

  /**
   * 수강 신청 기간 입력 및 수정 및 등록과 미등록으로 구분하여 강의 목록 조회하는 페이지 응답
   */
  @GetMapping("lectureList")
  public String getLectureList(Model model) {
    //수간 신청일을 담은 Dto클래스
    RegistrationDateDto registrationDateDto = registrationManageService.registrationDate();
    model.addAttribute("registrationDateDto", registrationDateDto);

    //등록된 강의 List
    List<LectureDto> lectureList = registrationManageService.lectureList();
    model.addAttribute("lectureList", lectureList);

    //미등록 강의 List
    List<LectureDto> pendingLecture = registrationManageService.pendingLecture();
    model.addAttribute("pendingLecture", pendingLecture);

    //관리자 강의 목록 페이지 응답
    return "/manager/lectureList";
  }

  //수강 신청일을 저장 처리
  @PostMapping("saveDate")
  public ResponseEntity<String> saveDate(@RequestBody RegistrationDateDto registrationDateDto) {

    //시작일과 종료일을 매개변수로 기간 테이블에 저장
    try {
      registrationManageService.insertOrUpdateRegistrationDate(registrationDateDto.getStartDate(), registrationDateDto.getEndDate());
    } catch (IllegalArgumentException e) {
      //2주 ~ 4주 조건에 맞지 않을 경우 : 400 응답
      return ResponseEntity.badRequest().body("기간 설정 오류");
    }
    //저장 완료시 성공 응답
    return ResponseEntity.ok("Success");
  }

  //교수, 학생 목록 페이지 응답
  @GetMapping("studentList")
  public String getStudentList(Long lectureId, Model model) {
    //선택한 강의에 대한 값
    LectureDto lectureDto = registrationManageService.getLecture(lectureId);
    model.addAttribute("lectureDto", lectureDto);

    //해당 강의를 등록하고 담당하는 교수의 값
    ProfessorDto professorDto = registrationManageService.professor(lectureId);
    model.addAttribute("professorDto", professorDto);

    //해당 강의를 수강신청한 학생 목록
    List<StudentDto> studentList = professorLectureService.studentList(lectureId);
    model.addAttribute("studentList", studentList);

    //관리자의 학생 목록 페이지 응답
    return "/manager/studentList";
  }

  //미등록 강의를 등록처리
  @PutMapping("registLecture")
  public ResponseEntity<String> registLecture(@RequestBody Long lectureId) {
    //강의 식별키를 매개변수로 해당 강의를 등록 완료 시킨 뒤 성공 응답
    registrationManageService.lectureOpen(lectureId);
    return ResponseEntity.ok("Success");
  }

}
