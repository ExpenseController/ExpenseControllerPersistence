package br.com.expenseController;

import br.com.expenseController.controller.TagController;
import br.com.expenseController.controller.TagControllerHelper;
import br.com.expenseController.persistence.PeriodPersistence;
import br.com.expenseController.persistence.PeriodPersistenceHelper;
import br.com.expenseController.persistence.PeriodsPersistenceImpl;
import br.com.expenseController.persistence.PeriodsPersistencePOA;
import br.com.expenseController.persistence.TagsPersistenceImpl;
import br.com.expenseController.persistence.TagsPersistencePOA;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class Main {
    
    private static Main INSTANCE;
    private ORB orb;
    
    private Main() {
        try {
            java.util.Properties props = System.getProperties();
            props.put("org.omg.CORBA.ORBInitialPort", "1050");
            props.put("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
            props.put("com.sun.CORBA.giop.ORBGIOPVersion", "1.0");
            //Cria e inicializa o ORB
            orb = ORB.init(new String[]{}, props);
        } catch (Exception e) {
            System.err.println("ERRO: " + e);
            e.printStackTrace(System.out);
        }
    }
    
    public static Main getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Main();
        }
        
        return INSTANCE;
    }
    
    public void run() {
        // Aguarda chamadas dos clientes
        System.out.println("Server waiting connections ....");
        orb.run();
    }
    
    private void initPersistences() {
        try {
            // Ativa o POA
            POA rootpoa = POAHelper.narrow(this.orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
            // Obtém uma referência para o servidor de nomes
            org.omg.CORBA.Object objRef = this.orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            
            // Cria a implementação e registra no ORB
            TagsPersistencePOA impl = new TagsPersistenceImpl();
            // Pega a referência do servidor
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(impl);
            TagController href = TagControllerHelper.narrow(ref);
            // Registra o servidor no servico de nomes
            NameComponent path[] = ncRef.to_name("TagsPersistence");
            ncRef.rebind(path, href);
            
            // Cria a implementação e registra no ORB
            PeriodsPersistencePOA produtoImpl = new PeriodsPersistenceImpl();
            // Pega a referência do servidor
            ref = rootpoa.servant_to_reference(produtoImpl);
            PeriodPersistence produtoHref = PeriodPersistenceHelper.narrow(ref);
            // Registra o servidor no servico de nomes
            path = ncRef.to_name("PeriodsPersistence");
            ncRef.rebind(path, produtoHref);
//            
//            // Cria a implementação e registra no ORB
//            ControllerOrcamentoImpl orcamentoImpl = new ControllerOrcamentoImpl();
//            // Pega a referência do servidor
//            ref = rootpoa.servant_to_reference(orcamentoImpl);
//            ControllerOrcamento orcamentoHref = ControllerOrcamentoHelper.narrow(ref);
//            // Registra o servidor no servico de nomes
//            name = "ControllerOrcamento";
//            path = ncRef.to_name(name);
//            ncRef.rebind(path, orcamentoHref);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
    
    public static void main(String args[]) {
        Main main = Main.getInstance();
        main.initPersistences();
        main.run();
        System.out.println("Ending Server.");
    }

    public ORB getOrb() {
        return orb;
    }
}