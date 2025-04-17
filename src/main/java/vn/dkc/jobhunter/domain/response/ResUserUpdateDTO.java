package vn.dkc.jobhunter.domain.response;

import lombok.Getter;
import lombok.Setter;
import vn.dkc.jobhunter.util.constant.GenderEnum;

import java.time.Instant;

@Getter
@Setter
public class ResUserUpdateDTO {
    private long id;
    private String name;
    private int age;
    private GenderEnum gender;
    private String address;
    private Instant updateAt;
    private CompanyUser company;
    private RoleUser role;

    @Getter
    @Setter
    public static class CompanyUser{
        private long id;
        private String name;
    }

    @Getter
    @Setter
    public static class RoleUser{
        private long id;
        private String name;
    }
}
