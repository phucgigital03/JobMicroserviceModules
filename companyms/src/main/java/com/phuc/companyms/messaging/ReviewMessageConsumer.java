package com.phuc.companyms.messaging;


import com.phuc.companyms.company.CompanyService;
import com.phuc.companyms.dto.ReviewMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ReviewMessageConsumer {
    private final CompanyService companyService;
    public ReviewMessageConsumer(CompanyService companyService) {
        this.companyService = companyService;
    }

    @RabbitListener(queues = "companyRatingQueue")
    public void consumeReviewMessage(ReviewMessage reviewMessage) {
        System.out.println("Received review message with reviewId: " + reviewMessage.getId());
        companyService.updateCompanyRating(reviewMessage);
    }
}
