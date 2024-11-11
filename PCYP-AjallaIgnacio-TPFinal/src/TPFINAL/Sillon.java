package TPFINAL;

import java.util.concurrent.Semaphore;

public class Sillon extends Thread{
    private Barberia barberia; //Representa la barberia
    private Cliente cliente; // se usa para guardar el cliente que esta sentado en el sillon
    private int id; // id del sillon
    private final Semaphore semaphoreSillon; // semaforo binario que controla el uso del sillon

    /*constructor que que recibe los siguientes parametros desde la barberia */
    public Sillon(int id, Barberia barberia){
        this.id = id;
        this.barberia = barberia;
        semaphoreSillon = new Semaphore(1, true);//el semaforo inciara en 1 y tendra la politica FIFO
    }

    /*El cliente se va a sentar en el sillon */
    public void sentarse(Cliente cliente) throws InterruptedException {
        sleep(500);
        this.cliente = cliente;// se guarda referencia del cliente en el sillon
        cliente.setSillon(this);// se guarda referencia del sillon en el cliente
        barberia.getImpresora().imprimir("El Cliente "+cliente.getId()+" se sento en el Sillon "+this.id);     
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Semaphore getSemaphoreSillon() {
        return semaphoreSillon;
    }

    public long getId() {
        return id;
    }

    

}
