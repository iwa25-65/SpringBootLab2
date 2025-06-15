package pl.dmcs.rkotas.springbootlab2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.dmcs.rkotas.springbootlab2.model.Grade;
import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    // For students to view their own grades
    @Query("SELECT g FROM Grade g WHERE g.student.id = :studentId")
    List<Grade> findByStudentId(@Param("studentId") Long studentId);

    // For teachers to view grades for their subjects
    @Query("SELECT g FROM Grade g WHERE g.subject.teacher.id = :teacherId")
    List<Grade> findByTeacherId(@Param("teacherId") Long teacherId);

    // For specific subject-grade view
    @Query("SELECT g FROM Grade g WHERE g.student.id = :studentId AND g.subject.id = :subjectId")
    List<Grade> findByStudentAndSubject(@Param("studentId") Long studentId,
                                        @Param("subjectId") Long subjectId);
    // Add this to your GradeRepository interface
    @Query("SELECT g FROM Grade g WHERE g.student.id = :studentId AND g.subject.teacher.id = :teacherId")
    List<Grade> findByStudentIdAndTeacherId(@Param("studentId") Long studentId,
                                            @Param("teacherId") Long teacherId);
}
