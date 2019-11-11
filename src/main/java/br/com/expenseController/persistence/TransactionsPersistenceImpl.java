package br.com.expenseController.persistence;

import br.com.expenseController.model.Transaction;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Leandro Ramos (leandroramosmarcelino@hotmail.com)
 */
public class TransactionsPersistenceImpl extends TransactionsPersistencePOA {

    private static final String FILE_DIR = "database/Transactions.json";
    private static final Logger LOG = LoggerFactory.getLogger(TransactionsPersistenceImpl.class);
    private static TransactionsPersistenceImpl INSTANCE;
    private final List<Transaction> transatcions;
    
    private TransactionsPersistenceImpl(List<Transaction> tags) {
        this.transatcions = tags;
    }

    public static final TransactionsPersistenceImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TransactionsPersistenceImpl(readTransactionsFile());
        }
        
        return INSTANCE;
    } 
    
    @Override
    public boolean insert(Transaction transaction) {
        this.transatcions.add(transaction);
        
        if (!save()) {
            this.transatcions.remove(transaction);
            return false;
        }
        
        return true;
    }

    @Override
    public boolean update(Transaction transaction) {
        final Optional<Transaction> optionalTransaction = this.transatcions.stream()
                .filter((t) -> t.getCode() == transaction.getCode()).findFirst();
        
        if (!optionalTransaction.isPresent()) {
            return false;
        }
        
        this.transatcions.remove(optionalTransaction.get());
        
        if (!insert(transaction)) {
            this.transatcions.add(optionalTransaction.get());
            return false;
        }
        
        return true;
    }

    @Override
    public boolean remove(Transaction transaction) {
        final Optional<Transaction> optionalTransaction = this.transatcions.stream()
                .filter((t) -> t.getCode() == transaction.getCode()).findFirst();
        
        if (!optionalTransaction.isPresent()) {
            return false;
        }
        
        this.transatcions.remove(optionalTransaction.get());
        
        if (!save()) {
            this.transatcions.add(optionalTransaction.get());
            return false;
        }
        
        return true;
    }

    private boolean save() {
        try {
            File file = createFile();
            return saveFile(file, getParser().toXML(this.transatcions));
        } catch (Exception e) {
            LOG.error("Failed to save file", e);
            return false;
        }
    }

    private File createFile() {
        File file = new File(FILE_DIR);
        file.mkdirs();

        if (file.exists()) {
            file.delete();
        }
        
        return file;
    }
    
    private boolean saveFile(File file, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        } catch (Exception e) {
            System.out.println("Falha ao gravar arquivo " + file.getAbsolutePath());
            return false;
        }
        
        return true;
    }
    
    @Override
    public Transaction load(int code) {
        try {
            final Optional<Transaction> optionalTransaction = this.transatcions.stream()
                    .filter((t) -> t.getCode() == code).findFirst();
            if (optionalTransaction.isPresent()) {
                return optionalTransaction.get();
            }
        } catch (Exception e) {
            LOG.warn("Failed to load Tag", e);
        }

        return null;
    }

    @Override
    public List<Transaction> loadAll() {
        return this.transatcions;
    }
    
    private static XStream getParser() {
        XStream parser = new XStream(new JettisonMappedXmlDriver());
        parser.setMode(XStream.NO_REFERENCES);
        parser.alias("transatcion", Transaction.class);
        //Clear out existing permissions and set own ones
        parser.addPermission(NoTypePermission.NONE);
        //Allow some basics
        parser.addPermission(NullPermission.NULL);
        parser.addPermission(PrimitiveTypePermission.PRIMITIVES);
        parser.addPermission(AnyTypePermission.ANY);
        parser.allowTypeHierarchy(Collection.class);
        parser.processAnnotations(Transaction.class);
    
        return parser;
    }
    
    private static List<Transaction> readTransactionsFile() {
        File file = new File(FILE_DIR);
        List<Transaction> retornar = new ArrayList<>();
        
        if (file.exists()) {
            try (FileReader fileReader = new FileReader(file)) {
                retornar = (List<Transaction>) getParser().fromXML(fileReader);
                fileReader.close();
            } catch (Exception e) {
                e.printStackTrace();
                LOG.warn("Failed to read tags json files", e);
            }
        }

        return retornar;
    }
}