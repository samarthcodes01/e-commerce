package com.samarth.product.customer;

public record CustomerResponse(
         String id,
         String firstname,
         String lastname,
         String email,
         Address address
) {
}
