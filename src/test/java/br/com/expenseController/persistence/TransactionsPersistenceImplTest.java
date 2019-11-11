package br.com.expenseController.persistence;

import br.com.expenseController.model.Tag;
import br.com.expenseController.model.Transaction;
import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Leandro Ramos (leandroramosmarcelino@hotmail.com)
 */
public class TransactionsPersistenceImplTest {
    
    @Test
    public void testProccess() {
        TransactionsPersistencePOA transactionsPersistence = TransactionsPersistenceImpl.getInstance();
//        Assert.assertTrue("Failed to read file", !tagsPersistence.loadAll().isEmpty());
        Tag tag = new Tag(1, "Gasolina");
        Transaction transaction = new Transaction(1, "Posto Russi", System.currentTimeMillis(), 
                BigDecimal.valueOf(50), tag);
        Assert.assertTrue(transactionsPersistence.insert(transaction));
        transaction.setValue(BigDecimal.valueOf(100));
        Assert.assertTrue(transactionsPersistence.update(transaction));
        Assert.assertTrue(transactionsPersistence.remove(transaction));
    }
}
