package lecture.registration.professor.service;

import lecture.registration.manager.dto.RegistrationDateDto;
import lecture.registration.manager.repository.RegistrationManageRepository;
import lecture.registration.professor.dto.LectureDto;
import lecture.registration.professor.repository.ProfessorLectureRepository;
import lecture.registration.student.dto.StudentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorLectureServiceImpl implements ProfessorLectureService {

  private final ProfessorLectureRepository professorLectureRepository;
  private final RegistrationManageRepository registrationDateRepository;
  private LocalDate nowDate = LocalDate.now();

  @Transactional
  @Override
  public void addLecture(LectureDto lectureDto, Long professorId) {

    int lectureQuan = professorLectureRepository.lectureQuan(professorId);

    if (lectureQuan <= 2) {
      professorLectureRepository.addLecture(lectureDto);
      professorLectureRepository.addProfessorLecture(professorId, lectureDto.getLectureId());
    } else {
      throw new LectureMax("강의를 추가로 등록할 수 없습니다.");
    }
  }

  @Override
  public int lectureQuan(Long professorId) {
    return professorLectureRepository.lectureQuan(professorId);
  }

  @Override
  public List<LectureDto> lectureList(Long professorId) {
    return professorLectureRepository.lectureList(professorId);
  }

  @Override
  public List<LectureDto> pendingLecture(Long professorId) {
    return professorLectureRepository.pendingLecture(professorId);
  }

  @Override
  public LectureDto getLecture(Long lectureId) {

    LocalDate endDate = null;

    try {
      RegistrationDateDto registrationDateDto = registrationDateRepository.registrationDate();
      endDate = LocalDate.parse(registrationDateDto.getEndDate());
    } catch (NullPointerException e) {
      endDate = nowDate.minusDays(1);
    }

    if (nowDate.isBefore(endDate)) {
      throw new NotFinishRegistrationException("수강신청이 완료된 후 학생을 조회할 수 있습니다.");
    }

    return professorLectureRepository.getLecture(lectureId);
  }

  @Override
  public List<StudentDto> studentList(Long lectureId) {
    return professorLectureRepository.studentList(lectureId);
  }

  public class NotFinishRegistrationException extends RuntimeException {
    public NotFinishRegistrationException(String message) {
      super(message);
    }
  }
  public class LectureMax extends RuntimeException {
    public LectureMax(String message) {
      super(message);
    }
  }


}
