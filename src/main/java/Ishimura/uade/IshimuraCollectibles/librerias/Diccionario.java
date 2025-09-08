package Ishimura.uade.IshimuraCollectibles.librerias;
import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;
import Ishimura.uade.IshimuraCollectibles.exceptions.ColeccionableException;
import Ishimura.uade.IshimuraCollectibles.exceptions.CategoryDuplicateException;


public class Diccionario {
    private Nodo inicio;
    private int tamanio;

    public void inicializar() {
        inicio = null;
        tamanio = 0;
    }

    public void agregar(int clave, Coleccionable coleccionable, int stock) {
        Nodo actual = inicio;
        while (actual != null) {
            if (actual.clave == clave) {
                throw new ColeccionableException("La clave ya existe en el diccionario.");// fijar como funciona con expeiond balbalal
            }
            actual = actual.siguiente;
        }
        Nodo nuevoNodo = new Nodo(clave, coleccionable, stock);
        nuevoNodo.siguiente = inicio;
        inicio = nuevoNodo;
        tamanio++;
    }

    public void sacar(int clave) {
    Nodo actual = inicio;
    Nodo anterior = null;

    while(actual!=null && actual.clave != clave){
        anterior = actual;
        actual = actual.siguiente;
    }
    if(actual != null){
        if(anterior == null){
            inicio = actual.siguiente;
        }else{
            anterior.siguiente = actual.siguiente;
        }
        tamanio--;
        }
    }

    public int[] getClaves() {
        int[] claves = new int[tamanio];
        Nodo actual = inicio;
        int index = 0;
        while (actual != null) {
            claves[index++] = actual.clave;
            actual = actual.siguiente;
        }
        return claves;
    }

    public Object[] getValor(int clave) {
        Nodo actual = inicio;
        while (actual != null) {
            if (actual.clave == clave) {
                return actual.obtenerValores();
            }
            actual = actual.siguiente;
        }
        throw new ColeccionableException("La clave no existe");
    }

    public boolean isEmpty() {
        return inicio == null;
    }


}
