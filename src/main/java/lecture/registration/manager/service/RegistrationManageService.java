package lecture.registration.manager.service;

import lecture.registration.manager.dto.RegistrationDateDto;
import lecture.registration.professor.dto.LectureDto;
import lecture.registration.professor.dto.ProfessorDto;

import java.util.List;

public interface RegistrationManageService {

  RegistrationDateDto registrationDate();

  List<LectureDto> lectureList();
  List<LectureDto> pendingLecture();

  ProfessorDto professor(Long lectureId);

  LectureDto getLecture(Long lectureId);

  void insertOrUpdateRegistrationDate(String startDate, String endDate);

  void lectureOpen(Long lectureId);

}
