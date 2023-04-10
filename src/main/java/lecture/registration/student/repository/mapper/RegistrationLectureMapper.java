package lecture.registration.student.repository.mapper;

import lecture.registration.student.dto.EnrollmentDto;
import lecture.registration.student.dto.LectureDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RegistrationLectureMapper {

  List<LectureDto> lectureList();

  int studentLectureCount(@Param("studentId") Long studentId);

  int enrollmentCount(@Param("lectureId") Long lectureId);

  List<LectureDto> registedLectureList(@Param("studentId") Long StrdentId);

  int registedLecture(@Param("lectureId") Long lectureId, @Param("studentId") Long studentId);

  void registration(EnrollmentDto enrollmentDto);

  void deleteEnrollment(@Param("lectureId") Long lectureId, @Param("studentId") Long studentId);


}
