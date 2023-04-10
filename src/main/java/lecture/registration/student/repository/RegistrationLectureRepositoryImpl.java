package lecture.registration.student.repository;

import lecture.registration.student.dto.EnrollmentDto;
import lecture.registration.student.dto.LectureDto;
import lecture.registration.student.repository.mapper.RegistrationLectureMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RegistrationLectureRepositoryImpl implements RegistrationLectureRepository {

  private final RegistrationLectureMapper registrationLectureMapper;
  private final Map<Long, LectureDto> lectureList = new HashMap<>();

  @Override
  public List<LectureDto> lectureList() {
    lectureList.clear();

    List<LectureDto> lectureDtos = registrationLectureMapper.lectureList();
    for (LectureDto lectureDto : lectureDtos) {
      lectureList.put(lectureDto.getLectureId(), lectureDto);
    }
    return lectureDtos;
  }

  @Override
  public int studentLectureCount(Long studentId) {
    return registrationLectureMapper.studentLectureCount(studentId);
  }

  @Override
  public LectureDto lecture(Long lectureId) {
    return lectureList.get(lectureId);
  }

  @Override
  public int enrollmentCount(Long lectureId) {
    return registrationLectureMapper.enrollmentCount(lectureId);
  }

  @Override
  public List<LectureDto> registedLectureList(Long studentId) {
    lectureList.clear();

    List<LectureDto> lectureDtos = registrationLectureMapper.registedLectureList(studentId);
    for (LectureDto lectureDto : lectureDtos) {
      lectureList.put(lectureDto.getLectureId(), lectureDto);
    }

    return lectureDtos;
  }

  @Override
  public int registedLecture(Long lectureId, Long studentId) {
    return registrationLectureMapper.registedLecture(lectureId, studentId);
  }

  @Override
  public void registration(EnrollmentDto enrollmentDto) {
    registrationLectureMapper.registration(enrollmentDto);
  }

  @Override
  public void deleteEnrollment(Long lectureId, Long studentId) {
    registrationLectureMapper.deleteEnrollment(lectureId, studentId);
  }
}
