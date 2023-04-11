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

/**
 * 강의 추가 및 조회 기능은 담은 service 클래스
 */
@Service
@RequiredArgsConstructor
public class ProfessorLectureServiceImpl implements ProfessorLectureService {

  /**
   * 강의 추가 및 조회 기능을 담은 repository 클래스 주입
   * 수강 신청 시간을 담은 클래스 주입
   * 수간 신청 마감 날짜와 비교될 현재 날짜 선언
   */
  private final ProfessorLectureRepository professorLectureRepository;
  private final RegistrationManageRepository registrationDateRepository;
  private LocalDate nowDate = LocalDate.now();

  /**
   * 강의 추가
   */
  @Transactional
  @Override
  public void addLecture(LectureDto lectureDto, Long professorId) {

    //이미 등록된 강의 수량 조회
    int lectureQuan = professorLectureRepository.lectureQuan(professorId);

    //수량이 2개 미만일시
    if (lectureQuan < 2) {
      //강의 추가
      professorLectureRepository.addLecture(lectureDto);
      professorLectureRepository.addProfessorLecture(professorId, lectureDto.getLectureId());
    } else {
      //강의 등록 실패 exception 전달
      throw new LectureMax("강의를 추가로 등록할 수 없습니다.");
    }
  }

  //현재 추가된 강의 수량을 가져오는 메서드
  @Override
  public int lectureQuan(Long professorId) {
    return professorLectureRepository.lectureQuan(professorId);
  }

  //등록이 완료된 강의 List
  @Override
  public List<LectureDto> lectureList(Long professorId) {
    return professorLectureRepository.lectureList(professorId);
  }

  //미등록 상태의 강의 List
  @Override
  public List<LectureDto> pendingLecture(Long professorId) {
    return professorLectureRepository.pendingLecture(professorId);
  }

  //선택한 강의의 정보를 꺼내고, 학생 정보 조회 가능 여부를 검증
  @Override
  public LectureDto getLecture(Long lectureId) {

    LocalDate endDate = null;

    /**
     * 마감 날짜를 조회
     * null에 대한 처리를 optional로 변경 필요!!!
     */
    try {
      RegistrationDateDto registrationDateDto = registrationDateRepository.registrationDate();
      endDate = LocalDate.parse(registrationDateDto.getEndDate());
    } catch (NullPointerException e) {
      endDate = nowDate.minusDays(1);
    }

    //마감 날짜보다 이전일시 exception 전달
    if (nowDate.isBefore(endDate)) {
      throw new NotFinishRegistrationException("수강신청이 완료된 후 학생을 조회할 수 있습니다.");
    }

    //강의 정보 반환
    return professorLectureRepository.getLecture(lectureId);
  }

  /**
   * 학생 목록 반환하는 메서드
   */
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
