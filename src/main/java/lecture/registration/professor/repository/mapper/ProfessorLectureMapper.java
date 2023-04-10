package lecture.registration.professor.repository.mapper;

import lecture.registration.lecture.dto.lectureDto;
import lecture.registration.professor.dto.LectureDto;
import lecture.registration.student.dto.StudentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProfessorLectureMapper {

  Integer addLecture(LectureDto lectureDto);

  void addProfessorLecture(@Param("professorId") Long professorId, @Param("lectureId") Long lectureId);

  int lectureQuan(@Param("professorId") Long professorId);

  List<LectureDto> lectureList(@Param("professorId") Long professorId);

  List<lectureDto> pendingLecture(@Param("professorId") Long professorId);

  List<StudentDto> studentList(@Param("lectureId") Long studentId);


}
