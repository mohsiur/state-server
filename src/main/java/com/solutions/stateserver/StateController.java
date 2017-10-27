package com.solutions.stateserver;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StateController {

	/**
	 * View Model for Spring Framework Server
	 * @param model
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
    @GetMapping("/home")
    public String lat_lon_Form(Model model) throws ParseException, IOException {
    		Solution solution = new Solution();
        model.addAttribute("home", solution);
        return "home";
    }

    /**
     * Binding the Model to the viewer
     * @param solution
     * @return
     * @throws ParseException
     * @throws IOException
     */
    @PostMapping("/home")
    public String formSubmit(@ModelAttribute Solution solution) throws ParseException, IOException{
    		
    		//System.out.println(solution.getState().toString());
        return "result";
    }
    
	
}
