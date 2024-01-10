package com.pdv.Test.Controller;

import com.pdv.Test.Models.DTOs.Compras.CprAddItem;
import com.pdv.Test.Models.DTOs.Compras.CprNewDoc;
import com.pdv.Test.Service.ComprasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/buys")
@RequiredArgsConstructor
public class ComprasController {
    private final ComprasService comprasService;

    @PostMapping(value = "new")
    public ResponseEntity<Object> createBuyDoc(@RequestBody CprNewDoc cprNewDoc){
        try {
            return comprasService.createBuyDoc(cprNewDoc);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return switch (error) {
                case "ERR-C02" -> comprasService.getOpenDoc(cprNewDoc);
                case "const_assigned" -> ResponseEntity.badRequest().body("Error.");
                default -> ResponseEntity.badRequest().body(e.getMessage());
            };
        }
    }

    @PostMapping(value = "addItem")
    public ResponseEntity<Object> addItem(@RequestBody CprAddItem addItem){
        try {
            return comprasService.addItem(addItem);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return switch (error) {
                //case "ERR-C02" -> comprasService.getOpenDoc(addItem);
                case "const_assigned" -> ResponseEntity.badRequest().body("Error.");
                default -> ResponseEntity.badRequest().body(e.getMessage());
            };
        }
    }

}
