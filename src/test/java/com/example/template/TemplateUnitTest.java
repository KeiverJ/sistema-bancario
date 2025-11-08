package com.example.template;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit (smoke): verificar que las clases de plantilla existen en el classpath.
 */
class TemplateUnitTest {

    @Test
    @DisplayName("Existe clase SolicitudCreditoDefault en classpath")
    void existeSolicitudCreditoDefault() throws Exception {
        Class<?> cls = Class.forName("com.example.template.impl.SolicitudCreditoDefault");
        assertNotNull(cls);
    }
}
