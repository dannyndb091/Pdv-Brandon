package com.pdv.Test.Controller;

import com.pdv.Test.Models.DTOs.Compras.CprAddItem;
import com.pdv.Test.Models.DTOs.Compras.CprCloseBuy;
import com.pdv.Test.Models.DTOs.Compras.CprNewDoc;
import com.pdv.Test.Service.Others.ErrorsList;
import com.pdv.Test.Service.ComprasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/buys")
@RequiredArgsConstructor
public class ComprasController {
    private final ComprasService comprasService;
    private final ErrorsList errors;

    @PostMapping(value = "new")
    public ResponseEntity<Object> createBuyDoc(@RequestBody CprNewDoc cprNewDoc){
        try {
            return comprasService.createBuyDoc(cprNewDoc);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @PostMapping(value = "addItem")
    public ResponseEntity<Object> addItem(@RequestBody CprAddItem addItem){
        try {
            return comprasService.addItem(addItem, false);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @PostMapping(value = "closeBuy")
    public ResponseEntity<Object> closeBuy(@RequestBody CprCloseBuy closeBuy){
        try {
            return comprasService.closeBuy(closeBuy);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @DeleteMapping(value = "delItem")
    public ResponseEntity<Object> delItem(@RequestBody CprAddItem addItem){
        try {
            return comprasService.delItem(addItem);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @DeleteMapping(value = "delBuy")
    public ResponseEntity<Object> delBuy(@RequestBody CprNewDoc buyDoc){
        try {
            return comprasService.delBuy(buyDoc);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @PutMapping(value = "cancelBuy")
    public ResponseEntity<Object> cancelBuy(@RequestBody CprAddItem addItem){
        try {
            return comprasService.cancelBuy(addItem);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @PutMapping(value = "updateItem")
    public ResponseEntity<Object> updateItem(@RequestBody CprAddItem addItem){
        try {
            return comprasService.addItem(addItem, true);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }
}
