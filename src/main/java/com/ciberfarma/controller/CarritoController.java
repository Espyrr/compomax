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
		BigDecimal total = carrito.stream().map(DetalleBoleta::getImporte).reduce(BigDecimal.ZERO, BigDecimal::add);

		model.addAttribute("carrito", carrito);
		model.addAttribute("total", total);

		return "carrito";
	}

	@PostMapping("/agregar/{idProducto}")
	public String agregarProducto(@PathVariable("idProducto") String idProducto, @RequestParam("cantidad") int cantidad,
			HttpSession session, @RequestHeader(value = "referer", required = false) String referer) {

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
			session.setAttribute("mensaje",
					"No hay suficiente stock disponible para el producto: " + producto.getDescripcion());
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
		session.setAttribute("cantArticulos", carrito.stream().mapToInt(DetalleBoleta::getCantidad).sum());

		session.setAttribute("mensaje", "Producto añadido exitosamente al carrito.");
		session.setAttribute("tipo", "success");

		return "redirect:" + (referer != null ? referer : "/catalogo");
	}

	@GetMapping("/eliminar/{idProducto}")
	public String eliminarProducto(@PathVariable("idProducto") String idProducto, HttpSession session,
			@RequestHeader(value = "referer", required = false) String referer) {

		List<DetalleBoleta> carrito = (List<DetalleBoleta>) session.getAttribute("carrito");
		if (carrito != null) {
			carrito.removeIf(item -> item.getProducto().getIdProducto().equals(idProducto));
		}

		session.setAttribute("carrito", carrito);
		session.setAttribute("cantArticulos", carrito.stream().mapToInt(DetalleBoleta::getCantidad).sum());

		return "redirect:" + (referer != null ? referer : "/");
	}

	@GetMapping("/finalizar")
	public String finalizarCompra(HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
		if (usuario == null) {
			session.setAttribute("mensaje", "Debe iniciar sesión o registrarse para finalizar la compra.");
			session.setAttribute("tipo", "warning");
			return "redirect:/carrito";
		}

		List<DetalleBoleta> carrito = (List<DetalleBoleta>) session.getAttribute("carrito");
		if (carrito == null || carrito.isEmpty()) {
			session.setAttribute("mensaje", "No hay productos en el carrito.");
			session.setAttribute("tipo", "warning");
			return "redirect:/carrito";
		}

		// Descontar stock
		for (DetalleBoleta item : carrito) {
			Producto producto = item.getProducto();
			int nuevoStock = producto.getStock() - item.getCantidad();
			producto.setStock(nuevoStock);
			productoService.guardar(producto);
		}

		session.setAttribute("mensaje", "Compra exitosa. ¡Vuelva pronto!");
		session.setAttribute("tipo", "success");

		session.removeAttribute("carrito");
		session.removeAttribute("cantArticulos");

		return "redirect:/carrito";
	}

	@PostMapping("/limpiar-mensaje")
	@ResponseBody
	public void limpiarMensaje(HttpSession session) {
		session.removeAttribute("mensaje");
		session.removeAttribute("tipo");
	}

}
