package TPFINAL;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Main extends Thread {
    public static boolean cerrarBarberia = false;
    public static void main(String[] args) throws InterruptedException {
        int sillas = 5;
        Semaphore semaphoreBarberia = new Semaphore(sillas, true);
        Barberia barberia = new Barberia(sillas, semaphoreBarberia);
        List<Cliente> clientes = new ArrayList<>();
        //Intancia cada Cliente
        for (int i = 0; i < 10; i++) {
            Cliente cliente = new Cliente(barberia);
            clientes.add(cliente);
        }

        //Primero manda 7 Clientes y luego de 8seg manda otros 3 Clientes
        for (int i = 0; i < clientes.size(); i++) {
            if (i == 7) {
                sleep(8000);
            }
            clientes.get(i).start();
        }


        // Manda 5 Clientes uno por uno cada cierto tiempo
        for (int i = 0; i < 5; i++) {
            Cliente cliente = new Cliente(barberia);
            cliente.start();
            int num = (int) (Math.random() * (3000 - 1000)) + 1000;
            sleep(num);
        
        }

        while (!cerrarBarberia) {
            //System.out.println(barberia.getSemaphoreBarberia().availablePermits());
            //System.out.println("1");
            //System.out.println(barberia.getBarbero().isDurmiendo()+" "+barberia.getBarbero2().isDurmiendo());
            if (barberia.getSemaphoreBarberia().availablePermits() == sillas) {
                cerrarBarberia = true;
                System.out.println("------------ La Barberia va a cerrar -----------");
                barberia.getBarbero().getSemaphorBarbero().release();
                barberia.getBarbero2().getSemaphorBarbero().release();
            }     
        }
    }
}
