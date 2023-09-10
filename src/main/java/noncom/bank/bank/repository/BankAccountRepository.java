package noncom.bank.bank.repository;

import noncom.bank.bank.model.BankAccount;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;

@Repository
public class BankAccountRepository implements IBankAccountRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BankAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BankAccount createBankAccount(@NotNull BankAccount bankAccount) {
        String sql = "INSERT INTO bank_account (name, pin) VALUES (?, ?)";
        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(
                sql,
                Types.VARCHAR, Types.INTEGER
        );
        factory.setGeneratedKeysColumnNames("id");
        factory.setReturnGeneratedKeys(true);
        PreparedStatementCreator psc = factory.newPreparedStatementCreator(
                List.of(
                        bankAccount.getName(),
                        bankAccount.getPin()
                )
        );

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);

        bankAccount.setId(getId(bankAccount.getName(), bankAccount.getPin()));

        return bankAccount;
    }

    @Override
    public List<BankAccount> findAll() {
        String sql = "SELECT * FROM bank_account";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(BankAccount.class));
    }

    @Override
    public int getId(String name, int pin) {
        String sql = "SELECT id FROM bank_account WHERE name = ? AND pin = ?";

        return jdbcTemplate.queryForList(sql, Integer.class, name, pin).get(0);
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM bank_account";

        return jdbcTemplate.queryForList(sql, Long.class).get(0);
    }

    @Override
    public void deposit(int accountId, double amount) {
        String sql = "UPDATE bank_account SET balance = balance + ? WHERE id = ?";
        jdbcTemplate.update(sql, amount, accountId);
    }

    @Override
    public void withdraw(int accountId, double amount) {
        String sql = "UPDATE bank_account SET balance = balance - ? WHERE id = ?";
        jdbcTemplate.update(sql, amount, accountId);
    }

    @Override
    public void transfer(int sourceAccountId, int targetAccountId, double amount) {
        withdraw(sourceAccountId, amount);
        deposit(targetAccountId, amount);
    }

    @Override
    public boolean checkAccountExists(String name){
        String sql = "SELECT COUNT(*) FROM bank_account WHERE name = ?";

        return jdbcTemplate.queryForList(sql, Integer.class, name).get(0) > 0;
    }

    @Override
    public boolean checkPinForAccount(int accountId, int pin) {
        String sql = "SELECT COUNT(*) FROM bank_account WHERE id = ? AND pin = ?";

        return jdbcTemplate.queryForList(sql, Integer.class, accountId, pin).get(0) > 0;
    }

    @Override
    public boolean checkBalance(int accountId, double amount) {
        String sql = "SELECT balance FROM bank_account WHERE id = ?";

        return jdbcTemplate.queryForList(sql, Double.class, accountId).get(0) >= amount;
    }

    @Override
    public boolean checkTargetId(int targetId) {
        String sql = "SELECT COUNT(*) FROM bank_account WHERE id = ?";

        return jdbcTemplate.queryForList(sql, Integer.class, targetId).get(0) > 0;
    }
}