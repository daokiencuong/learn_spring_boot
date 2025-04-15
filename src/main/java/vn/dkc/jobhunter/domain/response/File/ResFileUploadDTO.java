package vn.dkc.jobhunter.domain.response.File;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.dkc.jobhunter.util.annotation.ApiMessage;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResFileUploadDTO {
    private String fileName;
    private Instant uploadedAt;
}
