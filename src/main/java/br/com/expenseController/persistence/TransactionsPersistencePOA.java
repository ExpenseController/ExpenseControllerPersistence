package br.com.expenseController.persistence;

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
import org.omg.PortableServer.Servant;

public abstract class TransactionsPersistencePOA extends Servant
        implements TransactionPersistenceOperations, InvokeHandler {

    // Constructors
    private static final Hashtable METHODS = new Hashtable();
    private static final String[] IDS = {
        "IDL:br/com/expenseController/persistence/TransactionPersistence:1.0"
    };

    static {
        METHODS.put("insert", new Integer(0));
        METHODS.put("update", new Integer(1));
        METHODS.put("remove", new Integer(2));
        METHODS.put("load", new Integer(3));
        METHODS.put("loadAll", new Integer(4));
        METHODS.put("loadPeriod", new Integer(5));
    }

    public OutputStream _invoke(String method, InputStream in, ResponseHandler rh) {
        OutputStream out = null;
        Integer __method = (Integer) METHODS.get(method);

        if (__method == null) {
            throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
        }

        switch (__method.intValue()) {
        case 0: // br/com/expenseController/persistence/TransactionPersistence/insert
        {
            Transaction transaction = TransactionHelper.read(in);
            boolean result = false;
            result = this.insert(transaction);
            out = rh.createReply();
            out.write_boolean(result);
            break;
        }

        case 1: // br/com/expenseController/persistence/TransactionPersistence/update
        {
            Transaction transaction = TransactionHelper.read(in);
            boolean result = false;
            result = this.update(transaction);
            out = rh.createReply();
            out.write_boolean(result);
            break;
        }

        case 2: // br/com/expenseController/persistence/TransactionPersistence/remove
        {
            Transaction transaction = TransactionHelper.read(in);
            boolean result = false;
            result = this.remove(transaction);
            out = rh.createReply();
            out.write_boolean(result);
            break;
        }

        case 3: // br/com/expenseController/persistence/TransactionPersistence/load
        {
            int code = in.read_long();
            Transaction result = null;
            result = this.load(code);
            out = rh.createReply();
            TransactionHelper.write(out, result);
            break;
        }

        case 4: // br/com/expenseController/persistence/TransactionPersistence/loadAll
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

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] objectId) {
        return (String[]) IDS.clone();
    }

    public TransactionPersistence _this() {
        return TransactionPersistenceHelper.narrow(
                super._this_object());
    }

    public TransactionPersistence _this(ORB orb) {
        return TransactionPersistenceHelper.narrow(
                super._this_object(orb));
    }
}