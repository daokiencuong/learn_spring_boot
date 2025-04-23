package vn.dkc.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.dkc.jobhunter.service.EmailService;
import vn.dkc.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/email")
    @ApiMessage("Send email")
    public String sendEmail() {
//        this.emailService.sendSimpleEmail();
        // Logic to send email

//        this.emailService.sendEmailSync(
//                "daokiencuong04@gmail.com",
//                "Test send email",
//                "<h1><b>Hello World</b></h1>",
//                false,
//                true);

        this.emailService.sendEmailFromTemplateSync(
                "daokiencuong04@gmail.com",
                "Thanks for signing up!",
                "job"
        );
        return "Email sent successfully!";
    }
}
