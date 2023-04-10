package lecture.registration.student.repository;

import lecture.registration.student.dto.EnrollmentDto;
import lecture.registration.student.dto.LectureDto;

import java.util.List;

public interface RegistrationLectureRepository {

  List<LectureDto> lectureList();

  int studentLectureCount(Long studentId);

  LectureDto lecture(Long lectureId);

  int enrollmentCount(Long lectureId);

  List<LectureDto> registedLectureList(Long studentId);

  int registedLecture(Long lectureId, Long studentId);

  void registration(EnrollmentDto enrollmentDto);

  void deleteEnrollment(Long lectureId, Long studentId);

}
