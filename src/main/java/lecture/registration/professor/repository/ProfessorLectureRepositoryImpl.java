package lecture.registration.professor.repository;

import lecture.registration.professor.dto.LectureDto;
import lecture.registration.professor.repository.mapper.ProfessorLectureMapper;
import lecture.registration.student.dto.StudentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ProfessorLectureRepositoryImpl implements ProfessorLectureRepository {

  private final ProfessorLectureMapper professorLectureMapper;
  private final Map<Long, LectureDto> lectureList = new HashMap<>();

  @Override
  public Integer addLecture(LectureDto lectureDto) {
    return professorLectureMapper.addLecture(lectureDto);
  }

  @Override
  public void addProfessorLecture(Long professorId, Long lectureId) {
    professorLectureMapper.addProfessorLecture(professorId, lectureId);
  }

  @Override
  public int lectureQuan(Long professorId) {
    return professorLectureMapper.lectureQuan(professorId);
  }

  @Override
  public List<LectureDto> lectureList(Long professorId) {
    List<LectureDto> lectureDtos = professorLectureMapper.lectureList(professorId);
    for (LectureDto lectureDto : lectureDtos) {
      lectureList.put(lectureDto.getLectureId(), lectureDto);
    }
    return lectureDtos;
  }

  @Override
  public List<LectureDto> pendingLecture(Long professorId) {
    return professorLectureMapper.pendingLecture(professorId);
  }

  @Override
  public LectureDto getLecture(Long lectureId) {
    return lectureList.get(lectureId);
  }

  @Override
  public List<StudentDto> studentList(Long lectureId) {
    return professorLectureMapper.studentList(lectureId);
  }

}
