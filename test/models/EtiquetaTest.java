package models;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EtiquetaTest {
    @Test
    public void crearEtiqueta() {
        Etiqueta etiqueta = new Etiqueta("Importante");
        assertEquals("Importante", etiqueta.getTexto());
    }
}