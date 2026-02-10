package com.example.planify.data.POJOs;

/*
 * Esta clase representa LA UNIDAD MÁS PEQUEÑA del sistema de calendario.
 *
 * Cada objeto CalendarDay corresponde a UNA CASILLA del calendario mensual
 * que se muestra en el RecyclerView gestionado por CalendarAdapter.
 *
 * IMPORTANTE:
 * - No representa una fecha completa (no tiene mes ni año)
 * - No tiene lógica de UI
 * - Solo contiene la información mínima necesaria para pintar una casilla
 *
 * Esta clase es utilizada por:
 * → FragmentMainCalendario, que genera una lista de CalendarDay
 * → CalendarAdapter, que usa esa lista para pintar el calendario
 *
 * Gracias a esta clase podemos representar:
 *  - días reales (1, 2, 3, ...)
 *  - huecos del calendario (null), cuando el mes no empieza en lunes
 */

public class CalendarDay {

    // Número del día del mes
    // Si es null, significa que esta casilla es un hueco vacío
    private Integer dayNumber;

    public CalendarDay(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    /*
     * Este método se usa principalmente en el CalendarAdapter
     * para decidir si la casilla:
     *  - se deja vacía
     *  - se puede clicar
     */
    public boolean isEmpty() {
        return dayNumber == null;
    }

    /*
     * Devuelve el número del día para mostrarlo en pantalla.
     * El CalendarAdapter lo convierte en texto.
     */
    public Integer getDayNumber() {
        return dayNumber;
    }
}