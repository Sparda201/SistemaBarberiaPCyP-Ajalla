package TPFINAL;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Barberia extends Thread{
    private int sillas; //representa la cantidad de sillas de la barberia
    private Barbero barbero; //representa al primero Barbero
    private Barbero barbero2; //representa al segundo Barbero
    private Secador secador; //representa al unico secador de pelo
    private Impresora impresora; //reprensenta al objeto que imprimira los msj
    private Sillon sillon1; //representa el primer sillon donde se cortara el pelo
    private Sillon sillon2; //representa el segundo sillon donde se cortara el pelo
    private final Semaphore semaphoreBarberia; //Semaforo de la Barberia que controla los asientos
    private final Semaphore semaphoreAsignarBarbero; //Semaforo que controla que solo un cliente pueda pedir turno a la vez


    public Barberia(int sillas, Semaphore semaphoreBarberia){
        this.sillas = sillas;
        this.semaphoreBarberia = semaphoreBarberia;
        semaphoreAsignarBarbero = new Semaphore(1, true);
        sillon1 = new Sillon(1,this);
        sillon2 = new Sillon(2,this);
        barbero = new Barbero(1, this,sillon1);
        barbero2 = new Barbero(2, this,sillon2);
        secador = new Secador(this);
        impresora = new Impresora();
        barbero.start();
        barbero2.start();
    }

    /* Imprime un msj cuando el cliente toma un asiento */
    public void recibirCliente(int id) throws InterruptedException{
        impresora.imprimir("El Cliente "+ id+" esta esperando en la sala de espera");
    }
    
    /* Le asigna turno al cliente para alguno de los 2 Barberos */
    public void asignarBarbero(int id, Cliente cliente) throws InterruptedException{
        semaphoreAsignarBarbero.acquire(); //pide permiso para poder pedir turno con alguno barbero, solo un cliente a la vez puede pedir turno
        //Seccion Critica
        int idBarbero = 0; //variable que guardara el id del barbero al que le toco para el corte
        //Intenta conseguir permiso con el barbero 1 para cortarse el pelo
        if (sillon1.getSemaphoreSillon().tryAcquire(0, TimeUnit.SECONDS)) {
            idBarbero = 1;
            sillon1.sentarse(cliente);
            semaphoreAsignarBarbero.release(); //devuelve el permiso para que otro cliente pida turno
        }else if (sillon2.getSemaphoreSillon().tryAcquire(0, TimeUnit.SECONDS)) {//sino tiene permisos el barbero 1 entonces preguntara al barbero 2
            idBarbero = 2;
            sillon2.sentarse(cliente);
            semaphoreAsignarBarbero.release();//devuelve el permiso para que otro cliente pida turno
        }else{// si ambos barberos estan ocupados, preguntara cual tiene menos cola para esperar a ese barbero
            if (sillon1.getSemaphoreSillon().getQueueLength() <= sillon2.getSemaphoreSillon().getQueueLength()) {
                impresora.imprimir("El Cliente "+id+" esta esperando al Barbero 1");
                semaphoreAsignarBarbero.release(); //devuelve el permiso para que otro cliente pida turno
                sillon1.getSemaphoreSillon().acquire(); // pide permiso para cortarse el pelo con el Barbero 1
                sillon1.sentarse(cliente);
                idBarbero = 1;
            }else{
                impresora.imprimir("El Cliente "+id+" esta esperando al Barbero 2");
                semaphoreAsignarBarbero.release(); //devuelve el permiso para que otro cliente pida turno
                sillon2.getSemaphoreSillon().acquire(); // pide permiso para cortarse el pelo con el Barbero 2
                sillon2.sentarse(cliente);
                idBarbero = 2;
            }
        }
        semaphoreBarberia.release();// devuelve el permiso de la silla de la sala de espera
        
        //Dependiendo el barbero que le toco, este procedera a cortarle el pelo
        if (idBarbero == 1) {
            barbero.getSemaphorBarbero().release();
        }else if(idBarbero == 2){
            barbero2.getSemaphorBarbero().release();
        }

    }

    /*Metodos Getters */
    public Barbero getBarbero() {
        return barbero;
    }

    public Barbero getBarbero2() {
        return barbero2;
    }

    public Semaphore getSemaphoreBarberia() {
        return semaphoreBarberia;
    }

    public int getSillas() {
        return sillas;
    }

    public Secador getSecador() {
        return secador;
    }

    public Impresora getImpresora() {
        return impresora;
    }

    public Sillon getSillon1() {
        return sillon1;
    }

    public Sillon getSillon2() {
        return sillon2;
    }

    

}
