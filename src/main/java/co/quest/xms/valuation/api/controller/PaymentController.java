package co.quest.xms.valuation.api.controller;

import co.quest.xms.valuation.api.dto.SubscriptionRequest;
import co.quest.xms.valuation.application.payment.PaymentService;
import co.quest.xms.valuation.domain.model.PaymentIntentDetails;
import co.quest.xms.valuation.domain.model.SubscriptionDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/subscribe")
    public ResponseEntity<PaymentIntentDetails> createSubscription(@RequestBody SubscriptionRequest request) {
        PaymentIntentDetails paymentIntentDetails = paymentService.createSubscription(request.getCustomerId(), request.getPlanId());
        return ResponseEntity.ok(paymentIntentDetails);
    }

    @DeleteMapping("/subscribe/{subscriptionId}")
    public ResponseEntity<SubscriptionDetails> cancelSubscription(@PathVariable String subscriptionId) {
        SubscriptionDetails subscriptionDetails = paymentService.cancelSubscription(subscriptionId);
        return ResponseEntity.ok(subscriptionDetails);
    }
}
