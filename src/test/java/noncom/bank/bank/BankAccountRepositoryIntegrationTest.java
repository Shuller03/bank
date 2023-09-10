package noncom.bank.bank;

import noncom.bank.bank.model.BankAccount;
import noncom.bank.bank.repository.BankAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Sql(scripts = {"/schema.sql", "/data.sql"}, config = @SqlConfig(encoding = "utf-8"))
public class BankAccountRepositoryIntegrationTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Test
    public void testDatabaseInitialization() {

        long count = bankAccountRepository.count();
        //stub
        assertEquals(10, count);
    }

    @Test
    public void testInsertData() {

        List<BankAccount> accounts = bankAccountRepository.findAll();
        //stub
        assertEquals(13, accounts.size());

    }

    @Test
    public void testCustomSqlScriptExecution() {

        String customSql = "INSERT INTO bank_account (name, pin) VALUES (?, ?)";
        jdbcTemplate.update(customSql, "CustomUser", 9999);

        List<BankAccount> accounts = bankAccountRepository.findAll();
        //stub
        assertEquals(7, accounts.size());
    }
}
