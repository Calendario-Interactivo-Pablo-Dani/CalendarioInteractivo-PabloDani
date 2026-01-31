package com.example.planify.ui;

/*Esta clase va a ser unicamente para representar una casilla del calendario, es decir, un día
* Nos va a servir para manejar si será un día vacío o no, me explico, si el mes empieza en miercoles,
* el lunes y el martes tienen que tener casillas vacías. También nos servirá para manejar el numDia.
* */

public class CalendarDay {
    private Integer dayNumber; // null = celda vacía

    public CalendarDay(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }
    public boolean isEmpty() {
        return dayNumber == null;
    }
    public Integer getDayNumber() {
        return dayNumber;
    }
}
