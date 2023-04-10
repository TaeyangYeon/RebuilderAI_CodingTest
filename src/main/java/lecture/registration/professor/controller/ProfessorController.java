package lecture.registration.professor.controller;

import lecture.registration.lecture.dto.lectureDto;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("professor")
public class ProfessorController {

  private final ProfessorLectureService professorLectureService;
  private final LoginRepository loginRepository;

  @GetMapping("lectureList")
  public String getLectureList(Model model, HttpServletRequest request) {
    List<LectureDto> lectureList = professorLectureService.lectureList(userId(request));
    model.addAttribute("lectureList", lectureList);

    List<lectureDto> pendingLecture = professorLectureService.pendingLecture(userId(request));
    model.addAttribute("pendingLecture", pendingLecture);

    int lectureQuan = professorLectureService.lectureQuan(userId(request));
    model.addAttribute("lectureQuan", lectureQuan);

    return "/professor/lectureList";
  }

  @GetMapping("studentList")
  public ResponseEntity studentList(HttpServletRequest request) {
    Long lectureId = Long.valueOf(request.getParameter("lectureId"));
    try {
      professorLectureService.getLecture(lectureId);
    } catch (ProfessorLectureServiceImpl.NotFinishRegistrationException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수강신청 완료 후 조회 가능");
    }
    return ResponseEntity.ok("Success");
  }

  @GetMapping("getStudentList")
  public String getStudentList(Long lectureId, Model model) {
    List<StudentDto> studentList = professorLectureService.studentList(lectureId);
    model.addAttribute("studentList", studentList);

    LectureDto lectureDto = professorLectureService.getLecture(lectureId);
    model.addAttribute("lectureDto", lectureDto);

    return "/professor/studentList";
  }

  @PostMapping("addLecture")
  public ResponseEntity addLecture(@RequestBody LectureDto lectureDto, HttpServletRequest request) {

    try {
      professorLectureService.addLecture(lectureDto, userId(request));
    } catch (ProfessorLectureServiceImpl.LectureMax e) {
      return ResponseEntity.badRequest().body("추가 등록 불가");
    }
    return ResponseEntity.ok("Success");
  }



  private Long userId(HttpServletRequest request) {
    return loginRepository.getUserIdFromToken(request);
  }

}
