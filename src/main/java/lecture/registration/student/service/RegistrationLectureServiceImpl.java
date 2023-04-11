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

/**
 * 수강 신청하는 기능을 담은 service 클래스
 */
@Service
@RequiredArgsConstructor
public class RegistrationLectureServiceImpl implements RegistrationLectureService{

  /**
   * 수강 신청 기능을 담은 repository 클래스 주입
   * 수강 신청일을 조회하는 기능을 담은 클래스 주입
   * 수강 최대 인원, 최대 동시 접속자, 오늘 날짜 선언
   */
  private final RegistrationLectureRepository registrationLectureRepository;
  private final RegistrationManageRepository registrationManageRepository;
  private final int maxEnrollmentCount = 15;
  private final int MAX_CONCURRENT_REGISTRATIONS = 10;
  private final Semaphore SEMAPHORE = new Semaphore(MAX_CONCURRENT_REGISTRATIONS, true);
  private LocalDate nowDate = LocalDate.now();

  //등록이 완료된 강의 목록
  @Override
  public List<LectureDto> lectureList() {
    return registrationLectureRepository.lectureList();
  }

  //사용자가 신청한 강의 목록
  @Override
  public List<LectureDto> registedLectureList(Long studentId) {
    return registrationLectureRepository.registedLectureList(studentId);
  }

  //수강 신청 가능 여부 (String 타입으로 반환)
  @Override
  public String registrationDate() {
    return registrationAble();
  }

  //수강 신청 가능 여부
  private String registrationAble() {

    //반환할 String 변수와 시작일 종료일 선언
    String registrationDate = "";
    LocalDate startDate = null;
    LocalDate endDate = null;

    /**
     * 수강 신청 기간 조회
     * null에 대한 처리를 optional로 변경 필요!!!
     */
    try {
      RegistrationDateDto registrationDateDto = registrationManageRepository.registrationDate();
      startDate = LocalDate.parse(registrationDateDto.getStartDate());
      endDate = LocalDate.parse(registrationDateDto.getEndDate());
    } catch (NullPointerException e) {
      //설정된 값이 없을 경우 신청 시산을 피해서 임의 값 설정
      startDate = nowDate.plusDays(1);
      endDate = nowDate.minusDays(1);
    }

    //수강 신청을 반영하여 신청 가능 여부 설정
    registrationDate = setValid(startDate, endDate);

    return registrationDate;
  }

  /**
   * 날짜를 기준으로 수강 신청 가능 여부 설정
   */
  private String setValid(LocalDate startDate, LocalDate endDate) {
    String registrationDate;
    if ((nowDate.isAfter(startDate) || nowDate.isEqual(startDate)) &&
        (nowDate.isBefore(endDate) || nowDate.isEqual(endDate))) {
      if (registrationLectureRepository.lectureList().size() >= 5) {
        //기간 안에 있고, 강의가 5개 이상
        registrationDate = "in";
      } else {
        //기간 안에 있지만, 강의가 5개 미만
        registrationDate = "bottom";
      }
    } else {
      //수강 기간이 아님
      registrationDate = "out";
    }
    return registrationDate;
  }

  //선택한 강의의 정보를 조회
  @Override
  public LectureDto getLecture(Long lectureId) {
    return registrationLectureRepository.lecture(lectureId);
  }

  //수강 신청 메서드
  @Override
  public void registration(EnrollmentDto enrollmentDto) {

    //동시 접속 제한 10 설정
    try {
      SEMAPHORE.acquire();
      //이미 신청된 강의 수량 조회
      int registeredLectureCount = registrationLectureRepository.studentLectureCount(enrollmentDto.getStudentId());

      //강의가 3개 이상일시 exception 전달
      if (1 + registeredLectureCount > 3) {
        throw new RegistedLectureLimitException("3개 이상 강의를 신청할 수 없습니다.");
      }

      //중복으로 신청시 exception 전달
      if (registrationLectureRepository.registedLecture(enrollmentDto.getLectureId(), enrollmentDto.getStudentId()) > 0) {
        throw new AleadyRegistedLectureException("이미 등록된 강의 입니다.");
      }

      //해당 강의의 수강 인원 조회
      int enrollmentCount = registrationLectureRepository.enrollmentCount(enrollmentDto.getLectureId());

      //수강 인원이 최대인원 이상일 경우 exception 전달
      if (enrollmentCount >= maxEnrollmentCount) {
        LectureDto lecture = registrationLectureRepository.lecture(enrollmentDto.getLectureId());
        throw new RegistrationLimitReachedException("정원 초과로 신청할 수 없습니다.");
      } else {
        //수강 인원이 최대인원 미만일 경우 수강 신청 진행
        registrationLectureRepository.registration(enrollmentDto);
      }
    } catch (InterruptedException e) {
      //접속 제한 초과시 대기
      Thread.currentThread().interrupt();
    } finally {
      SEMAPHORE.release();
    }
  }

  //수강 철회 메서드
  @Override
  public void deleteEnrollment(Long lectureId, Long studentId) {

    //철회 가능일 조회
    LocalDate endDate = deleteAbleDate();

    //오늘 날짜와 비교하여 철회 가능시 철회
    if (nowDate.isBefore(endDate)) {
      registrationLectureRepository.deleteEnrollment(lectureId, studentId);
    } else {
      //철회 가능 기간 지났을 시에는 exception 전달
      throw new NoDeleteEnrollmentException("수강 정정 기간이 지났습니다.");
    }

  }

  //최소 가능일 조회
  private LocalDate deleteAbleDate() {
    RegistrationDateDto registrationDateDto = registrationManageRepository.registrationDate();
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
