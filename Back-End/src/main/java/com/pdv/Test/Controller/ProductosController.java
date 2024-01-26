package com.pdv.Test.Controller;

import com.pdv.Test.Models.DTOs.Productos.UpdateProduct;
import com.pdv.Test.Models.Productos;
import com.pdv.Test.Service.Others.ErrorsList;
import com.pdv.Test.Service.ProductosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost")
public class ProductosController {
    private final ProductosService productosService;
    private final ErrorsList errors;

    @PostMapping(value = "new")
    public ResponseEntity<Object> newProduct(@RequestBody Productos pro){
        try {
            return productosService.newProduct(pro);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @PutMapping(value = "update")
    public ResponseEntity<Object> updateProduct(@RequestBody UpdateProduct pro){
        try {
            return productosService.updateProduct(pro);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<Object> deleteProduct(@RequestBody Productos pro){
        try {
            return productosService.deleteProduct(pro);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @GetMapping(value = "get/selectableToSell/")
    public ResponseEntity<Object> selectableToSell(){
        try {
            return productosService.selectableToSell();
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }
}
