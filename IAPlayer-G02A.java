/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conecta4;

import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author José María Serrano
 * @version 1.5 Departamento de Informática. Universidad de Jáen
 *
 * Inteligencia Artificial. 2º Curso. Grado en Ingeniería Informática
 *
 * Clase IAPlayer para representar al jugador CPU que usa una técnica de IA
 *
 * Esta clase es la que tenemos que implementar y completar
 *
 */
public class IAPlayer extends Player {

    //Historial de movimientos de jugadas (para deshacer movimientos)
    private final Stack<Integer> historialMovimientos;

    //Máximo número de hijos por nodo
    private static final int MAX_LV = 8;
    private int MAX_HIJOS;
    private int infnivel;
    private int supnivel;

    //Numero de filas y columnas
    private int filas;
    private int columnas;

    //Estados actual y anterior del tablero
    private int[][] estado;
    private int[][] anterior;

    //Jugada padre (se actualiza con cada jugada)
    private Jugada padre;

    //Clase Jugada, representa un nodo del árbol
    private class Jugada {

        //Vector de hijos
        private final ArrayList<Jugada> hijos;
        private int utilidad;
        //Columna donde se echó ficha en esta jugada
        private final int columna;
        //Numero de hijos de la jugada
        private int numhijos;
        private int alfa;
        private int beta;

        public Jugada(int utilidad, int columna, int alfa, int beta) {

            //Si no es un nodo hoja, creo el vector de hijos
            this.hijos = new ArrayList<>();
            this.utilidad = utilidad;
            this.numhijos = 0;
            this.columna = columna;
            this.alfa = alfa;
            this.beta = beta;
        }

        //Añadir un hijo a la jugada (si se puede)
        public void addHijo(Jugada j) {
            if (numhijos < MAX_HIJOS) {
                hijos.add(j);
                numhijos++;
            }
        }

        @Override
        public String toString() {
            return "Columna: " + columna + ". NumHijos: " + hijos.size() + ". Utilidad: " + utilidad + ". Alfa: " + alfa + ". Beta: " + beta;
        }
    }

    public int checkWinMatriz(int x, int y, int conecta) {
        /*
		 *	x fila
		 *	y columna
         */

        //Comprobar vertical
        int ganar1 = 0;
        int ganar2 = 0;
        int ganador = 0;
        boolean salir = false;
        for (int i = 0; (i < filas) && !salir; i++) {
            if (estado[i][y] != Conecta4.VACIO) {
                if (estado[i][y] == Conecta4.PLAYER1) {
                    ganar1++;
                } else {
                    ganar1 = 0;
                }
                // Gana el jugador 1
                if (ganar1 == conecta) {
                    ganador = Conecta4.PLAYER1;
                    salir = true;
                }
                if (!salir) {
                    if (estado[i][y] == Conecta4.PLAYER2) {
                        ganar2++;
                    } else {
                        ganar2 = 0;
                    }
                    // Gana el jugador 2
                    if (ganar2 == conecta) {
                        ganador = Conecta4.PLAYER2;
                        salir = true;
                    }
                }
            } else {
                ganar1 = 0;
                ganar2 = 0;
            }
        }
        // Comprobar horizontal
        ganar1 = 0;
        ganar2 = 0;
        for (int j = 0; (j < columnas) && !salir; j++) {
            if (estado[x][j] != Conecta4.VACIO) {
                if (estado[x][j] == Conecta4.PLAYER1) {
                    ganar1++;
                } else {
                    ganar1 = 0;
                }
                // Gana el jugador 1
                if (ganar1 == conecta) {
                    ganador = Conecta4.PLAYER1;
                    salir = true;
                }
                if (ganador != Conecta4.PLAYER1) {
                    if (estado[x][j] == Conecta4.PLAYER2) {
                        ganar2++;
                    } else {
                        ganar2 = 0;
                    }
                    // Gana el jugador 2
                    if (ganar2 == conecta) {
                        ganador = Conecta4.PLAYER2;
                        salir = true;
                    }
                }
            } else {
                ganar1 = 0;
                ganar2 = 0;
            }
        }
        // Comprobar oblicuo. De izquierda a derecha
        ganar1 = 0;
        ganar2 = 0;
        int a = x;
        int b = y;
        while (b > 0 && a > 0) {
            a--;
            b--;
        }
        while (b < columnas && a < filas && !salir) {
            if (estado[a][b] != Conecta4.VACIO) {
                if (estado[a][b] == Conecta4.PLAYER1) {
                    ganar1++;
                } else {
                    ganar1 = 0;
                }
                // Gana el jugador 1
                if (ganar1 == conecta) {
                    ganador = Conecta4.PLAYER1;
                    salir = true;
                }
                if (ganador != Conecta4.PLAYER1) {
                    if (estado[a][b] == Conecta4.PLAYER2) {
                        ganar2++;
                    } else {
                        ganar2 = 0;
                    }
                    // Gana el jugador 2
                    if (ganar2 == conecta) {
                        ganador = Conecta4.PLAYER2;
                        salir = true;
                    }
                }
            } else {
                ganar1 = 0;
                ganar2 = 0;
            }
            a++;
            b++;
        }
        // Comprobar oblicuo de derecha a izquierda 
        ganar1 = 0;
        ganar2 = 0;
        a = x;
        b = y;
        //buscar posiciÃ³n de la esquina
        while (b < columnas - 1 && a > 0) {
            a--;
            b++;
        }

        while (b > -1 && a < filas && !salir) {
            if (estado[a][b] != Conecta4.VACIO) {
                if (estado[a][b] == Conecta4.PLAYER1) {
                    ganar1++;
                } else {
                    ganar1 = 0;
                }
                // Gana el jugador 1
                if (ganar1 == conecta) {
                    ganador = Conecta4.PLAYER1;
                    salir = true;
                }
                if (ganador != Conecta4.PLAYER1) {
                    if (estado[a][b] == Conecta4.PLAYER2) {
                        ganar2++;
                    } else {
                        ganar2 = 0;
                    }
                    // Gana el jugador 2
                    if (ganar2 == conecta) {
                        ganador = Conecta4.PLAYER2;
                        salir = true;
                    }
                }
            } else {
                ganar1 = 0;
                ganar2 = 0;
            }
            a++;
            b--;
        }

        return ganador;
    }

