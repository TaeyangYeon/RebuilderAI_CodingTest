package lecture.registration.manager.service;

import lecture.registration.manager.dto.RegistrationDateDto;
import lecture.registration.manager.repository.RegistrationManageRepository;
import lecture.registration.professor.dto.LectureDto;
import lecture.registration.professor.dto.ProfessorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 관리자 관리 기능을 담은 service 클래스
 */
@Service
@RequiredArgsConstructor
public class RegistrationManageServiceImpl implements RegistrationManageService {

  /**
   * 관리자 관리 기능은 담은 repository 클래스 주입
   * 기간 등록 및 변경시 검증에 필요한 최소 기간과 최대 기간을 선언
   */
  private final RegistrationManageRepository registrationManageRepository;
  private final int minRegistrationPeriodDays = 14;
  private final int maxRegistrationPeriodDays = 28;

  //기간을 가져오는 메서드
  @Override
  public RegistrationDateDto registrationDate() {

    RegistrationDateDto registrationDateDto = registrationManageRepository.registrationDate();

    /**
     * 값이 없을 경우 새로운 클래스를 주입
     * null에 대한 처리를 optional로 변경 필요!!!
     */
    try {
      registrationDateDto.getStartDate();
    } catch (NullPointerException e) {
      registrationDateDto = new RegistrationDateDto();
    }
    return registrationDateDto;
  }

  //등록된  강의 목록을 가져오는 메서드
  @Override
  public List<LectureDto> lectureList() {
    return registrationManageRepository.lectureList();
  }

  //미등록 강의 목록을 가져오는 메서드
  @Override
  public List<LectureDto> pendingLecture() {
    return registrationManageRepository.pendingLecture();
  }

  //해당 강의의 교수 정보를 가져오는 메서드
  @Override
  public ProfessorDto professor(Long lectureId) {
    return registrationManageRepository.professor(lectureId);
  }

  //선택한 강의를 가져오는 메서드
  @Override
  public LectureDto getLecture(Long lectureId) {
    return registrationManageRepository.getLecture(lectureId);
  }

  /**
   * 기간을 저장하는 메서드
   * 기존에 저장된 기간이 없을 시 insert
   * 기존에 저장된 기간이 있을 시 update
   */
  @Override
  public void insertOrUpdateRegistrationDate(String startDateStr, String endDateStr) {

    LocalDate startDate = LocalDate.parse(startDateStr);
    LocalDate endDate = LocalDate.parse(endDateStr);

    //localDate타입으로 선언하여 날짜를 비교
    if (endDate.isAfter(startDate.plusDays(minRegistrationPeriodDays - 1)) &&
      endDate.isBefore(startDate.plusDays(maxRegistrationPeriodDays + 1))) {

      /**
       * 값이 없을 경우 insert 메서드 실행
       * null에 대한 처리를 optional로 변경 필요!!!
       */
      try {
        Long lectureRegistrationPeriodId = registrationManageRepository.registrationDate().getLectureRegistrationPeriodId();
        registrationManageRepository.updateRegistrationDate(startDateStr, endDateStr, lectureRegistrationPeriodId);
      } catch (NullPointerException e) {
        registrationManageRepository.insertRegistrationDate(startDateStr, endDateStr);
      }
    } else {
      //기간 선택 오류시 exception 전달
      throw new IllegalArgumentException("기간 선택이 잘못됐습니다.");
    }
  }

  @Override
  public void lectureOpen(Long lectureId) {
    registrationManageRepository.lectureOpen(lectureId);
  }

}
