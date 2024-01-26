package com.pdv.Test.Controller;

import com.pdv.Test.Models.DTOs.Ventas.VtaAddItem;
import com.pdv.Test.Models.DTOs.Ventas.VtaCloseSell;
import com.pdv.Test.Models.DTOs.Ventas.VtaNewDoc;
import com.pdv.Test.Service.Others.ErrorsList;
import com.pdv.Test.Service.VentasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sells")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost")
public class VentasController {
    private final VentasService ventasService;
    private final ErrorsList errors;

    @PostMapping(value = "new")
    public ResponseEntity<Object> createSellDoc(@RequestBody VtaNewDoc vta){
        try {
            return ventasService.createSellDoc(vta);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @PostMapping(value = "addItem")
    public ResponseEntity<Object> addSellItem(@RequestBody VtaAddItem addItem){
        try {
            return ventasService.addSellItem(addItem, false);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @PostMapping(value = "close")
    public ResponseEntity<Object> closeSellDoc(@RequestBody VtaCloseSell closeSell){
        try {
            return ventasService.closeSellDoc(closeSell);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @DeleteMapping(value = "delItem")
    public ResponseEntity<Object> delSellItem(@RequestBody VtaAddItem addItem){
        try {
            return ventasService.delSellItem(addItem);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<Object> deleteSellDoc(@RequestBody VtaNewDoc docDelete){
        try {
            return ventasService.deleteSellDoc(docDelete);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @PutMapping(value = "cancel")
    public ResponseEntity<Object> cancelSellDoc(@RequestBody VtaNewDoc docCancel){
        try {
            return ventasService.cancelSellDoc(docCancel);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @PutMapping(value = "updateItem")
    public ResponseEntity<Object> updateItem(@RequestBody VtaAddItem addItem){
        try {
            return ventasService.addSellItem(addItem, true);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @GetMapping(value = "get/all")
    public ResponseEntity<Object> getAll(){
        try {
            return ventasService.getAllDocs();
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @GetMapping(value = "get/mov/{invoice}/{movLine}")
    public ResponseEntity<Object> getMov(@PathVariable Integer invoice, @PathVariable Integer movLine){
        try {
            return ventasService.getMov(invoice, movLine);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @GetMapping(value = "get/mov/{proSku}")
    public ResponseEntity<Object> getMovProd(@PathVariable String proSku){
        try {
            return ventasService.getMovProd(proSku);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @GetMapping(value = "get/doc/{invoice}")
    public ResponseEntity<Object> getDocDetail(@PathVariable Integer invoice){
        try {
            return ventasService.getDocDetail(invoice);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }
}
