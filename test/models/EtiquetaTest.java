package models;

import org.dbunit.JndiDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import play.Environment;
import play.Logger;
import play.db.Database;
import play.db.jpa.JPAApi;
import play.inject.Injector;
import play.inject.guice.GuiceApplicationBuilder;

import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.Assert.*;

public class EtiquetaTest {
    static private Injector injector;

    @BeforeClass
    static public void initApplication() {
        GuiceApplicationBuilder guiceApplicationBuilder =
                new GuiceApplicationBuilder().in(Environment.simple());
        injector = guiceApplicationBuilder.injector();
        injector.instanceOf(JPAApi.class);
    }

    @Test
    public void crearEtiqueta() {
        Etiqueta etiqueta = new Etiqueta("Importante");
        assertEquals("Importante", etiqueta.getTexto());
    }

    @Test
    public void addEtiquetaDB() {
        EtiquetaRepository etiquetaRepository = injector.instanceOf(EtiquetaRepository.class);
        Etiqueta etiqueta = new Etiqueta("Importante");
        etiqueta = etiquetaRepository.add(etiqueta);
        assertNotNull(etiqueta.getId());
    }

    @Test
    public void equalsEtiquetas() {
        // La igualdad en etiquetas sin id se basa en el texto
        Etiqueta etiqueta1 = new Etiqueta("A");
        Etiqueta etiqueta2 = new Etiqueta("A");
        Etiqueta etiqueta3 = new Etiqueta("B");
        assertEquals(etiqueta1, etiqueta2);
        assertNotEquals(etiqueta1, etiqueta3);

        // La igualdad en etiquetas con id se basa en el id
        etiqueta1.setId(1000L);
        etiqueta2.setId(1001L);
        etiqueta3.setId(1000L);
        assertEquals(etiqueta1, etiqueta3);
        assertNotEquals(etiqueta1, etiqueta2);
    }
}