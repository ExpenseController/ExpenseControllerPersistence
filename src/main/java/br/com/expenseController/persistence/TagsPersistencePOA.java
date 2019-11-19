package br.com.expenseController.persistence;

import br.com.expenseController.model.Tag;
import br.com.expenseController.model.TagHelper;
import br.com.expenseController.model.TagsHelper;
import java.util.Hashtable;
import java.util.List;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.PortableServer.Servant;

public abstract class TagsPersistencePOA extends Servant
        implements TagPersistenceOperations, InvokeHandler {

    // Constructors
    private static final Hashtable METHODS = new Hashtable();
    private static final String[] IDS = {
        "IDL:br/com/expenseController/persistence/TagPersistence:1.0"
    };

    static {
        METHODS.put("insert", new java.lang.Integer(0));
        METHODS.put("update", new java.lang.Integer(1));
        METHODS.put("remove", new java.lang.Integer(2));
        METHODS.put("load", new java.lang.Integer(3));
        METHODS.put("loadAll", new java.lang.Integer(4));
    }

    public OutputStream _invoke(String method, InputStream in, ResponseHandler rh) {
        OutputStream out = null;
        Integer __method = (Integer) METHODS.get(method);
        
        if (__method == null) {
            throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
        }

        switch (__method.intValue()) {
        case 0: // br/com/expenseController/persistence/TagPersistence/save
        {
            List<Tag> tags = TagsHelper.read(in);
            boolean result = false;
            result = this.save(tags);
            out = rh.createReply();
            out.write_boolean(result);
            break;
        }

        case 1: // br/com/expenseController/persistence/TagPersistence/load
        {
            int code = in.read_long();
            Tag result = null;
            result = this.load(code);
            out = rh.createReply();
            TagHelper.write(out, result);
            break;
        }

        case 2: // br/com/expenseController/persistence/TagPersistence/loadAll
        {
            List<Tag> result = null;
            result = this.loadAll();
            out = rh.createReply();
            TagsHelper.write(out, result);
            break;
        }

        default:
            throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
        }

        return out;
    } // _invoke


    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] objectId) {
        return (String[]) IDS.clone();
    }

    public TagPersistence _this() {
        return TagPersistenceHelper.narrow(
                super._this_object());
    }

    public TagPersistence _this(ORB orb) {
        return TagPersistenceHelper.narrow(
                super._this_object(orb));
    }
}