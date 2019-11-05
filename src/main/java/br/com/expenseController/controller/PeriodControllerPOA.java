package br.com.expenseController.controller;

import br.com.expenseController.model.Period;
import br.com.expenseController.model.PeriodHelper;
import br.com.expenseController.model.PeriodsHelper;
import java.util.Hashtable;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;

public abstract class PeriodControllerPOA extends Servant
        implements PeriodControllerOperations, InvokeHandler {

    // Constructors
    private static final Hashtable METHODS = new Hashtable();
    private static final String[] IDS = {
        "IDL:br/com/expenseController/controllers/PeriodController:1.0"
    };

    static {
        METHODS.put("insert", new java.lang.Integer(0));
        METHODS.put("update", new java.lang.Integer(1));
        METHODS.put("remove", new java.lang.Integer(2));
        METHODS.put("load", new java.lang.Integer(3));
        METHODS.put("loadAll", new java.lang.Integer(4));
    }

    public OutputStream _invoke(String $method, InputStream in, ResponseHandler $rh) {
        OutputStream out = null;
        Integer __method = (Integer) METHODS.get($method);
        
        if (__method == null) {
            throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
        }

        switch (__method.intValue()) {
        case 0: // br/com/expenseController/controllers/PeriodController/insert
        {
            Period period = PeriodHelper.read(in);
            boolean $result = false;
            $result = this.insert(period);
            out = $rh.createReply();
            out.write_boolean($result);
            break;
        }
        
        case 1: // br/com/expenseController/controllers/PeriodController/update
        {
            Period period = PeriodHelper.read(in);
            boolean $result = false;
            $result = this.update(period);
            out = $rh.createReply();
            out.write_boolean($result);
            break;
        }
        
        case 2: // br/com/expenseController/controllers/PeriodController/remove
        {
            Period period = PeriodHelper.read(in);
            boolean $result = false;
            $result = this.remove(period);
            out = $rh.createReply();
            out.write_boolean($result);
            break;
        }
        
        case 3: // br/com/expenseController/controllers/PeriodController/load
        {
            int code = in.read_long();
            Period $result = null;
            $result = this.load(code);
            out = $rh.createReply();
            PeriodHelper.write(out, $result);
            break;
        }

        case 4: // br/com/expenseController/controllers/PeriodController/loadAll
        {
            Period $result[] = null;
            $result = this.loadAll();
            out = $rh.createReply();
            PeriodsHelper.write(out, $result);
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

    public PeriodController _this() {
        return PeriodControllerHelper.narrow(super._this_object());
    }

    public PeriodController _this(ORB orb) {
        return PeriodControllerHelper.narrow(super._this_object(orb));
    }
}