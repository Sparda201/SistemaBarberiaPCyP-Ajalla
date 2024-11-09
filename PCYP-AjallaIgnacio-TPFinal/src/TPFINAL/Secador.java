package TPFINAL;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Secador extends Thread{
    private final Semaphore semaphoreSecador;// semaforo que controlara el uso del secador
    private Barberia barberia; // representa la barberia

    public Secador(Barberia barberia){
        semaphoreSecador = new Semaphore(1, true);
        this.barberia = barberia;
    }

    /* Secara el pelo al cliente */
    public void secar(int idCliente, int idBarbero) throws InterruptedException{
        // El barbero intentara pedir permiso para usar la secadora de pelo
        if (!semaphoreSecador.tryAcquire(0, TimeUnit.SECONDS)) {// sino tiene permiso esperara a que termine de usarse
            barberia.getImpresora().imprimir("=========== El Barbero "+idBarbero+" esta esperando por la secadora de pelo ==========");
            semaphoreSecador.acquire();// al estar ocupado el secador, se pide permiso de nuevo para que este en cola
        }
        //seccion critica
        //simula el uso de la secadora de pelo
        barberia.getImpresora().imprimir("========== El Barbero "+idBarbero+" esta secando el pelo al Cliente "+idCliente+" ============");
        sleep(1000);
        barberia.getImpresora().imprimir("========== El Barbero "+idBarbero+" termino de secar el pelo al Cliente "+idCliente+" ============");
        semaphoreSecador.release(); //devuelve el permiso
    }

    /*Metodos Getters */
    public Semaphore getSemaphoreSecador() {
        return semaphoreSecador;
    }

    

}
