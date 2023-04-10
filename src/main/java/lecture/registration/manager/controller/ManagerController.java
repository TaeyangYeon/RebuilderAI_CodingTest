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

@Controller
@RequiredArgsConstructor
@RequestMapping("manager")
public class ManagerController {

  private final RegistrationManageService registrationManageService;
  private final ProfessorLectureService professorLectureService;

  @GetMapping("lectureList")
  public String getLectureList(Model model) {
    RegistrationDateDto registrationDateDto = registrationManageService.registrationDate();
    model.addAttribute("registrationDateDto", registrationDateDto);

    List<LectureDto> lectureList = registrationManageService.lectureList();
    model.addAttribute("lectureList", lectureList);

    List<LectureDto> pendingLecture = registrationManageService.pendingLecture();
    model.addAttribute("pendingLecture", pendingLecture);

    return "/manager/lectureList";
  }

  @PostMapping("saveDate")
  public ResponseEntity<String> saveDate(@RequestBody RegistrationDateDto registrationDateDto) {
    try {
      registrationManageService.insertOrUpdateRegistrationDate(registrationDateDto.getStartDate(), registrationDateDto.getEndDate());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("기간 설정 오류");
    }
    return ResponseEntity.ok("Success");
  }

  @GetMapping("studentList")
  public String getStudentList(Long lectureId, Model model) {
    LectureDto lectureDto = registrationManageService.getLecture(lectureId);
    model.addAttribute("lectureDto", lectureDto);

    ProfessorDto professorDto = registrationManageService.professor(lectureId);
    model.addAttribute("professorDto", professorDto);

    List<StudentDto> studentList = professorLectureService.studentList(lectureId);
    model.addAttribute("studentList", studentList);

    return "/manager/studentList";
  }

  @PutMapping("registLecture")
  public ResponseEntity<String> registLecture(@RequestBody Long lectureId) {
    registrationManageService.lectureOpen(lectureId);
    return ResponseEntity.ok("Success");
  }

}
