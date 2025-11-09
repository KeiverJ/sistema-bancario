package com.example.model.solicitud;

import org.junit.jupiter.api.Test;

import com.example.model.solicitud.Solicitud.EstadoSolicitud;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudE2ETest {
    @Test
    void flujoCompleto() {
        Solicitud solicitud = new Solicitud();
        assertEquals(Solicitud.EstadoSolicitud.RECIBIDA, solicitud.getEstado());
        solicitud.marcarValidando();
        assertEquals(Solicitud.EstadoSolicitud.VALIDANDO, solicitud.getEstado());
        solicitud.rechazar("No cumple requisitos");
        assertEquals(Solicitud.EstadoSolicitud.RECHAZADA, solicitud.getEstado());
        assertTrue(solicitud.getComentarios().stream().anyMatch(c -> c.contains("Rechazo")));
    }
}
