package com.pdv.Test.Service;

import com.pdv.Test.Repository.DocumentosRepository;
import com.pdv.Test.Repository.InventarioRepository;
import com.pdv.Test.Repository.MovimientosRepository;
import com.pdv.Test.Repository.ProductosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventarioService {
    private final DocumentosRepository docRep;
    private final InventarioRepository invRep;
    private final ProductosRepository proRep;
    private final MovimientosRepository movRep;


}
