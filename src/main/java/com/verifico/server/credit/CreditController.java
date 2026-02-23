package com.verifico.server.credit;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.verifico.server.common.dto.APIResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/credits")
@RequiredArgsConstructor
@Tag(name = "Credit API Endpoints", description = "Endpoints for managing user credit balance and transaction history")
public class CreditController {
  private final CreditService creditService;

  @Operation(summary = "View current credit balance")
  @GetMapping("/balance")
  public ResponseEntity<APIResponse<Integer>> viewBalance() {
    int response = creditService.checkBalance();

    return ResponseEntity.ok()
        .body(new APIResponse<>("Balance successfully fetched!", response));
  }

  @Operation(summary = "View paginated credit transaction history")
  @GetMapping("/transactions")
  public ResponseEntity<APIResponse<Page<CreditTransaction>>> viewTransactions(
      @RequestParam(value = "page", defaultValue = "0") @Positive int page) {

    if (page < 0 || page > 100) {
      page = 0;
    }

    final int size = 15;

    Page<CreditTransaction> response = creditService.getTransactions(page, size);

    return ResponseEntity.ok()
        .body(new APIResponse<>("Successfully Fetched Transactions!", response));
  }

}
