package br.com.expenseController.persistence;

import br.com.expenseController.model.Tag;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Leandro Ramos (leandroramosmarcelino@hotmail.com)
 */
public class TagsPersistenceImplTest {
    
    /**
     * Test of insert method, of class TagsPersistenceImpl.
     */
    @Test
    public void testProccess() {
        TagsPersistencePOA tagsPersistence = TagsPersistenceImpl.getInstance();
//        Assert.assertTrue("Failed to read file", !tagsPersistence.loadAll().isEmpty());
        Tag tag = new Tag(1, "Gasolina");
        Assert.assertTrue(tagsPersistence.insert(tag));
        tag.setDescription("Mercado");
        Assert.assertTrue(tagsPersistence.update(tag));
        Assert.assertTrue(tagsPersistence.remove(tag));
    }
}
