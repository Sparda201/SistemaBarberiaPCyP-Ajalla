package TPFINAL;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Cliente extends Thread{
    private static int idNext = 1; // id que incrementara cada vez que se cree un nuevo cliente y se le asignara al cliente
    private int id; //id del cliente
    private Barberia barberia; // reprenseta a la barberia
    private final Semaphore semaphoreEstado; //semaforo de condicion que controla la espera del cliente cuando le estan por cortar el pelo
    private Sillon sillon; // representa al sillon que usara el cliente
    
    /*constructor que que recibe los siguientes parametros desde el main */
    public Cliente(Barberia barberia){
       id = idNext;
       idNext++; //se incrementa el id para el proximo cliente
       this.barberia = barberia;
       semaphoreEstado = new Semaphore(0, true);//semaforo cerrado que inicializa en 0 con politica FIFO
    }



    @Override
    public void run() { // Proceso del Cliente
        try {
            barberia.getImpresora().imprimir("El Cliente "+ id+" entro a la Barberia");
            //Intenta adquirir un permiso para sentarse en las sillas de espera
            if (barberia.getSemaphoreBarberia().tryAcquire(0, TimeUnit.SECONDS)) {
                barberia.recibirCliente(id);//Entra en la sala de espera
                barberia.asignarBarbero(id,this);//pide turno para cortarse el pelo   
                
                //Si el barbero esta durmiendo lo despierta
                if (sillon.getId() == 1 && barberia.getBarbero().isDurmiendo()) {
                    barberia.getBarbero().setDurmiendo(false);
                    barberia.getImpresora().imprimir("El Cliente "+ id+" desperto al Barbero "+sillon.getId());
                }else if(sillon.getId() == 2 && barberia.getBarbero2().isDurmiendo()){
                    barberia.getBarbero2().setDurmiendo(false);
                    barberia.getImpresora().imprimir("El Cliente "+ id+" desperto al Barbero "+sillon.getId());
                }

                semaphoreEstado.acquire();//Pongo en espera al cliente para que le corten el pelo

                barberia.getImpresora().imprimir("El Cliente "+id+" se levanto del Sillon "+Long.valueOf(sillon.getId()).intValue());
                sillon.getSemaphoreSillon().release();//devuelve el permiso del Sillon 

                barberia.getImpresora().imprimir("El Cliente "+ id+" se retira de la Barberia");
            }else{// sino quedan permisos para cortarse el pelo se retira del local
                sleep(1000);
                barberia.getImpresora().imprimir("El Cliente "+id+" se retira de la Barberia al no tener asiento");   
            }
           
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

    }


    public Semaphore getSemaphoreEstado() {
        return semaphoreEstado;
    }



    public long getId() {
        return id;
    }



    public Sillon getSillon() {
        return sillon;
    }



    public void setSillon(Sillon sillon) {
        this.sillon = sillon;
    }


    
    



    

}
