package services;

import models.Etiqueta;
import models.Usuario;
import models.Tarea;
import org.dbunit.JndiDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import play.Environment;
import play.db.jpa.JPAApi;
import play.inject.Injector;
import play.inject.guice.GuiceApplicationBuilder;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EtiquetaServiceTest {
    static private Injector injector;

    @BeforeClass
    static public void initApplication() {
        GuiceApplicationBuilder guiceApplicationBuilder =
                new GuiceApplicationBuilder().in(Environment.simple());
        injector = guiceApplicationBuilder.injector();
        injector.instanceOf(JPAApi.class);
    }

    @Before
    public void initData() throws Exception {
        JndiDatabaseTester databaseTester = new JndiDatabaseTester("DBTodoList");
        IDataSet initialDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("test/resources/test_dataset.xml"));
        databaseTester.setDataSet(initialDataSet);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.onSetup();
    }

    @Test
    public void testFindById() {
        EtiquetaService etiquetaService = injector.instanceOf(EtiquetaService.class);

        Etiqueta etiqueta = etiquetaService.obtenerEtiqueta(1001L);
        assertEquals(etiqueta.getTexto(), "Casa");
    }

    @Test
    public void addEtiqueta() {
        EtiquetaService etiquetaService = injector.instanceOf(EtiquetaService.class);
        TareaService tareaService = injector.instanceOf(TareaService.class);

        Tarea tarea = tareaService.obtenerTarea(1002L);
        Etiqueta etiqueta = new Etiqueta("prueba");

        Usuario usuario = tarea.getUsuario();

        etiqueta = etiquetaService.addEtiqueta(etiqueta.getTexto(), usuario.getId(), tarea.getId());

        tarea = tareaService.obtenerTarea(1002L);

        assertTrue(tarea.getEtiquetas().contains(etiqueta));

    }

   
}
