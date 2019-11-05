package br.com.expenseController.controller;

import br.com.expenseController.model.Tag;
import br.com.expenseController.model.TagHelper;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;

public abstract class TagControllerPOA extends Servant
        implements TagControllerOperations, InvokeHandler {

    // Constructors
    private static final java.util.Hashtable METHODS = new java.util.Hashtable();
    private static final String[] IDS = {
        "IDL:br/com/expenseController/controllers/TagController:1.0"
    };

    static {
        METHODS.put("insert", new java.lang.Integer(0));
        METHODS.put("update", new java.lang.Integer(1));
        METHODS.put("remove", new java.lang.Integer(2));
        METHODS.put("load", new java.lang.Integer(3));
    }

    public OutputStream _invoke(String method,
            InputStream in,
            ResponseHandler rh) {
        OutputStream out = null;
        Integer __method = (Integer) METHODS.get(method);
        
        if (__method == null) {
            throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
        }

        switch (__method.intValue()) {
        case 0: // br/com/expenseController/controllers/TagController/insert
        {
            Tag tag = TagHelper.read(in);
            boolean result = false;
            result = this.insert(tag);
            out = rh.createReply();
            out.write_boolean(result);
            break;
        }

        case 1: // br/com/expenseController/controllers/TagController/update
        {
            Tag tag = TagHelper.read(in);
            boolean result = false;
            result = this.update(tag);
            out = rh.createReply();
            out.write_boolean(result);
            break;
        }

        case 2: // br/com/expenseController/controllers/TagController/remove
        {
            Tag tag = TagHelper.read(in);
            boolean result = false;
            result = this.remove(tag);
            out = rh.createReply();
            out.write_boolean(result);
            break;
        }

        case 3: // br/com/expenseController/controllers/TagController/load
        {
            int code = in.read_long();
            Tag result = null;
            result = this.load(code);
            out = rh.createReply();
            TagHelper.write(out, result);
            break;
        }

        default:
            throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
        }

        return out;
    } // _invoke

    public String[] _all_interfaces(POA poa, byte[] objectId) {
        return (String[]) IDS.clone();
    }

    public TagController _this() {
        return TagControllerHelper.narrow(
                super._this_object());
    }

    public TagController _this(ORB orb) {
        return TagControllerHelper.narrow(
                super._this_object(orb));
    }
}