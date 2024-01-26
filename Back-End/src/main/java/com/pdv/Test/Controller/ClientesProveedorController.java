package com.pdv.Test.Controller;

import com.pdv.Test.Models.ClienteProveedor;
import com.pdv.Test.Models.DTOs.Clientes.UpdateClient;
import com.pdv.Test.Service.Others.ErrorsList;
import com.pdv.Test.Service.ClientesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost")
public class ClientesProveedorController {
    private final ClientesService clientesService;
    private final ErrorsList errors;

    @PostMapping(value = "new")
    public ResponseEntity<Object> newClient(@RequestBody ClienteProveedor cli){
        try {
            return clientesService.newClient(cli);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @PutMapping(value = "update")
    public ResponseEntity<Object> updateClient(@RequestBody UpdateClient uCli){
        try {
            return clientesService.updateClient(uCli);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @DeleteMapping(value = "delete")
    public ResponseEntity<Object> deleteClient(@RequestBody ClienteProveedor cli){
        try {
            return clientesService.delClient(cli);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @GetMapping(value = "get/toSell")
    public ResponseEntity<Object> getToSell(){
        try {
            return clientesService.getToSell();
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }
}
