package com.example.webshopdat22b.controller;

import com.example.webshopdat22b.model.Product;
import com.example.webshopdat22b.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    ProductRepository productRepository;

    public HomeController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("products", productRepository.getAll());
       return "index";
    }
    //fra anchor i index
    @GetMapping("/create")
    public String showCreate() {
        //vis create side
        return "create";
    }
    //fra form action
    @PostMapping("/create")
    public String createProduct(@RequestParam("product-name") String newName, @RequestParam("product-price") double newPrice) {
        //lave et nyt Product
        Product newProduct = new Product();
        newProduct.setName(newName);
        newProduct.setPrice(newPrice);

        //gem nyt produkt
        productRepository.addProduct(newProduct);
        return "redirect:/";
    }


    //vis update side for produkt ud fra parameter i url
    @GetMapping("/update/{id}")
    public String showUpdate(@PathVariable("id") int updateId, Model model) {
        //find produkt med id=updateId i database
        Product updateProduct = productRepository.findProductById(updateId);

        //tilføj produkt til viewmodel, så det kan bruges i Thymeleaf
        model.addAttribute("product", updateProduct);

        //fortæl Spring hvilken HTML-side der skal vises
        return "update";
    }

    @PostMapping("/update")
    public String updateProduct(@RequestParam("product-name") String updateName,
                                @RequestParam("product-price") double updatePrice,
                                @RequestParam("product-id") int updateID) {
        //lave produkt ud fra parametre
        Product updateProduct = new Product(updateID, updateName, updatePrice);

        //kald opdater i repository
        productRepository.updateProduct(updateProduct);

        //redigerer til oversigtssiden
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") int deleteId) {
        productRepository.deleteProduct(deleteId);
        return "redirect:/";
    }
}
