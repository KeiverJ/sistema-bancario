package com.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SolicitudUnitTest {
    @Test
    void flujoEstadosYComentarios() {
        Solicitud s = new Solicitud();
        assertEquals(Solicitud.EstadoSolicitud.RECIBIDA, s.getEstado());
        s.marcarValidando();
        assertEquals(Solicitud.EstadoSolicitud.VALIDANDO, s.getEstado());
        s.aprobar();
        assertEquals(Solicitud.EstadoSolicitud.APROBADA, s.getEstado());

        Solicitud s2 = new Solicitud();
        s2.marcarValidando();
        s2.rechazar("Falta doc");
        assertEquals(Solicitud.EstadoSolicitud.RECHAZADA, s2.getEstado());
        assertTrue(s2.getComentarios().stream().anyMatch(c -> c.contains("Rechazo")));
    }
}
