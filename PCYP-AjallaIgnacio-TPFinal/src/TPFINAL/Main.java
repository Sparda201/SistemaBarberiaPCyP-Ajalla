package TPFINAL;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Main extends Thread {
    public static boolean cerrarBarberia = false;
    public static void main(String[] args) throws InterruptedException {
        int sillas = 5;//cantidad de sillas que tendra la barberia
        Semaphore semaphoreBarberia = new Semaphore(sillas, true); //semaforo de la barberia que controla las sillas
        Barberia barberia = new Barberia(sillas, semaphoreBarberia);// objeto barberia que se le envia el semaforo y la cantida de sillas 
        List<Cliente> clientes = new ArrayList<>();// lista de clientes que se iniciaran como hilos
        //Intancia cada Cliente
        for (int i = 0; i < 9; i++) {
            Cliente cliente = new Cliente(barberia);
            clientes.add(cliente);
        }

        //Primero manda 7 Clientes y luego de 8seg manda otros 2 Clientes
        for (int i = 0; i < clientes.size(); i++) {
            if (i == 7) {
                sleep(15000);
            }
            clientes.get(i).start();
        }


        //La tienda no se cerrara hasta que todos los clientes hayan terminado sus tareas
        while (!cerrarBarberia) {
            boolean band = false;
            //pregunta si todos los clientes creados siguen ejecutandose
            for (Cliente cliente : clientes) {
                if (cliente.isAlive()) {
                    band = true;
                }
            }
            if (!band) {
                cerrarBarberia = true;
                System.out.println("------------ La Barberia va a cerrar -----------");
                //le manda una senial a los barberos para que continuen con su ejecucion, como la variable cerraBarberia es true estos van a terminar sus procesos y la tienda cerrara
                barberia.getBarbero().getSemaphorBarbero().release();
                barberia.getBarbero2().getSemaphorBarbero().release();
            }     
        }
    }
}