    /**
     * @brief Método para colocar una ficha en la matriz que representa el tablero
     * @param estado - Matriz que representa el tablero
     * @param col - Columna en la que se desea colocar
     * @param jugador - Jugador que coloca la ficha
     * @return fila - Fila donde se ha colocado la ficha
     *         -1 - Si no se ha colocado ficha
     */
    private int colocar(int[][] estado, int col, int jugador) {
        int fila = -1;
        for (int i = filas - 1; i >= 0 && fila == -1; i--) {
            if (estado[i][col] == 0) {
                fila = i;
            }
        }
        if (fila == -1) {
            return -1;
        }
        estado[fila][col] = jugador;
        return fila;
    }

    //Método para mostrar el 100% del árbol
    private void verArbolMiniMax(Jugada sig, int nivel) {
        int jugador = Conecta4.PLAYER1;

        if (nivel % 2 != 0) {
            jugador = Conecta4.PLAYER2;
        }

        System.out.println("--- NIVEL " + nivel + " ---");
        for (int i = 0; i < sig.numhijos; i++) {
            colocar(estado, sig.hijos.get(i).columna, jugador);
            historialMovimientos.add(sig.hijos.get(i).columna);
            verMatriz(estado, filas, columnas, sig.hijos.get(i));
            verArbolMiniMax(sig.hijos.get(i), nivel + 1);
            deshacer(estado);
        }
        System.out.println("--- FIN NIVEL " + nivel + " ---");
    }

    /**
     * @brief Función para deshacer movimientos
     * @param estado - tablero del que se desea deshacer
     */
    private void deshacer(int[][] estado) {
        //Si el tablero está vacío
        if (historialMovimientos.empty()) {
            return;
        }
        //Si no, deshago el movimiento
        int deshago = historialMovimientos.pop();
        //Compruebo donde se puso la última ficha en la columna y deshago
        for (int i = 0; i < filas; i++) {
            if (estado[i][deshago] != Conecta4.VACIO) {
                estado[i][deshago] = Conecta4.VACIO;
                return;
            }
        }
    }

    /**
     * @brief Función para saber si una columna está completa
     * @param col - Columna a comprobar
     * @return true si está completa
     *         false en caso contrario
     */
    public boolean fullColumn(int col) {
        int y = filas - 1;
        //Ir a la última posición de la columna	
        while ((y >= 0) && (estado[y][col] != 0)) {
            y--;
        }

        // Si y < 0, columna completa
        return (y < 0);
    }

    //Método para mostrar la matriz
    private void verMatriz(int[][] m, int nfilas, int ncol, Jugada sig) {
        System.out.println(sig);
        for (int i = 0; i < nfilas; i++) {
            for (int j = 0; j < ncol; j++) {
                System.out.printf("%2d ", m[i][j]);
            }
            System.out.println("\n");
        }
    }

