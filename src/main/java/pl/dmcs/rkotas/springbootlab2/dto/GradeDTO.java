package pl.dmcs.rkotas.springbootlab2.dto;

public class GradeDTO {
    private Long id;
    private Double value;
    private String comment;

    private Long studentId;
    private Long subjectId;
    private String subjectName;

    public GradeDTO() {}

    public GradeDTO(Long id, String subjectName, Double value) {
        this.id = id;
        this.subjectName = subjectName;
        this.value = value;
    }

    public GradeDTO(Long id, Double value, String comment, Long studentId, Long subjectId, String subjectName) {
        this.id = id;
        this.value = value;
        this.comment = comment;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
}
