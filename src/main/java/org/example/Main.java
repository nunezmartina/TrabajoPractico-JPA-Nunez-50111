package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("example-unit");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        System.out.println("Estamos en Marcha");

        try {

            entityManager.getTransaction().begin(); // Persistir una nueva entidad Cliente

            Factura factura1 = Factura.builder()
                    .nro(15)
                    .fecha("05/09/2024")
                    .build();

            Domicilio dom1 = Domicilio.builder()
                    .nombreCalle("Concordia")
                    .numero(590)
                    .build();

            Cliente cliente1 = Cliente.builder()
                    .nombre("Martina")
                    .apellido("Nuñez")
                    .dni(45602188)
                    .build();

            cliente1.setDomicilio(dom1);
            dom1.setCliente(cliente1);

            factura1.setCliente(cliente1);

            //creacion de las categorias
            Categoria perecederos = Categoria.builder()
                    .denominacionC("perecederos")
                    .build();

            Categoria lacteos = Categoria.builder()
                    .denominacionC("lacteos")
                    .build();

            Categoria limpieza = Categoria.builder()
                    .denominacionC("limpieza")
                    .build();

            //creacion de los articulos
            Articulo art1 = Articulo.builder()
                    .cantidad(200)
                    .denominacion("Yogurt Frutilla")
                    .precio(1500)
                    .build();

            Articulo art2 = Articulo.builder()
                    .cantidad(300)
                    .denominacion("Detergente Magistral")
                    .precio(2500)
                    .build();

            //asigno categorias a los articulos y los articulos a la categoria
            art1.getCategorias().add(perecederos);
            art1.getCategorias().add(lacteos);
            lacteos.getArticulos().add(art1);
            perecederos.getArticulos().add(art1);

            art2.getCategorias().add(limpieza);
            limpieza.getArticulos().add(art2);

            //creo detalles de facturas
            DetalleFactura det1 = DetalleFactura.builder()
                    .build();

            det1.setArticulo(art1);
            det1.setCantidad(2);
            det1.setSubtotal(40);

            //bidireccionales
            art1.getDetalle().add(det1);
            factura1.getDetalleFacturas().add(det1);
            det1.setFactura(factura1);

            //creo otro detalle
            DetalleFactura det2 = DetalleFactura.builder()
                    .build();

            det2.setArticulo(art2);
            det2.setCantidad(1);
            det2.setSubtotal(80);

            art2.getDetalle().add(det2);
            factura1.getDetalleFacturas().add(det2);
            det2.setFactura(factura1);

            //seteo el total de la factura
            factura1.setTotal(120);

            entityManager.persist(factura1);


        // otro mas
            Cliente cliente2 = Cliente.builder()
                    .nombre("Sofia")
                    .apellido("Martinez")
                    .dni(4802772)
                    .build();
            Domicilio domicilio2= Domicilio.builder()
                    .nombreCalle("San Martin")
                    .numero(3893)
                    .build();
            cliente2.setDomicilio(domicilio2);
            domicilio2.setCliente(cliente2);

            Articulo articulo3= Articulo.builder()
                    .denominacion("Queso cremoso")
                    .cantidad(300)
                    .precio(2000)
                    .build();

            Factura factura2= Factura.builder()
                    .nro(101)
                    .fecha("10/03/2024")
                    .cliente(cliente2)
                    .build();

            articulo3.getCategorias().add(perecederos);
            articulo3.getCategorias().add(lacteos);

            lacteos.getArticulos().add(articulo3);
            perecederos.getArticulos().add(articulo3);



            DetalleFactura detalle3= DetalleFactura.builder()
                    .cantidad(2)
                    .articulo(articulo3)
                    .subtotal(4000)
                    .build();
            articulo3.getDetalle().add(detalle3);
            factura2.getDetalleFacturas().add(detalle3);
            detalle3.setFactura(factura2);

            DetalleFactura detalle4= DetalleFactura.builder()
                    .cantidad(1)
                    .articulo(art1)
                    .subtotal(700)
                    .build();
            art1.getDetalle().add(detalle4);
            factura2.getDetalleFacturas().add(detalle4);
            detalle4.setFactura(factura2);

            factura2.setTotal(4700);
            entityManager.persist(factura2);

            entityManager.flush(); //para limpiar la conexion
            entityManager.getTransaction().commit();


        }catch (Exception e){

            entityManager.getTransaction().rollback();
            }

        // Cerrar el EntityManager y el EntityManagerFactory
        entityManager.close();
        entityManagerFactory.close();
    }
}