    /**
     * @brief Función para calcular el número de hijos de un nodo
     * @return hijos - Número de hijos
     */
    private int numHijos() {
        int hijos = 0;
        for (int i = 0; i < columnas; i++) {
            if (!fullColumn(i)) {
                hijos++;
            }
        }
        return hijos;
    }

    /**
     * @brief Método recursivo que construye el arbol minimax, a partir de un nodo desarrolla hacia abajo hasta un nodo hoja 
     * @param nodo - Nodo a expandir
     * @param conecta - Número de fichas contiguas necesarias para ganar
     * @param nivel - nivel en el que se encuentra la generación
     * 
     * 
     */
    private void construyeMiniMax(Jugada nodo, int conecta, int nivel) {
        //Compruebo qué jugador soy mirando el nivel del arbol en el que me encuentro
        int jugador = Conecta4.PLAYER2;

        if (nivel % 2 != 0) {
            jugador = Conecta4.PLAYER1;
        }

        //Intento hacer jugadas en todas las columnas
        for (int i = 0; i < columnas; i++) {

            //Si la columna no está llena, y puede hacerse una jugada
            if (!fullColumn(i)) {

                //Coloca una ficha y devuelve la fila donde se colocó
                int fila = colocar(estado, i, jugador);
                //Añade la columna donde echó ficha al historial de movimientos
                historialMovimientos.add(i);

                //Calcula la utilidad de la jugada
                int hoja = checkWinMatriz(fila, i, conecta);
                //Como no se si mi hijo va a ser hoja, lo inicio a nulo
                Jugada sig = null;
                //Si es hoja, lo creo con la utilidad directamente y no llamo al método otra vez
                if (numHijos() == 0 || hoja != Conecta4.VACIO || (nivel + 1) >= supnivel) {
                    sig = new Jugada(heuristicaUtilidad(conecta), i, Integer.MIN_VALUE, Integer.MAX_VALUE);
                } else {
                    //Si no es hoja, miro si será nodo MIN o MAX y llamo al método...
                    //Básicamente, si estamos en PLAYER1 (nodo MIN), el hijo siguiente será de PLAYER2 (nodo MAX) así que lo inicio a MIN_VALUE
                    //Para que obtenga el máximo (ya que es MAX) y viceversa.
                    if (jugador == Conecta4.PLAYER1) {
                        sig = new Jugada(Integer.MIN_VALUE, i, nodo.alfa, nodo.beta);
                    } else {
                        sig = new Jugada(Integer.MAX_VALUE, i, nodo.alfa, nodo.beta);
                    }
                    //... sigo creando el arbol
                    construyeMiniMax(sig, conecta, nivel + 1);
                }
                //Añador al hijo a la lista
                nodo.addHijo(sig);

                //Asigno utilidad en función de que jugador sea. En este punto, un nodo no hoja debería de haber creado el hijo
                //sig. Así que según el nivel que sea (MIN o MAX), asigno la utilidad del padre en función de si es P1 o P2
                //Si soy P2 -> Me quedo con el mayor (MAX)
                //Si soy P1 -> Me quedo con el menor (MIN)
                /*MINIMAX Restringido sin poda
                if (jugador == Conecta4.PLAYER2) {
                    if (nodo.utilidad < sig.utilidad) {
                        nodo.utilidad = sig.utilidad;
                    }
                } else {
                    if (nodo.utilidad > sig.utilidad) {
                        nodo.utilidad = sig.utilidad;
                    }
                }*/
                //Si soy el jugador MAX (P2) puedo podar
                //Algoritmo hecho según wikipedia: https://es.wikipedia.org/wiki/Poda_alfa-beta
                //Y además con ayuda de esta página: http://homepage.ufp.pt/jtorres/ensino/ia/alfabeta.html
                if (jugador == Conecta4.PLAYER2) {
                    //Tomo como posible valor alfa la utilidad de mi hijo
                    int auxalfa = sig.utilidad;

                    //Actualizo las utilidades como hacía antes
                    if (sig.utilidad > nodo.utilidad) {
                        nodo.utilidad = sig.utilidad;
                    }

                    //PODA: Si mi beta es menor o igual que auxalfa, no
                    //me interesa explorar.
                    if (nodo.beta <= auxalfa) {
                        deshacer(estado);
                        return;
                    } else {
                        //Si no podo, actualizo valores de alfa (ya que estamos en MAX)
                        if (nodo.alfa < auxalfa) {
                            nodo.alfa = auxalfa;
                        }
                    }
                } else {
                    //Similar a J2
                    int auxbeta = sig.utilidad;

                    if (sig.utilidad < nodo.utilidad) {
                        nodo.utilidad = sig.utilidad;
                    }

                    if (auxbeta <= nodo.alfa) {
                        deshacer(estado);
                        return;
                    } else {
                        if (nodo.beta > auxbeta) {
                            nodo.beta = auxbeta;
                        }
                    }
                }
                //Deshago la jugada para seguir desarollando el árbol
                deshacer(estado);
            }
        }

    }

