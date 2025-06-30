package com.ciberfarma.controller;

import com.ciberfarma.model.Boleta;
import com.ciberfarma.model.Producto;
import com.ciberfarma.model.Usuario;
import com.ciberfarma.model.DetalleBoleta;
import com.ciberfarma.service.ProductoService;
import com.ciberfarma.service.BoletaService;
import com.ciberfarma.service.DetalleBoletaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private BoletaService boletaService;

    @Autowired
    private DetalleBoletaService detalleBoletaService;
    
    @GetMapping
    public String mostrarCarrito(HttpSession session, org.springframework.ui.Model model) {
        List<DetalleBoleta> carrito = (List<DetalleBoleta>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        // Calcular total
        BigDecimal total = carrito.stream()
                .map(DetalleBoleta::getImporte)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);

        return "carrito"; // Asegúrate de que el archivo se llama carrito.html y está en templates/
    }


    @GetMapping("/agregar/{idProducto}")
    public String agregarProducto(@PathVariable("idProducto") String idProducto,
                                  HttpSession session,
                                  @RequestHeader(value = "referer", required = false) String referer) {

        List<DetalleBoleta> carrito = (List<DetalleBoleta>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        boolean existe = false;
        for (DetalleBoleta item : carrito) {
            if (item.getProducto().getIdProducto().equals(idProducto)) {
                item.setCantidad(item.getCantidad() + 1);
                item.setImporte(item.getProducto().getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));
                existe = true;
                break;
            }
        }

        if (!existe) {
            Producto producto = productoService.buscarPorCodigo(idProducto);
            if (producto != null) {
                DetalleBoleta detalle = new DetalleBoleta();
                detalle.setProducto(producto);
                detalle.setCantidad(1);
                detalle.setImporte(producto.getPrecio());
                carrito.add(detalle);
            }
        }

        // ✅ Guardar el carrito y la cantidad total en sesión
        session.setAttribute("carrito", carrito);
        session.setAttribute("cantArticulos", carrito.stream()
                .mapToInt(DetalleBoleta::getCantidad)
                .sum());

        return "redirect:" + (referer != null ? referer : "/");
    }

    @GetMapping("/eliminar/{idProducto}")
    public String eliminarProducto(@PathVariable("idProducto") String idProducto,
                                   HttpSession session,
                                   @RequestHeader(value = "referer", required = false) String referer) {

        List<DetalleBoleta> carrito = (List<DetalleBoleta>) session.getAttribute("carrito");
        if (carrito != null) {
            carrito.removeIf(item -> item.getProducto().getIdProducto().equals(idProducto));
        }

        // ✅ Guardar el carrito actualizado y la nueva cantidad
        session.setAttribute("carrito", carrito);
        session.setAttribute("cantArticulos", carrito.stream()
                .mapToInt(DetalleBoleta::getCantidad)
                .sum());

        return "redirect:" + (referer != null ? referer : "/");
    }


    @GetMapping("/finalizar")
    public String finalizarCompra(HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/login";
        }

        List<DetalleBoleta> carrito = (List<DetalleBoleta>) session.getAttribute("carrito");
        if (carrito == null || carrito.isEmpty()) {
            return "redirect:/";
        }

        // Calcular montos
        BigDecimal subtotal = carrito.stream()
                .map(DetalleBoleta::getImporte)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal igv = subtotal.multiply(new BigDecimal("0.18"));
        BigDecimal total = subtotal.add(igv);

        // Crear boleta
        Boleta boleta = new Boleta();
        boleta.setUsuario(usuario);
        boleta.setFechaBoleta(LocalDateTime.now());
        boleta.setSubtotal(subtotal);
        boleta.setIgv(igv);
        boleta.setTotal(total);
        boletaService.guardar(boleta);

        // Asignar boleta a cada detalle y actualizar stock
        for (DetalleBoleta item : carrito) {
            item.setBoleta(boleta);

            // Descontar stock del producto
            Producto producto = item.getProducto();
            int nuevoStock = producto.getStock() - item.getCantidad();
            producto.setStock(nuevoStock);
            productoService.guardar(producto); // debe tener un método guardar(Product)
        }

        // Guardar todos los detalles
        detalleBoletaService.guardarDetalles(carrito);

        // Limpiar carrito
        session.removeAttribute("carrito");
        return "redirect:/?success=compra";
    }
    
}
