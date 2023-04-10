package lecture.registration.student.service;

import lecture.registration.manager.dto.RegistrationDateDto;
import lecture.registration.manager.repository.RegistrationManageRepository;
import lecture.registration.student.dto.EnrollmentDto;
import lecture.registration.student.dto.LectureDto;
import lecture.registration.student.repository.RegistrationLectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Semaphore;

@Service
@RequiredArgsConstructor
public class RegistrationLectureServiceImpl implements RegistrationLectureService{

  private final RegistrationLectureRepository registrationLectureRepository;
  private final RegistrationManageRepository registrationDateRepository;
  private final int maxEnrollmentCount = 15;
  private final int MAX_CONCURRENT_REGISTRATIONS = 10;
  private final Semaphore SEMAPHORE = new Semaphore(MAX_CONCURRENT_REGISTRATIONS, true);
  private LocalDate nowDate = LocalDate.now();

  @Override
  public List<LectureDto> lectureList() {
    return registrationLectureRepository.lectureList();
  }

  @Override
  public List<LectureDto> registedLectureList(Long studentId) {
    return registrationLectureRepository.registedLectureList(studentId);
  }

  @Override
  public String registrationDate() {
    return registrationAble();
  }

  private String registrationAble() {

    String registrationDate = "";
    LocalDate startDate = null;
    LocalDate endDate = null;

    try {
      RegistrationDateDto registrationDateDto = registrationDateRepository.registrationDate();
      startDate = LocalDate.parse(registrationDateDto.getStartDate());
      endDate = LocalDate.parse(registrationDateDto.getEndDate());
    } catch (NullPointerException e) {
      startDate = nowDate.plusDays(1);
      endDate = nowDate.minusDays(1);
    }

    registrationDate = setValid(startDate, endDate);

    return registrationDate;
  }

  private String setValid(LocalDate startDate, LocalDate endDate) {
    String registrationDate;
    if ((nowDate.isAfter(startDate) || nowDate.isEqual(startDate)) &&
        (nowDate.isBefore(endDate) || nowDate.isEqual(endDate))) {
      if (registrationLectureRepository.lectureList().size() >= 5) {
        registrationDate = "in";
      } else {
        registrationDate = "bottom";
      }
    } else {
      registrationDate = "out";
    }
    return registrationDate;
  }

  @Override
  public LectureDto getLecture(Long lectureId) {
    return registrationLectureRepository.lecture(lectureId);
  }

  @Override
  public void registration(EnrollmentDto enrollmentDto) {

    try {
      SEMAPHORE.acquire();
      int registeredLectureCount = registrationLectureRepository.studentLectureCount(enrollmentDto.getStudentId());

      if (1 + registeredLectureCount > 3) {
        throw new RegistedLectureLimitException("3개 이상 강의를 신청할 수 없습니다.");
      }

      if (registrationLectureRepository.registedLecture(enrollmentDto.getLectureId(), enrollmentDto.getStudentId()) > 0) {
        throw new AleadyRegistedLectureException("이미 등록된 강의 입니다.");
      }

      int enrollmentCount = registrationLectureRepository.enrollmentCount(enrollmentDto.getLectureId());

      if (enrollmentCount >= maxEnrollmentCount) {
        LectureDto lecture = registrationLectureRepository.lecture(enrollmentDto.getLectureId());
        throw new RegistrationLimitReachedException("정원 초과로 신청할 수 없습니다.");
      } else {
        registrationLectureRepository.registration(enrollmentDto);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      SEMAPHORE.release();
    }
  }

  @Override
  public void deleteEnrollment(Long lectureId, Long studentId) {

    LocalDate endDate = deleteAbleDate();

    if (nowDate.isBefore(endDate)) {
      registrationLectureRepository.deleteEnrollment(lectureId, studentId);
    } else {
      throw new NoDeleteEnrollmentException("수강 정정 기간이 지났습니다.");
    }

  }

  private LocalDate deleteAbleDate() {
    RegistrationDateDto registrationDateDto = registrationDateRepository.registrationDate();
    LocalDate endDate = LocalDate.parse(registrationDateDto.getEndDate()).minusDays(3);
    return endDate;
  }


  public class AleadyRegistedLectureException extends RuntimeException {
    public AleadyRegistedLectureException(String message) {
      super(message);
    }
  }
  public class NoDeleteEnrollmentException extends RuntimeException {
    public NoDeleteEnrollmentException(String message) { super(message); }
  }
  public class RegistedLectureLimitException extends RuntimeException {
    public RegistedLectureLimitException(String message) {
      super(message);
    }
  }
  public class RegistrationLimitReachedException extends RuntimeException {
    public RegistrationLimitReachedException(String message) {
      super(message);
    }
  }
}
