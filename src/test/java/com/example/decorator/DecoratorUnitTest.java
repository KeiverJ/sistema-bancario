package com.example.decorator;

import com.example.model.Cuenta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecoratorUnitTest {

    @Test
    @DisplayName("SeguroVidaDecorator añade costo fijo")
    void seguroVidaAñadeCosto() {
        Cuenta cuenta = new Cuenta();
        cuenta.setCuotaManejo(10000);
        BaseProductoComponent base = new BaseProductoComponent(cuenta);
        ProductoFinancieroComponent decorado = new SeguroVidaDecorator(base);
        assertEquals(10000 + 15000.0, decorado.getCostoMensual(), 0.01);
    }

    @Test
    @DisplayName("BaseProductoComponent: descripción y costo para cuenta y crédito")
    void baseProductoComponentDescripciones() {
        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(Cuenta.TipoCuenta.AHORROS);
        cuenta.setNumeroCuenta("123");
        cuenta.setCuotaManejo(5000);
        BaseProductoComponent baseCuenta = new BaseProductoComponent(cuenta);
        assertTrue(baseCuenta.getDescripcion().contains("Cuenta AHORROS #123"));
        assertEquals(5000, baseCuenta.getCostoMensual());
        assertEquals(0.0, baseCuenta.getBeneficioMensual());

        com.example.model.Credito credito = new com.example.model.Credito();
        credito.setTipoCredito(com.example.model.Credito.TipoCredito.HIPOTECARIO);
        credito.setMonto(200000);
        BaseProductoComponent baseCredito = new BaseProductoComponent(credito);
        assertTrue(baseCredito.getDescripcion().contains("Crédito HIPOTECARIO"));
        assertEquals(0.0, baseCredito.getCostoMensual());
    }

    @Test
    @DisplayName("BaseProductoComponent: descripción y costo para nulo")
    void baseProductoComponentNull() {
        BaseProductoComponent base = new BaseProductoComponent((Cuenta) null);
        assertEquals("Producto", base.getDescripcion());
        assertEquals(0.0, base.getCostoMensual());
    }

    @Test
    @DisplayName("ExencionCuotaDecorator elimina costo mensual")
    void exencionCuotaDecorator() {
        Cuenta cuenta = new Cuenta();
        cuenta.setCuotaManejo(8000);
        BaseProductoComponent base = new BaseProductoComponent(cuenta);
        ProductoFinancieroComponent decorado = new ExencionCuotaDecorator(base);
        assertEquals(0.0, decorado.getCostoMensual(), 0.01);
        assertTrue(decorado.getDescripcion().contains("ExenciónCuotaManejo"));
    }

    @Test
    @DisplayName("CashbackDecorator suma costo y beneficio")
    void cashbackDecorator() {
        Cuenta cuenta = new Cuenta();
        cuenta.setCuotaManejo(9000);
        BaseProductoComponent base = new BaseProductoComponent(cuenta);
        ProductoFinancieroComponent decorado = new CashbackDecorator(base);
        assertEquals(9000 + 5000, decorado.getCostoMensual(), 0.01);
        assertEquals(20000, decorado.getBeneficioMensual(), 0.01);
        assertTrue(decorado.getDescripcion().contains("Cashback"));
    }

    @Test
    @DisplayName("BeneficioVipDecorator suma costo y beneficio")
    void beneficioVipDecorator() {
        Cuenta cuenta = new Cuenta();
        cuenta.setCuotaManejo(7000);
        BaseProductoComponent base = new BaseProductoComponent(cuenta);
        ProductoFinancieroComponent decorado = new BeneficioVipDecorator(base);
        assertEquals(7000 + 20000, decorado.getCostoMensual(), 0.01);
        assertEquals(10000, decorado.getBeneficioMensual(), 0.01);
        assertTrue(decorado.getDescripcion().contains("VIP"));
    }

    @Test
    @DisplayName("ProductoDecorator delega correctamente")
    void productoDecoratorDelegacion() {
        Cuenta cuenta = new Cuenta();
        cuenta.setId("C1");
        cuenta.setCuotaManejo(3000);
        BaseProductoComponent base = new BaseProductoComponent(cuenta);
        ProductoDecorator decorador = new ProductoDecorator(base) {};
        assertEquals("C1", decorador.getId());
        assertEquals(3000, decorador.getCostoMensual());
        assertEquals(0.0, decorador.getBeneficioMensual());
        assertEquals(base.getDescripcion(), decorador.getDescripcion());
    }

    @Test
    @DisplayName("Decoradores anidados: combinación de beneficios y costos")
    void decoradoresAnidados() {
        Cuenta cuenta = new Cuenta();
        cuenta.setCuotaManejo(10000);
        BaseProductoComponent base = new BaseProductoComponent(cuenta);
        ProductoFinancieroComponent decorado = new SeguroVidaDecorator(
                new CashbackDecorator(
                        new BeneficioVipDecorator(base)));
        // 10000 + 20000 (VIP) + 5000 (Cashback) + 15000 (Seguro)
        assertEquals(10000 + 20000 + 5000 + 15000, decorado.getCostoMensual(), 0.01);
        // 10000 (VIP) + 20000 (Cashback)
        assertEquals(10000 + 20000, decorado.getBeneficioMensual(), 0.01);
        String desc = decorado.getDescripcion();
        assertTrue(desc.contains("VIP"));
        assertTrue(desc.contains("Cashback"));
        assertTrue(desc.contains("Seguro de vida"));
    }
}