    //Constructor de la clase
    public IAPlayer() {
        historialMovimientos = new Stack<>();
        infnivel = 0;
        supnivel = 0;
    }

    /**
     * @breif Función heurística para los nodos hoja
     * @param conecta - Número de fichas que tienen que estar contiguas para ganar
     * @return cont - Valor heurístico del nodo.
     */
    private int heuristicaUtilidad(int conecta) {
        //verMatriz(estado, filas, columnas, null);
        int cont = 0;

        //Verticales
        cont += heuristicaVerticales(conecta);

        //Horizontal
        cont += heuristicaHorizontales(conecta);

        //Diagonales
        cont += heuristicaDiagonalesIzqDer(conecta);
        cont += heuristicaDiagonalesDerIzq(conecta);

        return cont;

    }

    private int heuristicaDiagonalesIzqDer(int conecta) {
        int cont = 0;

        for (int i = 0; i <= filas - conecta; i++) {
            int contFichas = 0;

            for (int j = 0; j <= columnas - conecta; j++) {
                int player = 0;
                contFichas = 0;

                for (int k = 0; k < conecta; k++) {

                    if (estado[i + k][j + k] == Conecta4.PLAYER1) {
                        if (player == Conecta4.PLAYER2) {
                            contFichas = -1;
                            break;
                        }
                        contFichas++;
                        player = Conecta4.PLAYER1;

                    } else if (estado[i + k][j + k] == Conecta4.PLAYER2) {
                        if (player == Conecta4.PLAYER1) {
                            contFichas = -1;
                            break;
                        }
                        contFichas++;
                        player = Conecta4.PLAYER2;
                    }
                }

                if (contFichas != -1 && player != 0) {
                    if (player == Conecta4.PLAYER1) {
                        cont -= Math.pow(10, contFichas);
                    } else {
                        cont += Math.pow(10, contFichas);
                    }
                }
            }

        }
        return cont;
    }

    private int heuristicaDiagonalesDerIzq(int conecta) {
        int cont = 0;

        for (int i = 0; i <= filas - conecta; i++) {
            int contFichas = 0;

            for (int j = columnas - 1; j >= conecta - 1; j--) {
                int player = 0;
                contFichas = 0;

                for (int k = 0; k < conecta; k++) {

                    if (estado[i + k][j - k] == Conecta4.PLAYER1) {
                        if (player == Conecta4.PLAYER2) {
                            contFichas = -1;
                            break;
                        }
                        contFichas++;
                        player = Conecta4.PLAYER1;

                    } else if (estado[i + k][j - k] == Conecta4.PLAYER2) {
                        if (player == Conecta4.PLAYER1) {
                            contFichas = -1;
                            break;
                        }
                        contFichas++;
                        player = Conecta4.PLAYER2;
                    }
                }

                if (contFichas != -1 && player != 0) {
                    if (player == Conecta4.PLAYER1) {
                        cont -= Math.pow(10, contFichas);
                    } else {
                        cont += Math.pow(10, contFichas);
                    }
                }
            }

        }
        return cont;
    }

    private int heuristicaVerticales(int conecta) {

        int cont = 0;
        for (int j = 0; j < columnas; j++) {
            int auxcont = 0;
            int ultima = 0;
            int hueco = 0;
            for (int i = filas - 1; i >= 0 && hueco < conecta; i--) {

                if (estado[i][j] == Conecta4.PLAYER1) {
                    if (ultima == Conecta4.PLAYER2) {
                        hueco = 1;
                        auxcont = 1;
                    } else {
                        auxcont++;
                        hueco++;
                    }
                    if (ultima != Conecta4.PLAYER1) {
                        ultima = Conecta4.PLAYER1;
                    }
                } else if (estado[i][j] == Conecta4.PLAYER2) {
                    if (ultima == Conecta4.PLAYER1) {
                        hueco = 1;
                        auxcont = 1;
                    } else {
                        auxcont++;
                        hueco++;
                    }
                    if (ultima != Conecta4.PLAYER2) {
                        ultima = Conecta4.PLAYER2;
                    }
                } else if (ultima != 0) {
                    hueco++;
                }
            }
            if (hueco >= conecta) {
                if (ultima == Conecta4.PLAYER2) {
                    cont += Math.pow(10, auxcont);
                } else {
                    cont -= Math.pow(10, auxcont);
                }
            }
        }

        return cont;
    }

