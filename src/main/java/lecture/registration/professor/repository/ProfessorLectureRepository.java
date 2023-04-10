package lecture.registration.professor.repository;

import lecture.registration.professor.dto.LectureDto;
import lecture.registration.student.dto.StudentDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProfessorLectureRepository {

  Integer addLecture(LectureDto lectureDto);

  void addProfessorLecture(Long professorId, Long lectureId);

  int lectureQuan(Long professorId);

  List<LectureDto> lectureList(Long professorId);

  List<LectureDto> pendingLecture(Long professorId);

  LectureDto getLecture(Long lectureId);

  List<StudentDto> studentList(@Param("lectureId") Long lectureId);
}
