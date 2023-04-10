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

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("student")
public class StudentController {

  private final RegistrationLectureService registrationLectureService;
  private final LoginRepository loginRepository;
  DateFormat dateParse = new SimpleDateFormat("yyyy-MM-dd");

  @GetMapping("lectureList")
  public String getLectureList(Model model, HttpServletRequest request) {

    List<LectureDto> lectureList = registrationLectureService.lectureList();
    model.addAttribute("lectureList", lectureList);
    model.addAttribute("registrationAble", registrationLectureService.registrationDate());
    model.addAttribute("lectureQuan", registrationLectureService.registedLectureList(userId(request)).size());

    return "student/lectureList";
  }

  @GetMapping("registedLectureList")
  public String getRegistrationLectureList(Model model, HttpServletRequest request) {
    List<LectureDto> lectureList = registrationLectureService.registedLectureList(userId(request));
    model.addAttribute("lectureList", lectureList);
    return "student/registedLectureList";
  }

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
