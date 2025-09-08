package Ishimura.uade.IshimuraCollectibles.librerias;
import java.util.Arrays;

import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;

public class Nodo {
    int clave;
    Object[] valores;
    Nodo siguiente;

    Nodo(int clave, Coleccionable coleccionable, int stock) {
        this.clave = clave;
        this.valores = new Object[1];
        this.valores[0] = coleccionable;
        this.valores[1] = stock;
        this.siguiente = null;
    }

    void agregarValor(int valor) {
       //hacer de nuevo 
    }

    Object[] obtenerValores() {
        return Arrays.copyOf(valores,2); //probar
    }
}