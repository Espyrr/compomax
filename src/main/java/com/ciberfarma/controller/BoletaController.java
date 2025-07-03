package com.ciberfarma.controller;

import com.ciberfarma.model.Boleta;
import com.ciberfarma.model.Usuario;
import com.ciberfarma.service.BoletaService;
import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/boletas")
public class BoletaController {

	@Autowired
	private BoletaService boletaService;

	@GetMapping
	public String listarBoletas(HttpSession session, Model model) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
		if (usuario == null) {
			return "redirect:/login";
		}

		List<Boleta> boletas;
		if (usuario.getIdTipo().getIdTipo() == 1) {
			boletas = boletaService.listarTodos();
		} else {
			boletas = boletaService.listarPorUsuario(usuario);
		}

		model.addAttribute("boletas", boletas);
		return "boletas/listado"; 
	}

	@GetMapping("/{id}/pdf")
	public void verPdf(@PathVariable("id") Integer id, HttpSession session,
			jakarta.servlet.http.HttpServletResponse response) throws Exception {

		Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
		if (usuario == null) {
			response.sendRedirect("/login");
			return;
		}

		Boleta boleta = boletaService.obtenerPorId(id).orElse(null);
		if (boleta == null || (usuario.getIdTipo().getIdTipo() != 1
				&& !boleta.getUsuario().getIdUsuario().equals(usuario.getIdUsuario()))) {
			response.sendRedirect("/boletas");
			return;
		}

		InputStream jasperStream = new ClassPathResource("reporte_boleta.jasper").getInputStream();
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(List.of(boleta));

		Map<String, Object> params = new HashMap<>();
		params.put("logoPath", new ClassPathResource("logo.png").getInputStream()); 

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, params, dataSource);

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=boleta_" + id + ".pdf");

		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
	}

}
