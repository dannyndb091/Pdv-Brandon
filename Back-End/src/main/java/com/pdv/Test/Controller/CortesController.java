package com.pdv.Test.Controller;

import com.pdv.Test.Models.DTOs.Cortes.CortesApertura;
import com.pdv.Test.Service.CortesService;
import com.pdv.Test.Service.Others.ErrorsList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/arch")
@RequiredArgsConstructor
public class CortesController {
    private final CortesService cortesService;
    private final ErrorsList errors;

    @PostMapping(value = "create")
    public ResponseEntity<Object> createArch(@RequestBody CortesApertura apert){
        try {
            return cortesService.createArch(apert);
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }

    @PostMapping(value = "close")
    public ResponseEntity<Object> closeArch(){
        try {
            return cortesService.closeArch();
        }
        catch (Exception e){
            String error = e.getMessage().split("[«»]")[1];
            return ResponseEntity.badRequest().body(errors.getErrorMsg(error));
        }
    }
}
