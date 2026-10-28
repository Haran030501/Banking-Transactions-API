package com.banking.api.service;
import com.banking.api.dto.*;
import com.banking.api.exception.*;
import com.banking.api.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

class BankServiceTest {

    private BankService service;

    @BeforeEach
    void setUp() {
        service = new BankService(new InMemoryAccountRepository(), new InMemoryTransactionRepository());
    }

    private AccountResponse open(String name, String balance) {
        return service.createAccount(new CreateAccountRequest(name, new BigDecimal(balance)));
    }

    private void assertMoney(String expected, BigDecimal actual) {
        assertEquals(0, new BigDecimal(expected).compareTo(actual),
                "expected " + expected + " but was " + actual);
    }

    @Test
    void createAccount_setsInitialBalance() {
        AccountResponse amy = open("Amy", "100");
        assertNotNull(amy.id());
        assertMoney("100", amy.balance());
    }

    @Test
    void transfer_movesMoney() {
        AccountResponse amy = open("Amy", "100");
        AccountResponse bob = open("Bob", "50");
        service.transfer(new TransferRequest(amy.id(), bob.id(), new BigDecimal("30")));
        assertMoney("70", service.getAccount(amy.id()).balance());
        assertMoney("80", service.getAccount(bob.id()).balance());
    }

    @Test
    void transfer_withInsufficientFunds_failsAndChangesNothing() {
        AccountResponse amy = open("Amy", "10");
        AccountResponse bob = open("Bob", "0");
        assertThrows(InsufficientFundsException.class, () -> 
                service.transfer(new TransferRequest(amy.id(), bob.id(), new BigDecimal("50"))));
        assertMoney("10", service.getAccount(amy.id()).balance());
        assertMoney("0", service.getAccount(bob.id()).balance());
        assertTrue(service.getTransactions(amy.id()).isEmpty());
    }

    @Test
    void transfer_toUnknownAccount_fails() {
        AccountResponse amy = open("Amy", "100");
        assertThrows(AccountNotFoundException.class, () ->
                service.transfer(new TransferRequest(amy.id(), "random", BigDecimal.ONE)));
    }

    @Test
    void transfer_toSameAccount_fails() {
        AccountResponse amy = open("Amy", "100");
        assertThrows(InvalidTransferException.class, () ->
                service.transfer(new TransferRequest(amy.id(), amy.id(), BigDecimal.ONE)));
    }

    @Test
    void getTransactions_includesSentAndReceived() {
        AccountResponse amy = open("Amy", "100");
        AccountResponse bob = open("Bob", "100");
        service.transfer(new TransferRequest(amy.id(), bob.id(), BigDecimal.ONE));
        service.transfer(new TransferRequest(bob.id(), amy.id(), BigDecimal.TWO));
        List<TransactionResponse> history = service.getTransactions(amy.id());
        assertEquals(2, history.size());
    }

    @Test
    void getTransactions_forUnknownAccount_fails() {
        assertThrows(AccountNotFoundException.class, () -> service.getTransactions("random"));
    }

    @Test
    void concurrentTransfers_neverOverdraw() throws InterruptedException {
        AccountResponse amy = open("Amy", "100");
        AccountResponse bob = open("Bob", "0");
        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 200; i++) {
            pool.submit(() -> {
                try {
                    service.transfer(new TransferRequest(amy.id(), bob.id(), BigDecimal.ONE));
                } catch (InsufficientFundsException ignored) { }
            });
        }
        pool.shutdown();
        assertTrue(pool.awaitTermination(10, TimeUnit.SECONDS));
        assertMoney("0", service.getAccount(amy.id()).balance());
        assertMoney("100", service.getAccount(bob.id()).balance());
        assertEquals(100, service.getTransactions(amy.id()).size()); 
    }
}
