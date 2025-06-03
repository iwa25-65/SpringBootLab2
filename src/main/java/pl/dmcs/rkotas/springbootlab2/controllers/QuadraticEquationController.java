package pl.dmcs.rkotas.springbootlab2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.dmcs.rkotas.springbootlab2.model.QuadraticEquation;

@Controller
public class QuadraticEquationController {

    @RequestMapping("/quadraticEquation")
    public String quadraticEquation(Model model) {
        model.addAttribute("message", "Enter the coefficients:");
        QuadraticEquation quadraticEquation = new QuadraticEquation();
        model.addAttribute("quadraticEquation", quadraticEquation);
        return "quadraticEquation";
    }

    @RequestMapping(value = "/solveEquation", method = RequestMethod.POST)
    public String solveEquation(@ModelAttribute("quadraticEquation") QuadraticEquation quadraticEquation, Model model) {
        double a = quadraticEquation.getA();
        double b = quadraticEquation.getB();
        double c = quadraticEquation.getC();

        double discriminant = b * b - (4 * a * c);
        double x = (-b) / (2 * a);
        double x1 = ((-b) - Math.sqrt(discriminant)) / (2 * a);
        double x2 = ((-b) + Math.sqrt(discriminant)) / (2 * a);

        String solution;
        if (discriminant > 0) {
            solution = "First solution is " + x1 + ", Second solution is " + x2;
        } else if (discriminant == 0) {
            solution = "The unique solution is " + x;
        } else {
            solution = "There is no real solution";
        }

        model.addAttribute("solution", solution);
        return "quadraticResult";
    }
}