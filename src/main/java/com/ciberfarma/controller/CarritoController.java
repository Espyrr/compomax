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


    @PostMapping("/agregar/{idProducto}")
    public String agregarProducto(@PathVariable("idProducto") String idProducto,
                                  @RequestParam("cantidad") int cantidad,
                                  HttpSession session,
                                  @RequestHeader(value = "referer", required = false) String referer) {

        List<DetalleBoleta> carrito = (List<DetalleBoleta>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        Producto producto = productoService.buscarPorCodigo(idProducto);
        if (producto == null) {
            session.setAttribute("mensaje", "Producto no encontrado.");
            session.setAttribute("tipo", "danger");
            return "redirect:" + (referer != null ? referer : "/");
        }

        // Buscar si ya está en el carrito
        DetalleBoleta itemExistente = null;
        for (DetalleBoleta item : carrito) {
            if (item.getProducto().getIdProducto().equals(idProducto)) {
                itemExistente = item;
                break;
            }
        }

        int cantidadTotal = cantidad;
        if (itemExistente != null) {
            cantidadTotal += itemExistente.getCantidad();
        }

        // Validar contra el stock
        if (cantidadTotal > producto.getStock()) {
            session.setAttribute("mensaje", "No hay suficiente stock disponible para el producto: " + producto.getDescripcion());
            session.setAttribute("tipo", "warning");
            return "redirect:" + (referer != null ? referer : "/");
        }

        if (itemExistente != null) {
            itemExistente.setCantidad(cantidadTotal);
            itemExistente.setImporte(producto.getPrecio().multiply(BigDecimal.valueOf(cantidadTotal)));
        } else {
            DetalleBoleta detalle = new DetalleBoleta();
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setImporte(producto.getPrecio().multiply(BigDecimal.valueOf(cantidad)));
            carrito.add(detalle);
        }

        session.setAttribute("carrito", carrito);
        session.setAttribute("cantArticulos", carrito.stream()
                .mapToInt(DetalleBoleta::getCantidad)
                .sum());

        session.setAttribute("mensaje", "Producto añadido exitosamente al carrito.");
        session.setAttribute("tipo", "success");

        return "redirect:/catalogo"; // redirige al catálogo como pediste
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
    @Controller
    public class MensajeController {

        @PostMapping("/limpiar-mensaje")
        @ResponseBody
        public void limpiarMensaje(HttpSession session) {
            session.removeAttribute("mensaje");
            session.removeAttribute("tipo");
        }
    }

}
