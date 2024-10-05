package org.lessons.java.pizzeria.controller.api;

import java.util.List;
import java.util.Optional;

import org.lessons.java.pizzeria.model.Pizza;
import org.lessons.java.pizzeria.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("api/pizzas")
public class PizzaRestController {
	
	@Autowired
	private PizzaService service;
	
	@GetMapping
	public List<Pizza> index(@RequestParam(name = "word", required=false) String word){
		List<Pizza> result;
		
		//se la mia keyword è ben formata allora poni la domanda con la word inserita, altrimenti poni una index standard (tutto)
		
		
		if (word != null && !word.isEmpty())	{
			result = service.findByName(word);
		} else {
			result = service.findAllSortedById();
		}
		
		return result;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Pizza> show(@PathVariable Integer id) {
		
		Optional<Pizza> pizza = service.findById(id);
		
		if(pizza.isPresent()) {
			return new ResponseEntity<>(pizza.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(pizza.get(), HttpStatus.NOT_FOUND);
		}
			}
	
	@PostMapping
	public Pizza store(@Valid @RequestBody Pizza pizza) {
		Pizza newPizza = service.create(pizza);
		return newPizza;
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Pizza> update(@Valid @RequestBody Pizza pizza, @PathVariable Integer id) {
		
		Optional <Pizza> oldPizza = service.findById(id);
		
		if(oldPizza.isPresent()) {
			Pizza foundPizza = service.update(pizza);
			return new ResponseEntity<>(foundPizza, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Pizza> delete(@PathVariable Integer id) {
		Optional <Pizza> pizzaToDelete = service.findById(id);
		
		if (pizzaToDelete.isPresent()) {
			service.deleteById(id);
			return new ResponseEntity<Pizza>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	
}
