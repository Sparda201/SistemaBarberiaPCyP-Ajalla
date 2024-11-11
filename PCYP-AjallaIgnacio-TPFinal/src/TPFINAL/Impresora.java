package TPFINAL;

import java.util.concurrent.Semaphore;

public class Impresora extends Thread{
    private final Semaphore semaphoreImpresora; //Semaforo que controla el uso de la impresora o la impresion de los msjs

    public Impresora(){
        semaphoreImpresora = new Semaphore(1, true);//semaforo que inicializara en 1 con politica FIFO

    }

    /*Imprime un msj por pantalla */
    public void imprimir(String txt) throws InterruptedException{
        semaphoreImpresora.acquire();//pide permiso para usar la impresora
        System.out.println(txt); //Seccion Critica
        int num = (int) (Math.random() * (200-100)) + 100;
        sleep(num);
        semaphoreImpresora.release();// devuelve el permiso
    }

    /*Metodos Getters */
    public Semaphore getSemaphoreImpresora() {
        return semaphoreImpresora;
    }

    
}
