package com.assignment.suprdaily.controller;


import com.assignment.suprdaily.Entity.CanFulfilOrderResponse;
import com.assignment.suprdaily.Entity.Data;
import com.assignment.suprdaily.Entity.OrderRequest;
import com.assignment.suprdaily.Entity.ReserveOrderResponse;
import com.assignment.suprdaily.exception.OrderReservationException;
import com.assignment.suprdaily.service.OrderFulfilmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Validated
public class OrderController {
    private static final String SUCCESS_MESSAGE = "success";
    @Autowired
    private OrderFulfilmentService service;


    @PostMapping("/canFulfilOrder")
    public ResponseEntity<CanFulfilOrderResponse> canFulfilOrder(@RequestBody OrderRequest orderRequest) {
        service.canFulfilOrder(orderRequest);
        CanFulfilOrderResponse response = CanFulfilOrderResponse.builder()
                .canFulfil(service.canFulfilOrder(orderRequest)).build();

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("/reserveOrder")
    public ResponseEntity<ReserveOrderResponse> reserveOrder(@RequestBody OrderRequest orderRequest) {
        try {
            service.reserveOrder(orderRequest);

            ReserveOrderResponse response = ReserveOrderResponse.builder()
                    .data(Data.builder().reserved(true).message(SUCCESS_MESSAGE).build())
                    .code(SUCCESS_MESSAGE).build();

            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

        } catch (OrderReservationException exception) {
            ReserveOrderResponse response = ReserveOrderResponse.builder()
                    .data(Data.builder().reserved(false).message(exception.getMessage()).build())
                    .code(SUCCESS_MESSAGE).build();

            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }
    }
}
