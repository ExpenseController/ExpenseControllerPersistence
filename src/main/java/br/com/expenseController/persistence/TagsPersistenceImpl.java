package br.com.expenseController.persistence;

import br.com.expenseController.model.Tag;
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
public class TagsPersistenceImpl extends TagsPersistencePOA {

    private static final String FILE_DIR = "database/Tags.json";
    private static final Logger LOG = LoggerFactory.getLogger(TagsPersistenceImpl.class);
    
    @Override
    public boolean save(List<Tag> tags) {
        try {
            File file = createFile();
            return saveFile(file, getParser().toXML(tags));
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
    public Tag load(int code) {
        try {
            final Optional<Tag> optionalTag = loadAll().stream()
                    .filter((t) -> t.getCode() == code).findFirst();
            if (optionalTag.isPresent()) {
                return optionalTag.get();
            }
        } catch (Exception e) {
            LOG.warn("Failed to load Tag", e);
        }

        return null;
    }

    @Override
    public List<Tag> loadAll() {
        return readTagsFile();
    }
    
    private static XStream getParser() {
        XStream parser = new XStream(new JettisonMappedXmlDriver());
        parser.setMode(XStream.NO_REFERENCES);
        parser.alias("tag", Tag.class);
        //Clear out existing permissions and set own ones
        parser.addPermission(NoTypePermission.NONE);
        //Allow some basics
        parser.addPermission(NullPermission.NULL);
        parser.addPermission(PrimitiveTypePermission.PRIMITIVES);
        parser.addPermission(AnyTypePermission.ANY);
        parser.allowTypeHierarchy(Collection.class);
        parser.processAnnotations(Tag.class);
    
        return parser;
    }
    
    private List<Tag> readTagsFile() {
        File file = new File(FILE_DIR);
        List<Tag> retornar = new ArrayList<>();
        
        if (file.exists()) {
            try (FileReader fileReader = new FileReader(file)) {
                retornar = (List<Tag>) getParser().fromXML(fileReader);
                fileReader.close();
            } catch (Exception e) {
                e.printStackTrace();
                LOG.warn("Failed to read tags json files", e);
            }
        }

        return retornar;
    }
}