package org.lessons.java.pizzeria.controller;

import java.time.LocalDate;
import java.util.List;

import org.lessons.java.pizzeria.model.Pizza;
import org.lessons.java.pizzeria.model.SpecialOffer;
import org.lessons.java.pizzeria.service.IngredientService;
import org.lessons.java.pizzeria.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/menu")
public class PizzaController {

	@Autowired
	// repository field con autowired per dipendency injection
	private PizzaService pizzaService;
	
	@Autowired
	private IngredientService ingredientService;

	@GetMapping
	public String index(Model model, @RequestParam(name = "name", required = false) String name) {

		model.addAttribute("pizzaName", name);
		List<Pizza> pizzaList;

		if (name != null && !name.isEmpty()) {
			model.addAttribute("pizzaName", name);
			pizzaList = pizzaService.findByName(name);

		} else {
			// prendo i dati da consegnare a pizzas
			pizzaList = pizzaService.findAllSortedById();
		}

		// pizzaList = repo.findAll();
		// li inserisco nel modello
		model.addAttribute("menu", pizzaList);

		return "/pizzas/index";
	}

	@GetMapping("/{id}")
	public String show(@PathVariable("id") Integer pizzaId, Model model) {
		model.addAttribute("pizza", pizzaService.findById(pizzaId));
		return "/pizzas/show";
	}
	
	//create	
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("pizza", new Pizza());
		model.addAttribute("ingredients", ingredientService.findAllSortedById());
		return "/pizzas/create";
	}
	
	//Store
	@PostMapping("/create")
	public String store(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult, RedirectAttributes attributes, Model model) {
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("ingredients", ingredientService.findAllSortedById());
			return "/pizzas/create";
		}
		pizzaService.create(formPizza);
		attributes.addFlashAttribute("successMessage", formPizza.getName() + " has been created!");
		
		
		return "redirect:/menu";
	}
	
	//edit
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, Model model) {
		
		//metodo lungo
//			//trovo la pizza
//			Pizza pizzaToEdit = repo.findById(id).get();
//			//la inserisco nel model
//			model.addAttribute("pizza", pizzaToEdit);
		
		//metodo rapido
		model.addAttribute("pizza", pizzaService.findById(id));
		model.addAttribute("ingredients", ingredientService.findAllSortedById());
		
		//restituisco la view con il model inserito
		return "pizzas/edit";
	}
	
	//update
	@PostMapping("/edit/{id}")
	public String update(@Valid @ModelAttribute("pizza") Pizza updatedFormPizza, BindingResult bindingResult, RedirectAttributes attributes, Model model) {
		
		//se ci sono errori nel form, mostra gli errori
		if (bindingResult.hasErrors()) {
			model.addAttribute("ingredients", ingredientService.findAllSortedById());
			return "/pizzas/edit";
		}
		//altrimenti salva la pizza nella repo
		pizzaService.update(updatedFormPizza);	
		//ritorna al menu aggiornato
		attributes.addFlashAttribute("successMessage", updatedFormPizza.getName() + " has been updated!");
		
		
		return "redirect:/menu";
	}
	
	//delete
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes attributes) {
		
		//deleteById cerca ed elimina in un unico comando
		pizzaService.deleteById(id);
		
		attributes.addFlashAttribute("deletedMessage", "Pizza with id " + id + " has been deleted!");
		
		return "redirect:/menu";
	}
	
	//apply special offer
	@GetMapping("/{id}/specialOffer")
	public String setSpecialOffer(@PathVariable("id") Integer id, Model model) {
		SpecialOffer specialOffer = new SpecialOffer();
		specialOffer.setStartingDate(LocalDate.now());
		specialOffer.setPizza(pizzaService.findById(id));
		model.addAttribute("specialOffer", specialOffer);
		
		
		return "/specialOffers/create";
	}
	
	

}
