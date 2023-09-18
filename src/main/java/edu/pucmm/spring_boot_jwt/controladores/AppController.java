package edu.pucmm.spring_boot_jwt.controladores;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class AppController {

    /**
     * Controlador básico para visualizar sistema de plantilla en conjunto con JWT.
     * @param model     *
     * @return
     */
    @RequestMapping("/")
    public String index(Model model){
        String mensaje  = "App JWT con Spring Boot";
        model.addAttribute("mensaje", mensaje);
       return "/thymeleaf/index.html";
    }

    @GetMapping(path = "/contador")
    @ResponseBody
    public String sesion(HttpSession session){
        Integer contador = (Integer) session.getAttribute("contador");
        if(contador==null){
            contador=0;
        }
        contador++;
        session.setAttribute("contador", contador);
        return "Contador de Sesion...: "+contador;
    }
}
