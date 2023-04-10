package lecture.registration.manager.service;

import lecture.registration.manager.dto.RegistrationDateDto;
import lecture.registration.manager.repository.RegistrationManageRepository;
import lecture.registration.professor.dto.LectureDto;
import lecture.registration.professor.dto.ProfessorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationManageServiceImpl implements RegistrationManageService {

  private final RegistrationManageRepository registrationManageRepository;
  private final int minRegistrationPeriodDays = 14;
  private final int maxRegistrationPeriodDays = 28;

  @Override
  public RegistrationDateDto registrationDate() {

    RegistrationDateDto registrationDateDto = registrationManageRepository.registrationDate();
    try {
      registrationDateDto.getStartDate();
    } catch (NullPointerException e) {
      registrationDateDto = new RegistrationDateDto();
    }
    return registrationDateDto;
  }

  @Override
  public List<LectureDto> lectureList() {
    return registrationManageRepository.lectureList();
  }

  @Override
  public List<LectureDto> pendingLecture() {
    return registrationManageRepository.pendingLecture();
  }

  @Override
  public ProfessorDto professor(Long lectureId) {
    return registrationManageRepository.professor(lectureId);
  }

  @Override
  public LectureDto getLecture(Long lectureId) {
    return registrationManageRepository.getLecture(lectureId);
  }

  @Override
  public void insertOrUpdateRegistrationDate(String startDateStr, String endDateStr) {

    LocalDate startDate = LocalDate.parse(startDateStr);
    LocalDate endDate = LocalDate.parse(endDateStr);

    if (endDate.isAfter(startDate.plusDays(minRegistrationPeriodDays - 1)) &&
      endDate.isBefore(startDate.plusDays(maxRegistrationPeriodDays + 1))) {
      try {
        Long lectureRegistrationPeriodId = registrationManageRepository.registrationDate().getLectureRegistrationPeriodId();
        registrationManageRepository.updateRegistrationDate(startDateStr, endDateStr, lectureRegistrationPeriodId);
      } catch (NullPointerException e) {
        registrationManageRepository.insertRegistrationDate(startDateStr, endDateStr);
      }

    } else {
      throw new IllegalArgumentException("기간 선택이 잘못됐습니다.");
    }
  }

  @Override
  public void lectureOpen(Long lectureId) {
    registrationManageRepository.lectureOpen(lectureId);
  }

}
