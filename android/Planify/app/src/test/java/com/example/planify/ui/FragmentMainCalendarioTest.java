package com.example.planify.ui;

import static org.junit.Assert.*;

import com.example.planify.data.POJOs.CalendarDay;
import com.example.planify.ui.Fragmentos.FragmentMainCalendario;

import org.junit.Test;

import java.util.Calendar;
import java.util.List;

public class FragmentMainCalendarioTest {

    @Test
    public void generarDiasDelMes_enero2026_debeTener31DiasMasHuecos() {

        FragmentMainCalendario fragment = new FragmentMainCalendario();

        Calendar calendar = Calendar.getInstance();
        calendar.set(2026, Calendar.JANUARY, 1);

        List<CalendarDay> dias = fragment.generarDiasDelMes(calendar);

        // Enero 2026 tiene 31 días
        long diasReales = dias.stream()
                .filter(d -> d.getDayNumber() != null)
                .count();

        assertEquals(31, diasReales);
    }

    @Test
    public void generarDiasDelMes_febreroBisiesto2024_debeTener29Dias() {

        FragmentMainCalendario fragment = new FragmentMainCalendario();

        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.FEBRUARY, 1);

        List<CalendarDay> dias = fragment.generarDiasDelMes(calendar);

        long diasReales = dias.stream()
                .filter(d -> d.getDayNumber() != null)
                .count();

        assertEquals(29, diasReales);
    }

    @Test
    public void generarDiasDelMes_noDebeEstarVacio() {

        FragmentMainCalendario fragment = new FragmentMainCalendario();

        Calendar calendar = Calendar.getInstance();
        calendar.set(2026, Calendar.MARCH, 1);

        List<CalendarDay> dias = fragment.generarDiasDelMes(calendar);

        assertFalse(dias.isEmpty());
    }
}