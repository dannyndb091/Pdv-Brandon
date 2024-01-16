package com.pdv.Test.Controller;

import com.pdv.Test.Models.DTOs.Inventario.DcrAddItem;
import com.pdv.Test.Models.DTOs.Inventario.DcrCloseDoc;
import com.pdv.Test.Models.DTOs.Inventario.DcrNewDoc;
import com.pdv.Test.Service.InventarioService;
import com.pdv.Test.Service.Others.ErrorsList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/decrease")
@RequiredArgsConstructor
public class InventarioController {
    private final InventarioService inventarioService;
    private final ErrorsList errors;

    @PostMapping(value = "new")
    public ResponseEntity<Object> createDecrease(){
        try {
            return inventarioService.createDecrease();
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @PostMapping(value = "addItem")
    public ResponseEntity<Object> addItem(@RequestBody DcrAddItem addItem){
        try {
            return inventarioService.addItem(addItem,false);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @PostMapping(value = "close")
    public ResponseEntity<Object> closeDecrease(@RequestBody DcrCloseDoc closeBuy){
        try {
            return inventarioService.closeDecrease(closeBuy);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @DeleteMapping(value = "delItem")
    public ResponseEntity<Object> delItem(@RequestBody DcrAddItem addItem){
        try {
            return inventarioService.delItem(addItem);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @DeleteMapping(value = "delDcr")
    public ResponseEntity<Object> delDecrease(@RequestBody DcrNewDoc dcrDoc){
        try {
            return inventarioService.deleteDecrease(dcrDoc);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @PutMapping(value = "cancelDcr")
    public ResponseEntity<Object> cancelDecrease(@RequestBody DcrNewDoc dcrDoc){
        try {
            return inventarioService.cancelDecrease(dcrDoc);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @PutMapping(value = "updateItem")
    public ResponseEntity<Object> updateItem(@RequestBody DcrAddItem addItem){
        try {
            return inventarioService.addItem(addItem,true);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }
}
