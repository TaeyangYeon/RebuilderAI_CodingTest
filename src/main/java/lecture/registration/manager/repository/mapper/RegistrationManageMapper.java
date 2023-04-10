package lecture.registration.manager.repository.mapper;

import lecture.registration.manager.dto.RegistrationDateDto;
import lecture.registration.professor.dto.LectureDto;
import lecture.registration.professor.dto.ProfessorDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RegistrationManageMapper {

  RegistrationDateDto registrationDate();

  List<LectureDto> lectureList();

  List<LectureDto> pendingLecture();

  ProfessorDto professor(Long lectureId);

  void insertRegistrationDate(@Param("startDate") String startDate, @Param("endDate") String endDate);

  void updateRegistrationDate(@Param("startDate") String startDate, @Param("endDate") String endDate,
                              @Param("lectureRefistrationPeriodId") Long lectureRefistrationPeriodId);

  void lectureOpen(Long lectureId);

}
