package gc.garcol.accountbalancekafkastreamstateful.transport.rest;

import gc.garcol.accountbalancekafkastreamstateful.core.EBalance;
import gc.garcol.accountbalancekafkastreamstateful.core.SBalanceServiceTrait;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author thaivc
 * @since 2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/balance")
public class BalanceController {

    private final SBalanceServiceTrait balanceService;

    @GetMapping("/{balanceId}")
    public ResponseEntity<EBalance> get(@PathVariable UUID balanceId) {
        return Optional.ofNullable(balanceService.retrieve(balanceId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<EBalance>> get() {
        return Optional.ofNullable(balanceService.allBalances())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<String> create() {
        balanceService.created();
        return ResponseEntity.ok().body("success");
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody DBalanceDeposit balanceDeposit) {
        balanceService.deposit(balanceDeposit.balanceId(), balanceDeposit.amount());
        return ResponseEntity.ok().body("success");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody DBalanceWithdraw balanceDeposit) {
        balanceService.withdraw(balanceDeposit.balanceId(), balanceDeposit.amount());
        return ResponseEntity.ok().body("success");
    }
}
