package com.example.produitservice.controller;

import com.example.produitservice.model.Produit;
import com.example.produitservice.service.IProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/produits")
public class ProduitController {

    @Autowired
    private IProduitService produitService;

    @PostMapping
    public EntityModel<Produit> createProduit(@RequestBody Produit produit) {
        Produit saved = produitService.createProduit(produit);
        return toModel(saved);
    }

    @GetMapping
    public CollectionModel<EntityModel<Produit>> getAllProduits() {
        List<Produit> produits = produitService.getAllProduits();

        List<EntityModel<Produit>> produitModels = produits.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(produitModels,
                linkTo(methodOn(ProduitController.class).getAllProduits()).withSelfRel());
    }


    @GetMapping("/{id}")
    public EntityModel<Produit> getProduitById(@PathVariable Long id) {
        Produit produit = produitService.getProduitById(id);
        return toModel(produit);
    }


    @PutMapping("/{id}")
    public EntityModel<Produit> modifierProduit(@PathVariable Long id, @RequestBody Produit produit) {
        Produit updated = produitService.modifierProduit(id, produit);
        return toModel(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerProduit(@PathVariable Long id) {
        produitService.supprimerProduit(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<Produit> toModel(Produit produit) {
        return EntityModel.of(produit,
                linkTo(methodOn(ProduitController.class).getProduitById(produit.getId())).withSelfRel(),
                linkTo(methodOn(ProduitController.class).getAllProduits()).withRel("all-produits"),
                linkTo(methodOn(ProduitController.class).modifierProduit(produit.getId(), produit)).withRel("update"),
                linkTo(methodOn(ProduitController.class).supprimerProduit(produit.getId())).withRel("delete")
        );
    }
}
