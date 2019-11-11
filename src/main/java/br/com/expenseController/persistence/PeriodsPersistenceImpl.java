package br.com.expenseController.persistence;

import br.com.expenseController.model.Period;
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
public class PeriodsPersistenceImpl extends PeriodsPersistencePOA {

    private static final String FILE_DIR = "database/Periods.json";
    private static final Logger LOG = LoggerFactory.getLogger(PeriodsPersistenceImpl.class);
    private static PeriodsPersistenceImpl INSTANCE;
    private final List<Period> periods;
    
    private PeriodsPersistenceImpl(List<Period> periods) {
        this.periods = periods;
    }
    
    public static final PeriodsPersistenceImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PeriodsPersistenceImpl(readPeriodsFile());
        }
        
        return INSTANCE;
    } 
    
    @Override
    public boolean insert(Period period) {
        this.periods.add(period);
        
        if (!save()) {
            this.periods.remove(period);
            return false;
        }
        
        return true;
    }

    @Override
    public boolean update(Period period) {
        final Optional<Period> optionalPeriod = this.periods.stream()
                .filter((t) -> t.getCode() == period.getCode()).findFirst();
        
        if (!optionalPeriod.isPresent()) {
            return false;
        }
        
        this.periods.remove(optionalPeriod.get());
        
        if (!insert(period)) {
            this.periods.add(optionalPeriod.get());
            return false;
        }
        
        return true;
    }

    @Override
    public boolean remove(Period period) {
        final Optional<Period> optionalPeriod = this.periods.stream()
                .filter((t) -> t.getCode() == period.getCode()).findFirst();
        
        if (!optionalPeriod.isPresent()) {
            return false;
        }
        
        this.periods.remove(optionalPeriod.get());
        
        if (!save()) {
            this.periods.add(optionalPeriod.get());
            return false;
        }
        
        return true;
    }

    private boolean save() {
        try {
            File file = createFile();
            return saveFile(file, getParser().toXML(this.periods));
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
    public Period load(int code) {
        try {
            final Optional<Period> optionalPeriod = this.periods.stream()
                    .filter((t) -> t.getCode() == code).findFirst();
            if (optionalPeriod.isPresent()) {
                return optionalPeriod.get();
            }
        } catch (Exception e) {
            LOG.warn("Failed to load Period", e);
        }

        return null;
    }

    @Override
    public List<Period> loadAll() {
        return this.periods;
    }
    
    private static XStream getParser() {
        XStream parser = new XStream(new JettisonMappedXmlDriver());
        parser.setMode(XStream.NO_REFERENCES);
        parser.alias("period", Period.class);
        //Clear out existing permissions and set own ones
        parser.addPermission(NoTypePermission.NONE);
        //Allow some basics
        parser.addPermission(NullPermission.NULL);
        parser.addPermission(PrimitiveTypePermission.PRIMITIVES);
        parser.addPermission(AnyTypePermission.ANY);
        parser.allowTypeHierarchy(Collection.class);
        parser.processAnnotations(Period.class);
    
        return parser;
    }
    
    private static List<Period> readPeriodsFile() {
        File file = new File(FILE_DIR);
        List<Period> retornar = new ArrayList<>();
        
        if (file.exists()) {
            try (FileReader fileReader = new FileReader(file)) {
                retornar = (List<Period>) getParser().fromXML(fileReader);
                fileReader.close();
            } catch (Exception e) {
                e.printStackTrace();
                LOG.warn("Failed to read periods json files", e);
            }
        }

        return retornar;
    }
}