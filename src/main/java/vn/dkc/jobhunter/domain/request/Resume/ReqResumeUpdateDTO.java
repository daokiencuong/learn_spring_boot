package vn.dkc.jobhunter.domain.request.Resume;

import lombok.Getter;
import lombok.Setter;
import vn.dkc.jobhunter.util.constant.StatusResumeEnum;

@Getter
@Setter
public class ReqResumeUpdateDTO {
    private long id;
    private StatusResumeEnum status;
}
