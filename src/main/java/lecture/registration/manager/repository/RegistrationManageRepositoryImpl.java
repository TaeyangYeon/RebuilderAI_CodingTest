package lecture.registration.manager.repository;

import lecture.registration.manager.dto.RegistrationDateDto;
import lecture.registration.manager.repository.mapper.RegistrationManageMapper;
import lecture.registration.professor.dto.LectureDto;
import lecture.registration.professor.dto.ProfessorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RegistrationManageRepositoryImpl implements RegistrationManageRepository {

  private final RegistrationManageMapper registrationManageMapper;
  private final Map<Long, LectureDto> lectureList = new HashMap<>();

  @Override
  public RegistrationDateDto registrationDate() {
    RegistrationDateDto registrationDate = registrationManageMapper.registrationDate();
    return registrationDate;
  }

  @Override
  public List<LectureDto> lectureList() {

    lectureList.clear();

    List<LectureDto> lectureDtos = registrationManageMapper.lectureList();
    for (LectureDto lectureDto : lectureDtos) {
      lectureList.put(lectureDto.getLectureId(), lectureDto);
    }
    return lectureDtos;
  }

  @Override
  public List<LectureDto> pendingLecture() {
    return registrationManageMapper.pendingLecture();
  }

  @Override
  public ProfessorDto professor(Long lectureId) {
    return registrationManageMapper.professor(lectureId);
  }

  @Override
  public LectureDto getLecture(Long lectureId) {
    return lectureList.get(lectureId);
  }

  @Override
  public void insertRegistrationDate(String startDate, String endDate) {
    registrationManageMapper.insertRegistrationDate(startDate, endDate);
  }

  @Override
  public void updateRegistrationDate(String startDate, String endDate, Long lectureRefistrationPeriodId) {
    registrationManageMapper.updateRegistrationDate(startDate, endDate, lectureRefistrationPeriodId);
  }

  @Override
  public void lectureOpen(Long lectureId) {
    registrationManageMapper.lectureOpen(lectureId);
  }
}
