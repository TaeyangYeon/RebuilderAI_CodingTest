package lecture.registration.professor.controller;

import lecture.registration.login.repository.LoginRepository;
import lecture.registration.professor.dto.LectureDto;
import lecture.registration.professor.service.ProfessorLectureService;
import lecture.registration.professor.service.ProfessorLectureServiceImpl;
import lecture.registration.student.dto.StudentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 강의 추가 및 조회, 학생 조회를 위한 기능을 담당하는 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("professor")
public class ProfessorController {

  /**
   * 강의 추가 및 조회 기능은 담은 service 클래스 주입
   * 로그인 한 사용자 id 조회 기능을 담은 login 클래스 주입
   */
  private final ProfessorLectureService professorLectureService;
  private final LoginRepository loginRepository;

  /**
   * 강의 목록 페이지 응답
   * 강의 목록, 미등록 강의 목록, 강의 수량(최대 2개)
   */
  @GetMapping("lectureList")
  public String getLectureList(Model model, HttpServletRequest request) {
    //사용자 아이디를 기준으로 등록 완료된 강의 List
    List<LectureDto> lectureList = professorLectureService.lectureList(userId(request));
    model.addAttribute("lectureList", lectureList);

    //사용자 아이디를 기준으로 미등록 강의 List
    List<LectureDto> pendingLecture = professorLectureService.pendingLecture(userId(request));
    model.addAttribute("pendingLecture", pendingLecture);

    //사용자 아이디 기준 추가한 강의 수량
    int lectureQuan = professorLectureService.lectureQuan(userId(request));
    model.addAttribute("lectureQuan", lectureQuan);

    //교수의 강의 목록 페이지 응답
    return "/professor/lectureList";
  }

  /**
   * 학생 목록을 조회할 수 있는지 확인
   */
  @GetMapping("studentList")
  public ResponseEntity studentList(HttpServletRequest request) {
    Long lectureId = Long.valueOf(request.getParameter("lectureId"));
    try {
      professorLectureService.getLecture(lectureId);
    } catch (ProfessorLectureServiceImpl.NotFinishRegistrationException e) {
      //수간신청이 완료되지 않은 상태로 조회 시도할 시 : 403 응답
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수강신청 완료 후 조회 가능");
    }
    //성공시 성공 응답
    return ResponseEntity.ok("Success");
  }

  /**
   * 학생 목록 페이지 응답
   * 선택한 강의의 정보, 학생 목록
   */
  @GetMapping("getStudentList")
  public String getStudentList(Long lectureId, Model model) {
    //선택한 강의의 정보
    List<StudentDto> studentList = professorLectureService.studentList(lectureId);
    model.addAttribute("studentList", studentList);

    //해당 강의를 수간신청한 학생 목록
    LectureDto lectureDto = professorLectureService.getLecture(lectureId);
    model.addAttribute("lectureDto", lectureDto);

    //교수의 학생 목록 페이지 응답
    return "/professor/studentList";
  }

  //강의 추가
  @PostMapping("addLecture")
  public ResponseEntity addLecture(@RequestBody LectureDto lectureDto, HttpServletRequest request) {

    /**
     * 강의 제목과 강의 내용으로 강의 추가
     */
    try {
      professorLectureService.addLecture(lectureDto, userId(request));
    } catch (ProfessorLectureServiceImpl.LectureMax e) {
      //강의 추가 수량이 초과일시 : 400 응답
      return ResponseEntity.badRequest().body("추가 등록 불가");
    }
    //성공시 성공 응답
    return ResponseEntity.ok("Success");
  }

  //토큰에서 꺼낸 사용자 id
  private Long userId(HttpServletRequest request) {
    return loginRepository.getUserIdFromToken(request);
  }

}
