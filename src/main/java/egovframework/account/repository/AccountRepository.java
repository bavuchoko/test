package egovframework.account.repository;

import egovframework.account.dto.Account;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository extends EgovAbstractDAO {

    public void save(Account account) {
        insert("account.createAccount", account);
        insert("account.insertRole", account);
    }

    public Account findByUsername(String username) {
        return (Account)select("account.findByUsername", username);
    }
}
