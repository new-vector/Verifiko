package com.verifico.server.payment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.verifico.server.user.User;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
  Optional<Payment> findByIdempotencyKey(String idempotencyKey);

  Optional<Payment> findByPaymentIntentId(String paymentIntentId);

  Optional<Payment> findByTransactionInitiator(User user);
}
