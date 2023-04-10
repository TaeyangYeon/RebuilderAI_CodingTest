package lecture.registration.manager.repository;

import lecture.registration.manager.dto.RegistrationDateDto;
import lecture.registration.professor.dto.LectureDto;
import lecture.registration.professor.dto.ProfessorDto;

import java.util.List;

public interface RegistrationManageRepository {

  RegistrationDateDto registrationDate();

  List<LectureDto> lectureList();

  List<LectureDto> pendingLecture();

  ProfessorDto professor(Long lectureId);

  LectureDto getLecture(Long lectureId);

  void insertRegistrationDate(String startDate, String endDate);

  void updateRegistrationDate(String startDate, String endDate, Long lectureRefistrationPeriodId);

  void lectureOpen(Long lectureId);

}
