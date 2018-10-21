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
}