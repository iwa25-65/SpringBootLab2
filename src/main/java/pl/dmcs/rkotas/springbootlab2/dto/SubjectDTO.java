package pl.dmcs.rkotas.springbootlab2.dto;

public class SubjectDTO {
    private Long id;
    private String name;
    private String teacherUsername;
    private String code;

    public SubjectDTO(Long id, String name, String teacherUsername, String code) {
        this.id = id;
        this.name = name;
        this.teacherUsername = teacherUsername;
        this.code = code;
    }



    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTeacherUsername() { return teacherUsername; }
    public void setTeacherUsername(String teacherUsername) { this.teacherUsername = teacherUsername; }
}
