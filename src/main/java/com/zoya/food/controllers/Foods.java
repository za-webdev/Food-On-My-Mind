package com.zoya.food.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zoya.food.models.Ingredient;
import com.zoya.food.models.Recipe;
import com.zoya.food.models.Review;
import com.zoya.food.models.User;
import com.zoya.food.services.IngredientService;
import com.zoya.food.services.RecipeService;
import com.zoya.food.services.ReviewService;
import com.zoya.food.services.UserService;


@Controller
public class Foods {
	private UserService userService;
	private RecipeService recipeService;
	private ReviewService rService;
	private IngredientService ingService;
	private String[] stars = {"★★★★★","★★★★☆","★★★☆☆","★★☆☆☆","★☆☆☆☆"};
	
	public Foods(UserService userService,RecipeService recipeService,ReviewService rService,IngredientService ingService) {
		this.userService=userService;
		this.recipeService=recipeService;
		this.rService=rService;
		this.ingService=ingService;
	}
	
	@RequestMapping("/add/recipe")
	public String addRecipe(Model model,Principal principal,Ingredient ingredient) {
	model.addAttribute("currentUser",userService.findByUsername(principal.getName()));
	model.addAttribute("my_recipe", new Recipe());
		return "newRecipe";
	}
	
	@PostMapping("/createRecipe")
	public String createRecipe(Model model,@Valid @ModelAttribute("my_recipe") Recipe recipe, BindingResult result,@RequestParam("ing") String ing,@RequestParam("file") MultipartFile file,RedirectAttributes flash) {
		if (result.hasErrors()) {
			if(recipe.getIngredients() == null) {
				flash.addFlashAttribute("error", "cannot be null");
//				System.out.println("its null");
			}
			return "newRecipe";
		}
//	userValidator.Recipevalidate(recipe, result);
//        if (result.hasErrors()) {
//            return "newRecipe";
//       }
		

		
		
		if (file!= null) {
			
			try {
				byte[] bytes =file.getBytes();
				
				String encodedFile = Base64.getEncoder().encodeToString(bytes);
				recipe.setImg(encodedFile);
				
			}
			
			catch(Exception e ) {
				System.out.println("problem");
			}
			
		}
		Recipe my_Recipe =recipeService.create(recipe);
		List<String> items =(List<String>) Arrays.asList(ing.toLowerCase().trim().split("\\s*,\\s*"));
		
		ArrayList<Ingredient> ingredients=new ArrayList<Ingredient>();
		for(int i=0;i<items.size();i++) {
			ArrayList<Ingredient> findIng= ingService.findByIngredient(items.get(i));
			if(findIng.isEmpty()) {
				ingredients.add(ingService.create(items.get(i)));
			}
			else {
				ingredients.add(findIng.get(0));
			}
			
		}
		my_Recipe.setIngredients(ingredients);
		recipeService.update(my_Recipe);
		if(recipe.getImg().isEmpty()) {
			System.out.println("its empty");
		}
		
		return "redirect:/home";
	}
	
	
	@RequestMapping("/show/recipe/{id}")
	public String showRecipe(Principal principal,Model model, @PathVariable("id") Long id) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("currentUser",userService.findByUsername(principal.getName()));
		model.addAttribute("Recipe",recipeService.findById(id));
		model.addAttribute("my_review", new Review());
		model.addAttribute("stars", stars);
		return "showRecipe";
	}
	
	@PostMapping("/review/{id}")
	public String addReview(Principal principal,@Valid @ModelAttribute("my_review") Review review, BindingResult result, Model model, @PathVariable("id") Long id, RedirectAttributes flash){
		
		User user = userService.findByUsername(principal.getName());
		System.out.println(principal.getName());
		model.addAttribute("currentUser",user);
		
		if(user == null) {
			flash.addFlashAttribute("errors", "You must be logged in to do this!");
			return "redirect:/show/recipe/"+id;
		}
		if(result.hasErrors()) {
			model.addAttribute("Recipe", recipeService.findById(id));
			model.addAttribute("stars", stars);
			flash.addFlashAttribute("errors","Review must be 5 characters or longer");
			return String.format("redirect:/show/recipe/"+ id+ "/#review_form");
		}
		
		Recipe rep = recipeService.findById(id);
		List<Review> reviews = rep.getReviews();

		for(Review rev: reviews) {
			if(rev.getUser().getId() == user.getId()) {
				flash.addFlashAttribute("errors", "You have already left a review!");
				return String.format("redirect:/show/recipe/"+ id+ "/#review_form");
				
			}
		}
		
		review.setId(null);
		rService.create(review);
		reviews = rep.getReviews();
		reviews.add(review);
		Double sum = 0.0;
		for(Review rev: reviews) {
			sum += rev.getRating();
		}	
//		System.out.println(sum/reviews.size());
		rep.setAvgRating(sum/reviews.size());
		recipeService.update(rep);
		return "redirect:/show/recipe/"+id;
//		return String.format("redirect:/show/recipe/"+ id+ "/#review_form");
	}
	
	
	@RequestMapping("/addToFav")
	public String joinEvent(Principal principal,@RequestParam Long user_id,@RequestParam Long recipe_id) {
		
		User user = userService.findById(user_id);
		Recipe recipe = recipeService.findById(recipe_id);
		List<Recipe> favRecipes = user.getFavRecipes();
		favRecipes.add(recipe);
		user.setFavRecipes(favRecipes);
		userService.update(user);
		System.out.println(user_id);
		System.out.println(recipe_id);
		return "redirect:/show/recipe/"+recipe_id;
	}
	
	@RequestMapping("/profile")
    public String home(Principal principal, Model model) {
    	if(principal!= null) {
	        String username = principal.getName();
	        model.addAttribute("currentUser", userService.findByUsername(username));
    	}
    	
        return "profile";
    }
	
