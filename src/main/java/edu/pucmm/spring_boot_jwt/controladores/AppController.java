package edu.pucmm.spring_boot_jwt.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.websocket.server.PathParam;

@Controller
@RequestMapping("/")
public class AppController {

    /**
     * 
     * @param model
     * @param nombre
     * @return
     */
    @RequestMapping("/")
    public String index(Model model, @RequestParam(value = "nombre", defaultValue = "Mundo") String nombre){
        model.addAttribute("nombre", nombre);
       return "/freemarker/index";
    }
}