    private int heuristicaHorizontales(int conecta) {

        int valor = 0;

        for (int i = 0; i < filas; i++) {
            int contfichas = 0;

            for (int j = 0; j <= columnas - conecta; j++) {
                int player = 0;
                contfichas = 0;

                for (int k = j; k < (j + conecta); k++) {

                    if (estado[i][k] == Conecta4.PLAYER1) {
                        if (player == Conecta4.PLAYER2) {
                            contfichas = -1;
                            break;
                        }
                        contfichas++;
                        player = Conecta4.PLAYER1;

                    } else if (estado[i][k] == Conecta4.PLAYER2) {
                        if (player == Conecta4.PLAYER1) {
                            contfichas = -1;
                            break;
                        }
                        contfichas++;
                        player = Conecta4.PLAYER2;
                    }
                }

                if (contfichas != -1 && player != 0) {
                    if (player == Conecta4.PLAYER1) {
                        valor -= Math.pow(10, contfichas);
                    } else {
                        valor += Math.pow(10, contfichas);
                    }
                }
            }
        }
        //System.out.println("Valor heuristica horizontal: " + valor);
        return valor;
    }

    /**
     * @brief Función para obetener en qué columna se ha puesto la última jugada.
     * @return j - última columna usada
     *         -1 - No ha habido ningún cambio (no debería darlo)
     */
    private int getUltimaColuma() {
        //Devuelve la última columna donde se ha echado ficha (comparando entre las dos matrices, estado y anterior)
        for (int j = 0; j < columnas; j++) {
            for (int i = 0; i < filas; i++) {
                if (estado[i][j] != anterior[i][j]) {
                    return j;
                }
            }
        }
        return -1;
    }

    /**
     * @brief Función para moverse por el árbol (siguente padre de mi jugada)
     * @param col - última columna que se ha usado en el juego
     * @return Jugada si existe siguiente padre null si no existe (jugada no
     * racional por parte del humano, por ejemplo)
     */
    private Jugada buscaSiguientePadre(int col) {
        //Busco en función de la última columna donde se ha puesto ficha, el siguiente padre.
        for (int i = 0; i < padre.numhijos; i++) {
            if (padre.hijos.get(i).columna == col) {
                return padre.hijos.get(i);
            }

        }
        return null;
    }

    /**
     * @brief Método para seleccionar la siguiente jugada de IAPlayer
     * @return Jugada j - jugada que hará la máquina null - si no hay jugadas
     */
    private Jugada seleccionaTirada() {
        //Selecciono de entre las posibilidades, donde poner ficha.
        //Me quedo con la primera jugada que sea igual que la utilidad del padre
        //Es decir, la primera jugada favorable.
        verMatriz(estado, filas, columnas, padre);
        //Recorro los hijos y elijo la mejor jugada
        for (Jugada j : padre.hijos) {
            if (j.utilidad == padre.utilidad) {
                return j;
            }
        }

        return null;
    }

