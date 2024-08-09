package gc.garcol.accountbalancekafkastreamstateful.core;

import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.UUID;

/**
 * @author thaivc
 * @since 2024
 */
public class SBalanceCalculator implements SBalanceCalculatorTrait {

    private KeyValueStore<UUID, EBalance> balanceStore;
    private ProcessorContext<UUID, EBalance> context;

    @Override
    public void init(ProcessorContext<UUID, EBalance> context) {
        this.balanceStore = context.getStateStore(BalanceConstant.BALANCE_STATE_STORE);
        this.context = context;
    }

    @Override
    public void process(Record<UUID, ETransaction> message) {
        var transaction = message.value();

        var balance = processTransaction(transaction);
        if (balance == null) return;
        storeBalance(balance);
        emitChangedBalance(message, balance);
    }

    private EBalance processTransaction(ETransaction transaction) {
        switch (transaction.getType()) {
            case CREATE -> {
                return EBalance.init();
            }
            case DEPOSIT -> {
                var balance = balanceStore.get(transaction.getBalanceId());
                if (balance == null) return null;
                balance.deposit(transaction.getAmount());
                return balance;
            }
            case WITHDRAW -> {
                var balance = balanceStore.get(transaction.getBalanceId());
                if (balance == null) return null;
                balance.withdraw(transaction.getAmount());
                return balance;
            }
        }
        return null;
    }

    private void storeBalance(EBalance balance) {
        balanceStore.put(balance.getId(), balance);
    }

    private void emitChangedBalance(Record<UUID, ETransaction> message, EBalance balance) {
        context.forward(message
                .withKey(balance.getId())
                .withValue(balance)
        );
    }
}
