package vn.dkc.jobhunter.controller;

import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.dkc.jobhunter.service.EmailService;
import vn.dkc.jobhunter.service.SubscriberService;
import vn.dkc.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService, SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    @GetMapping("/email")
    @ApiMessage("Send email")
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public String sendEmail() {
//        this.emailService.sendSimpleEmail();
        // Logic to send email

//        this.emailService.sendEmailSync(
//                "daokiencuong04@gmail.com",
//                "Test send email",
//                "<h1><b>Hello World</b></h1>",
//                false,
//                true);
        this.subscriberService.sendSubscribersEmailJobs();

        return "Email sent successfully!";
    }
}
