package TPFINAL;

import java.util.concurrent.Semaphore;

public class Barbero extends Thread{
    private boolean durmiendo; // Indica si el barbero esta durmiendo o no
    private Barberia barberia; // Representa la Barberia
    private final Semaphore semaphoreBarbero; // semaforo que notifica cuando debe empezar a cortar el pelo
    private int id;
    private Sillon sillon;

    /*El contructor recibira los siguientes parametros desde la barberia */
    public Barbero(int id, Barberia barberia, Sillon sillon){
        durmiendo = true;
        this.barberia = barberia;
        this.id = id;
        this.sillon = sillon;
        semaphoreBarbero = new Semaphore(0, true);//semaforo cerrado que incializa en 0 con politica FIFO
    }

    @Override
    public void run() {
        boolean band = false;//boleano que sirve para terminar el bucle del barbero asi terminar su proceso
        try {
            while (band == false) {//bucle para que el barbero siempre este ejecutandose
                semaphoreBarbero.acquire();// pide un permiso para iniciar con el corte, espera la senial del Cliente para continuar
                if (!Main.cerrarBarberia) {//si la barberia no cerro continuara con sus tareas
                    sleep(200);
                    cortarPelo(sillon.getCliente().getId());//corta el pelo
                    sillon.getCliente().getSemaphoreEstado().release(); //notifica al cliente que termino el corte 
                    dormir(); 
                }else{// si la barberia cerro entonces terminar el proceso del barbero
                    band = true;
                }

            }
            
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /* Simula el Corte de Pelo al Cliente */
    public void cortarPelo(Long id) throws InterruptedException{
        int idCliente = Long.valueOf(id).intValue();
        barberia.getImpresora().imprimir("--------El Barbero "+this.id+" esta cortando el pelo al Cliente "+idCliente+"-----------");
        int num = (int) (Math.random() * (2000-1000)) + 1000;
        sleep(num); //simula el tiempo de corte de pelo
        usarSecador(idCliente);
        barberia.getImpresora().imprimir("--------El Barbero "+this.id+" termino de cortar el pelo al Cliente "+idCliente+"----------");
    }

    /* Usa la secadora de Pelo */
    public void usarSecador(int id) throws InterruptedException{
        barberia.getSecador().secar(id, this.id);
    }

    /* Pone a dormir al Barbero */
    public void dormir() throws InterruptedException{
        if (!sillon.getSemaphoreSillon().hasQueuedThreads()) {
            durmiendo = true;
            barberia.getImpresora().imprimir("El Barbero "+this.id+" esta durmiendo");
        }
        
    }

     /*Metodos Getters */
    public boolean isDurmiendo() {
        return durmiendo;
    }

    public void setDurmiendo(boolean durmiendo) {
        this.durmiendo = durmiendo;
    }

    public Semaphore getSemaphorBarbero() {
        return semaphoreBarbero;
    }



    
    
}
