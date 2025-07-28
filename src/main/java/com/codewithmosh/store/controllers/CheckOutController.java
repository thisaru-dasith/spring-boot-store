package com.codewithmosh.store.controllers;

import com.codewithmosh.exeptions.PaymentException;
import com.codewithmosh.store.dto.CheckOutRequestDto;
import com.codewithmosh.store.dto.CheckOutResponseDto;
import com.codewithmosh.store.dto.ErrorDto;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.exeptions.CartItemNotFound;
import com.codewithmosh.store.exeptions.CartItemsIsEmpty;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import com.codewithmosh.store.service.CheckOutService;
import com.codewithmosh.store.service.OrderService;
import com.codewithmosh.store.util.Status;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckOutController {


    private final CheckOutService checkOutService;
    private final OrderRepository orderRepository;

    @Value("${STRIPE_WEBHOOK_SECRET_KEY}")
    private String secretKey;



    @PostMapping()
    public ResponseEntity<?> checkout(@RequestBody CheckOutRequestDto checkOutRequestDto){
            CheckOutResponseDto checkOutResponseDto = checkOutService.checkOut(checkOutRequestDto);
            return ResponseEntity.ok(checkOutResponseDto);

    }

    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebHook(
            @RequestParam("Stripe-Signature") String signature,
            @RequestBody String payload

    ){
        try {
            Event event = Webhook.constructEvent(payload, signature, secretKey);
            System.out.println(event.getType());

            var stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);

            switch (event.getType()) {
                case "payment_intent.succeeded" -> {
                    var paymentIntent = (PaymentIntent) stripeObject;
                    if (paymentIntent != null){
                        String orderId = paymentIntent.getMetadata().get("order_id");
                        Order order = orderRepository.findById(Long.valueOf(orderId)).orElseThrow();
                        order.setStatus(Status.PAID);
                        orderRepository.save(order);
                    }

                    // Update order status (PAID)
                }
                case "payment_intent.failed" -> {
                    // Update order status (FAILED)
                }
            }
        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorDto> handlePaymentException(PaymentException ex){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Error creating the checkout session"));
    }


    @ExceptionHandler({CartItemNotFound.class, CartItemsIsEmpty.class})
    public ResponseEntity<ErrorDto> handleException(Exception ex){
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }
}
