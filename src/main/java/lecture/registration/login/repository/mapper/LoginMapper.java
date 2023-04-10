package lecture.registration.login.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginMapper {

  Long student(@Param("studentId") Long studentId);

  Long professor(@Param("professorId") Long professorId);

  Long manager(@Param("managerId") Long managerId);

}
