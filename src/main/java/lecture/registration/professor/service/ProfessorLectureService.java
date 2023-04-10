package lecture.registration.professor.service;

import lecture.registration.professor.dto.LectureDto;
import lecture.registration.student.dto.StudentDto;

import java.util.List;

public interface ProfessorLectureService {

  void addLecture(LectureDto lectureDto, Long professorId);

  int lectureQuan(Long professorId);

  List<LectureDto> lectureList(Long professorId);

  List<LectureDto> pendingLecture(Long professorId);

  LectureDto getLecture(Long lectureId);

  List<StudentDto> studentList(Long lectureId);

}
