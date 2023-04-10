package lecture.registration.student.service;

import lecture.registration.student.dto.EnrollmentDto;
import lecture.registration.student.dto.LectureDto;

import java.util.List;

public interface RegistrationLectureService {

  List<LectureDto> lectureList();

  List<LectureDto> registedLectureList(Long studentId);

  String registrationDate();

  LectureDto getLecture(Long lectureId);

  void registration(EnrollmentDto enrollmentDto);

  void deleteEnrollment(Long lectureId, Long studentId);
}
