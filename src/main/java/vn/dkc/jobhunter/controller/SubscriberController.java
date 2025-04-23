package vn.dkc.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.dkc.jobhunter.domain.Subscriber;
import vn.dkc.jobhunter.service.SubscriberService;
import vn.dkc.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;
    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @ApiMessage("Create a subscriber")
    public ResponseEntity<Subscriber> createSubscriber(@RequestBody Subscriber subscriber) {
        Subscriber createdSubscriber = subscriberService.handleCreateSubscriber(subscriber);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubscriber);
    }

    @PutMapping("/subscribers")
    @ApiMessage("Update a subscriber")
    public ResponseEntity<Subscriber> updateSubscriber(@RequestBody Subscriber subscriber) {
        Subscriber updatedSubscriber = subscriberService.handleUpdateSubscriber(subscriber);
        return ResponseEntity.status(HttpStatus.OK).body(updatedSubscriber);
    }
}
