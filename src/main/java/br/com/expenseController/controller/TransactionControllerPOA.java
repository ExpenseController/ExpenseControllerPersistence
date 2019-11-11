package br.com.expenseController.controller;

import br.com.expenseController.model.Transaction;
import br.com.expenseController.model.TransactionHelper;
import br.com.expenseController.model.TransactionsHelper;
import java.util.Hashtable;
import java.util.List;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;

public abstract class TransactionControllerPOA extends Servant
        implements TransactionControllerOperations, InvokeHandler {

    // Constructors
    private static final Hashtable METHODS = new Hashtable();
    private static final String[] IDS = {
        "IDL:br/com/expenseController/controllers/TransactionController:1.0"
    };

    static {
        METHODS.put("insert", new java.lang.Integer(0));
        METHODS.put("update", new java.lang.Integer(1));
        METHODS.put("remove", new java.lang.Integer(2));
        METHODS.put("load", new java.lang.Integer(3));
        METHODS.put("loadAll", new java.lang.Integer(4));
        METHODS.put("loadPeriod", new java.lang.Integer(5));
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
        case 0: // br/com/expenseController/controllers/TransactionController/insert
        {
            Transaction transaction = TransactionHelper.read(in);
            boolean result = false;
            result = this.insert(transaction);
            out = rh.createReply();
            out.write_boolean(result);
            break;
        }

        case 1: // br/com/expenseController/controllers/TransactionController/update
        {
            Transaction transaction = TransactionHelper.read(in);
            boolean result = false;
            result = this.update(transaction);
            out = rh.createReply();
            out.write_boolean(result);
            break;
        }

        case 2: // br/com/expenseController/controllers/TransactionController/remove
        {
            Transaction transaction = TransactionHelper.read(in);
            boolean result = false;
            result = this.remove(transaction);
            out = rh.createReply();
            out.write_boolean(result);
            break;
        }

        case 3: // br/com/expenseController/controllers/TransactionController/load
        {
            int code = in.read_long();
            Transaction result = null;
            result = this.load(code);
            out = rh.createReply();
            TransactionHelper.write(out, result);
            break;
        }

        case 4: // br/com/expenseController/controllers/TransactionController/loadAll
        {
            List<Transaction> result = null;
            result = this.loadAll();
            out = rh.createReply();
            TransactionsHelper.write(out, result);
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

    public TransactionController _this() {
        return TransactionControllerHelper.narrow(
                super._this_object());
    }

    public TransactionController _this(ORB orb) {
        return TransactionControllerHelper.narrow(
                super._this_object(orb));
    }

}