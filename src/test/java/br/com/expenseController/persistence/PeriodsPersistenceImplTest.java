package br.com.expenseController.persistence;

import br.com.expenseController.model.Period;
import br.com.expenseController.model.Tag;
import br.com.expenseController.model.Transaction;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Leandro Ramos (leandroramosmarcelino@hotmail.com)
 */
public class PeriodsPersistenceImplTest {
    
    @Test
    public void testProccess() {
        PeriodsPersistencePOA periodsPersistence = PeriodsPersistenceImpl.getInstance();
//        Assert.assertTrue("Failed to read file", !tagsPersistence.loadAll().isEmpty());
        Tag tag = new Tag(1, "Gasolina");
        Transaction transactionO = new Transaction(1, "Posto Russi", System.currentTimeMillis(), 
                BigDecimal.valueOf(50), tag);
        Transaction transactionE = new Transaction(2, "Salário Leandro", System.currentTimeMillis(), 
                BigDecimal.valueOf(2000), null);
        List<Transaction> outlay = new ArrayList<>();
        outlay.add(transactionO);
        List<Transaction> earnings = new ArrayList<>();
        earnings.add(transactionE);
        Period period = new Period(1, "Novembro/2019", outlay, earnings);
        Assert.assertTrue(periodsPersistence.insert(period));
        Transaction transactionE2 = new Transaction(2, "Salário Duda", System.currentTimeMillis(), 
                BigDecimal.valueOf(2500), null);
        period.getEarnings().add(transactionE2);
        Assert.assertTrue(periodsPersistence.update(period));
        Assert.assertTrue(periodsPersistence.remove(period));
    }
}