    /**
     * @brief Método para copiar dos matrices
     * @param n - Matriz a copiar
     * @param m - Copia de n
     */
    void copia(int[][] m, int[][] n) {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                m[i][j] = n[i][j];

            }

        }
    }

    private void verRamaRec(Jugada nodo, int nivel) {
        //Compruebo de qué jugador es la jugada
        int jugador = Conecta4.PLAYER2;
        if (nivel % 2 != 0) {
            jugador = Conecta4.PLAYER1;
        }
        System.out.println("===== NIVEL " + nivel + " =====");
        //Realizo la jugada y la añado a los movimientos
        colocar(estado, nodo.columna, jugador);
        historialMovimientos.add(nodo.columna);
        //Muestro la matriz para ver que es correcto
        verMatriz(estado, filas, columnas, nodo);
        //Si sigo teniendo hijos, desarrollo
        if (nodo.numhijos != 0) {
            verRamaRec(nodo.hijos.get(0), nivel + 1);
        }
        //Deshago la jugada y salgo de la recursividad
        deshacer(estado);
    }

    /**
     * @brief Método para ver una rama del árbol
     * @param rama - entero que debe estar entre [0, numHijos)
     */
    private void verRama(int rama) {
        //Si no es válido
        if (padre == null) {
            return;
        }
        //Muestro el primer nivel de la rama
        System.out.println("===== NIVEL 0 =====");
        verMatriz(estado, filas, columnas, padre);
        //Muestro el resto de niveles de la rama
        if (padre.numhijos < rama) {
            return;
        }
        verRamaRec(padre.hijos.get(rama), 1);
        //verMatriz(estado, filas, columnas, padre);
    }

    /**
     *
     * @param tablero Representación del tablero de juego
     * @param conecta Número de fichas consecutivas para ganar
     * @return Jugador ganador (si lo hay)
     */
    @Override
    public int turnoJugada(Grid tablero, int conecta) {
        //Si es la primera jugada
        if (padre == null) {
            //INICIALIZO las siguientes variabes a 0 y MAX_LV
            //Se usan para controlar el nivel del arbol, cuando tienen que generar y cuando no.
            //infnivel representaría por donde va la profundidad en intervalos de MAX_LV en MAX_LV
            //supnivel indica el supremo, máximo nivel que tenemos generado. Si infnivel == supnivel
            //tenemos que aumentar supnivel en MAX_LV niveles ya que generamos los siguientes MAX_LV del arbol.
            infnivel = 0;
            supnivel = MAX_LV;
            this.MAX_HIJOS = tablero.getColumnas();
            this.filas = tablero.getFilas();
            this.columnas = tablero.getColumnas();
            this.estado = tablero.toArray();

            //Inicializar el estado anterior (tablero vacío)
            this.anterior = new int[filas][columnas];
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    anterior[i][j] = 0;
                }
            }

            //Comprueba donde se echó ficha
            int col = getUltimaColuma();
            copia(anterior, estado);
            //Creas el padre
            this.padre = new Jugada(Integer.MIN_VALUE, col, Integer.MIN_VALUE, Integer.MAX_VALUE);

            //Construye el arbol minimax
            construyeMiniMax(padre, conecta, 0);
        } else {
            //Si no es la primera jugada
            //Convierto el tablero (estado actual) en matriz y lo muestro
            estado = tablero.toArray();
            //Comprueba donde se echó ficha
            int columna = getUltimaColuma();
            //Para moverme a la rama que coincida con el último movimiento
            padre = buscaSiguientePadre(columna);
            //Como he cambiado de padre, significa que he bajado un nivel. Incremento el ínfimo.
            infnivel++;

            //Si infnivel == supnivel, tengo que generar los siguientes MAX_LV
            //¡Pero cuidado! En función de MAX_LV puede ser que este nivel sea MAX o MIN (por seguridad lo dejo así).
            //contnivel es un contador general de niveles, cada vez que se baja un nivel, lo cuento (para comprobar).
            if (infnivel == supnivel || padre == null) {
                supnivel += MAX_LV;
                if (padre == null) {
                    padre = new Jugada(0, columna, 0, 0);
                }
                if (infnivel % 2 == 0) {
                    padre.utilidad = Integer.MIN_VALUE;
                } else {
                    padre.utilidad = Integer.MAX_VALUE;
                }
                padre.alfa = Integer.MIN_VALUE;
                padre.beta = Integer.MAX_VALUE;
                construyeMiniMax(padre, conecta, infnivel);
            }

        }

        //Elijo la mejor jugada de entre las disponibles
        Jugada sol = seleccionaTirada();

        //Echa una ficha y devuelve la fila donde quedó
        int fil = tablero.setButton(sol.columna, Conecta4.PLAYER2);
        copia(anterior, estado);
        estado = tablero.toArray();

        //Actualizo el padre a la nueva jugada, ya que como he jugado he bajado un nivel
        //este nivel deberá ser de P1 (MIN)
        padre = sol;
        infnivel++;

        //Igual que antes...
        if (infnivel == supnivel) {
            supnivel += MAX_LV;
            if (infnivel % 2 == 0) {
                padre.utilidad = Integer.MIN_VALUE;
            } else {
                padre.utilidad = Integer.MAX_VALUE;
            }
            padre.alfa = Integer.MIN_VALUE;
            padre.beta = Integer.MAX_VALUE;
            construyeMiniMax(padre, conecta, infnivel);
        }
        return tablero.checkWin(fil, sol.columna, conecta);
    } // turnoJugada

} // IAPlayer