//	@RequestMapping("/profile/img")
//	public String profileImage(Principal principal,User user,@RequestParam("file") MultipartFile file) {
//		if (file!= null) {
//			
//			try {
//				byte[] bytes =file.getBytes();
//				
//				String encodedFile = Base64.getEncoder().encodeToString(bytes);
//				user.setImg(encodedFile);
//				
//			}
//			
//			catch(Exception e ) {
//				System.out.println("problem");
//			}
//			
//		}
//		return "redirect:/profile";
//	}
	
	@RequestMapping("/edit/{id}")
	public String edit(Principal principal,Model model, @PathVariable("id") Long id) {
		String username = principal.getName();
        model.addAttribute("currentUser", userService.findByUsername(username));
		model.addAttribute("my_recipe", recipeService.findById(id));

		//to show pre-populated ingredients for this specific recipe
		List<Ingredient> ings= recipeService.findById(id).getIngredients();
		String ingredients = "";
		for(Ingredient ing: ings) {
			ingredients+=ing.getIngredient()+",";
		}
		model.addAttribute("ings",ingredients);
//		System.out.println(ings);
		return "edit";
	}
	
	@PostMapping("/update/{id}")
	public String updateRecipe(@Valid @ModelAttribute("my_recipe") Recipe recipe, BindingResult result,@RequestParam("ing") String ing, @PathVariable("id") Long id,@RequestParam("file") MultipartFile file) {
		if(result.hasErrors()) {
			return "edit";
		}
		
		Recipe my_Recipe =recipeService.findById(id);
		List<String> items =(List<String>) Arrays.asList(ing.toLowerCase().trim().split("\\s*,\\s*"));
		
		ArrayList<Ingredient> ingredients=new ArrayList<Ingredient>();
		for(int i=0;i<items.size();i++) {
			ArrayList<Ingredient> findIng= ingService.findByIngredient(items.get(i));
				if(findIng.isEmpty()) {
					ingredients.add(ingService.create(items.get(i)));
				}
				else {
					ingredients.add(findIng.get(0));
				}
		}
		
		my_Recipe.setIngredients(ingredients);
		
		if (file!= null) {
			
			try {
				byte[] bytes =file.getBytes();
				
				String encodedFile = Base64.getEncoder().encodeToString(bytes);
				recipe.setImg(encodedFile);
				
			}
			
			catch(Exception e ) {
				System.out.println("problem");
			}
			
		}
	
		Recipe old_recipe= recipeService.findById(id);
		if(old_recipe.getImg().isEmpty()) {
			old_recipe.setImg(recipe.getImg());
		}
		
		if(recipe.getImg().isEmpty()) {
			recipe.setImg(old_recipe.getImg());
		}
		
		recipe.setAvgRating(old_recipe.getAvgRating());
		recipe.setFavs(old_recipe.getFavs());
		recipe.setIngredients(ingredients);
		
		System.out.println("ingredient is"+ recipe.getIngredients());
		recipeService.update(recipe);

		return "redirect:/profile";
	}
	
	@RequestMapping("/edit/profile/{id}")
	public String editProfile(Principal principal,Model model, @PathVariable("id") Long id) {
		String username = principal.getName();
        model.addAttribute("CurrentUser", userService.findByUsername(username));
		return "edit-profile";
	}
	
	@PostMapping("/update/profile/{id}")
	public String updateProfile(Model model,@Valid @ModelAttribute("CurrentUser") User user, BindingResult result, @PathVariable("id") Long id,@RequestParam("file") MultipartFile file) {
		
		User currentUser=userService.findById(id);
		if(result.hasErrors()) {
			return "edit-profile";
		} 
		
		if (file!= null) {
			
			try {
				byte[] bytes =file.getBytes();
				
				String encodedFile = Base64.getEncoder().encodeToString(bytes);
				user.setImg(encodedFile);
				
			}
			
			catch(Exception e ) {
				System.out.println("problem");
			}
			
		}
		
		if(!user.getImg().isEmpty()) {
			currentUser.setImg(user.getImg());
		}
		
		currentUser.setAlias(user.getAlias());
		currentUser.setDescription(user.getDescription());
		
		userService.update(currentUser);
		return "redirect:/profile";
	}
	
	@RequestMapping("/delete/{id}")
    public String destroy(@PathVariable("id") Long id) {
		
        recipeService.delete(id);
        return "redirect:/profile";
    }
	
	
}


