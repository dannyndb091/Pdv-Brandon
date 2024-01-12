package com.pdv.Test.Service.Others;

import com.pdv.Test.Models.DTOs.Errors.ErrorResponse;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class ErrorsList {

    public ErrorResponse getErrorMsg(String cod){
        return switch (cod.substring(4,5)){
            case "C" -> comprasError(cod);
            case "V" -> ventasError(cod);
            case "R" -> reportesError(cod);
            case "P" -> productosError(cod);
            case "X" -> clientesError(cod);
            default -> ErrBuild("ERR-U00",cod);
        };
    }

    private ErrorResponse clientesError(String cod) {
        return switch (cod.substring(5)){
            case "01" -> ErrBuild(cod,"El formato del numero telefónico es invalido.");
            case "02" -> ErrBuild(cod,"El formato de Email es invalido.");
            case "03" -> ErrBuild(cod,"El formato de código de Cliente/Proveedor es invalido.");
            case "04" -> ErrBuild(cod,"El formato del nombre del Cliente/Proveedor es invalido.");
            case "05" -> ErrBuild(cod,"Ya existe un registro de este Código de Cliente.");
            case "06" -> ErrBuild(cod,"Ya existe un registro de este Email.");
            case "07" -> ErrBuild(cod,"Ya existe un registro de este numero telefónico.");
            case "08" -> ErrBuild(cod,"Se necesita especificar el tipo Cliente/Proveedor.");
            case "09" -> ErrBuild(cod,"No se encontró el Cliente/Proveedor que se desea actualizar.");
            case "10" -> ErrBuild(cod,"No se encontró el Cliente/Proveedor que se desea eliminar.");
            case "11" -> ErrBuild(cod,"No se puede eliminar, ya que el nombre colocado no coincide con el nombre del cliente.");
            case "12" -> ErrBuild(cod,"No se puede eliminar, ya que el Cliente/Proveedor cuenta con documentos registrados.");
            case "13" -> ErrBuild(cod,"");
            case "14" -> ErrBuild(cod,"");
            case "15" -> ErrBuild(cod,"");
            case "16" -> ErrBuild(cod,"");
            case "17" -> ErrBuild(cod,"");
            case "18" -> ErrBuild(cod,"");
            case "19" -> ErrBuild(cod,"");
            case "20" -> ErrBuild(cod,"");
            case "21" -> ErrBuild(cod,"");
            case "22" -> ErrBuild(cod,"");
            case "23" -> ErrBuild(cod,"");
            case "24" -> ErrBuild(cod,"");
            case "25" -> ErrBuild(cod,"");
            case "26" -> ErrBuild(cod,"");
            case "27" -> ErrBuild(cod,"");
            case "28" -> ErrBuild(cod,"");
            case "29" -> ErrBuild(cod,"");
            case "30" -> ErrBuild(cod,"");
            case "31" -> ErrBuild(cod,"");
            case "32" -> ErrBuild(cod,"");
            case "33" -> ErrBuild(cod,"");
            case "34" -> ErrBuild(cod,"");
            case "35" -> ErrBuild(cod,"");
            case "36" -> ErrBuild(cod,"");
            case "37" -> ErrBuild(cod,"");
            case "38" -> ErrBuild(cod,"");
            case "39" -> ErrBuild(cod,"");
            case "40" -> ErrBuild(cod,"");
            case "41" -> ErrBuild(cod,"");
            case "42" -> ErrBuild(cod,"");
            case "43" -> ErrBuild(cod,"");
            case "44" -> ErrBuild(cod,"");
            case "45" -> ErrBuild(cod,"");
            case "46" -> ErrBuild(cod,"");
            case "47" -> ErrBuild(cod,"");
            case "48" -> ErrBuild(cod,"");
            case "49" -> ErrBuild(cod,"");
            case "50" -> ErrBuild(cod,"");
            default -> ErrBuild("ERR-U00",cod);
        };
    }

    private ErrorResponse productosError(String cod) {
        return switch (cod.substring(5)){
            case "01" -> ErrBuild(cod,"Ya existe un registro de este SKU, favor de colocar uno distinto.");
            case "02" -> ErrBuild(cod,"El patrón del SKU es invalido.");
            case "03" -> ErrBuild(cod,"El patrón del nombre de producto es invalido.");
            case "04" -> ErrBuild(cod,"El tipo de producto no es valido.");
            case "05" -> ErrBuild(cod,"El formato del tipo de unidad es invalido.");
            case "06" -> ErrBuild(cod,"El valor de venta de un producto no puede ser menor a $0.01 MXN.");
            case "07" -> ErrBuild(cod,"El Precio Final no puede ser distinto al Precio Neto + IVA (16%).");
            case "08" -> ErrBuild(cod,"No se permiten descuentos iguales o mayores al 90%.");
            case "09" -> ErrBuild(cod,"No se lleno el campo de estatus.");
            case "10" -> ErrBuild(cod,"No se permiten topes de descuento menores al 0%.");
            case "11" -> ErrBuild(cod,"No existe el producto que desea actualizar.");
            case "12" -> ErrBuild(cod,"El producto seleccionado no se encuentra registrado.");
            case "13" -> ErrBuild(cod,"No se puede eliminar un producto con movimientos registrados.");
            case "14" -> ErrBuild(cod,"No se puede eliminar un producto con inventario activo.");
            case "15" -> ErrBuild(cod,"");
            case "16" -> ErrBuild(cod,"");
            case "17" -> ErrBuild(cod,"");
            case "18" -> ErrBuild(cod,"");
            case "19" -> ErrBuild(cod,"");
            case "20" -> ErrBuild(cod,"");
            case "21" -> ErrBuild(cod,"");
            case "22" -> ErrBuild(cod,"");
            case "23" -> ErrBuild(cod,"");
            case "24" -> ErrBuild(cod,"");
            case "25" -> ErrBuild(cod,"");
            case "26" -> ErrBuild(cod,"");
            case "27" -> ErrBuild(cod,"");
            case "28" -> ErrBuild(cod,"");
            case "29" -> ErrBuild(cod,"");
            case "30" -> ErrBuild(cod,"");
            case "31" -> ErrBuild(cod,"");
            case "32" -> ErrBuild(cod,"");
            case "33" -> ErrBuild(cod,"");
            case "34" -> ErrBuild(cod,"");
            case "35" -> ErrBuild(cod,"");
            case "36" -> ErrBuild(cod,"");
            case "37" -> ErrBuild(cod,"");
            case "38" -> ErrBuild(cod,"");
            case "39" -> ErrBuild(cod,"");
            case "40" -> ErrBuild(cod,"");
            case "41" -> ErrBuild(cod,"");
            case "42" -> ErrBuild(cod,"");
            case "43" -> ErrBuild(cod,"");
            case "44" -> ErrBuild(cod,"");
            case "45" -> ErrBuild(cod,"");
            case "46" -> ErrBuild(cod,"");
            case "47" -> ErrBuild(cod,"");
            case "48" -> ErrBuild(cod,"");
            case "49" -> ErrBuild(cod,"");
            case "50" -> ErrBuild(cod,"");
            default -> ErrBuild("ERR-U00",cod);
        };
    }

    private ErrorResponse reportesError(String cod) {
        return switch (cod.substring(5)){
            case "01" -> ErrBuild(cod,"");
            case "02" -> ErrBuild(cod,"");
            case "03" -> ErrBuild(cod,"");
            case "04" -> ErrBuild(cod,"");
            case "05" -> ErrBuild(cod,"");
            case "06" -> ErrBuild(cod,"");
            case "07" -> ErrBuild(cod,"");
            case "08" -> ErrBuild(cod,"");
            case "09" -> ErrBuild(cod,"");
            case "10" -> ErrBuild(cod,"");
            case "11" -> ErrBuild(cod,"");
            case "12" -> ErrBuild(cod,"");
            case "13" -> ErrBuild(cod,"");
            case "14" -> ErrBuild(cod,"");
            case "15" -> ErrBuild(cod,"");
            case "16" -> ErrBuild(cod,"");
            case "17" -> ErrBuild(cod,"");
            case "18" -> ErrBuild(cod,"");
            case "19" -> ErrBuild(cod,"");
            case "20" -> ErrBuild(cod,"");
            case "21" -> ErrBuild(cod,"");
            case "22" -> ErrBuild(cod,"");
            case "23" -> ErrBuild(cod,"");
            case "24" -> ErrBuild(cod,"");
            case "25" -> ErrBuild(cod,"");
            case "26" -> ErrBuild(cod,"");
            case "27" -> ErrBuild(cod,"");
            case "28" -> ErrBuild(cod,"");
            case "29" -> ErrBuild(cod,"");
            case "30" -> ErrBuild(cod,"");
            case "31" -> ErrBuild(cod,"");
            case "32" -> ErrBuild(cod,"");
            case "33" -> ErrBuild(cod,"");
            case "34" -> ErrBuild(cod,"");
            case "35" -> ErrBuild(cod,"");
            case "36" -> ErrBuild(cod,"");
            case "37" -> ErrBuild(cod,"");
            case "38" -> ErrBuild(cod,"");
            case "39" -> ErrBuild(cod,"");
            case "40" -> ErrBuild(cod,"");
            case "41" -> ErrBuild(cod,"");
            case "42" -> ErrBuild(cod,"");
            case "43" -> ErrBuild(cod,"");
            case "44" -> ErrBuild(cod,"");
            case "45" -> ErrBuild(cod,"");
            case "46" -> ErrBuild(cod,"");
            case "47" -> ErrBuild(cod,"");
            case "48" -> ErrBuild(cod,"");
            case "49" -> ErrBuild(cod,"");
            case "50" -> ErrBuild(cod,"");
            default -> ErrBuild("ERR-U00",cod);
        };

    }

    private ErrorResponse ventasError(String cod) {
        return switch (cod.substring(5)){
            case "01" -> ErrBuild(cod,"No se puede abrir un nuevo corte mientras exista otro abierto.");
            case "02" -> ErrBuild(cod,"Ya existe un corte abierto/cerrado el día de hoy.");
            case "03" -> ErrBuild(cod,"Error interno: No pueden existir ventas del día, previo a abrir el corte de caja.");
            case "04" -> ErrBuild(cod,"No se puede inicializar una caja con efectivo negativo.");
            case "05" -> ErrBuild(cod,"No existen cortes pendientes por cerrar.");
            case "06" -> ErrBuild(cod,"Error relacional, existe mas de 1 corte abierto.");
            case "07" -> ErrBuild(cod,"No se puede cerrar el corte mientras existan ventas pendientes de cerrar.");
            case "08" -> ErrBuild(cod,"La suma de los pagos registrados en el corte es distinta a la suma de los importes de los documentos de venta.");
            case "09" -> ErrBuild(cod,"Favor de aperturar la caja antes de realizar ventas.");
            case "10" -> ErrBuild(cod,"No se pueden agregar mas ventas, hasta cerrar el corte anterior y aperturar el nuevo.");
            case "11" -> ErrBuild(cod,"No se proporciono un Código de Cliente valido.");
            case "12" -> ErrBuild(cod,"No se encontró al Cliente/Proveedor especificado.");
            case "13" -> ErrBuild(cod,"El cliente no se encuentra activo para venta.");
            case "14" -> ErrBuild(cod,"El código especificado no corresponde a un cliente.");
            case "15" -> ErrBuild(cod,"No se puede abrir otro documento de venta, el cliente ya cuenta con uno abierto.");
            case "16" -> ErrBuild(cod,"No existe el documento especificado para la adición de productos.");
            case "17" -> ErrBuild(cod,"No se pueden cargar productos a documentos de venta finalizados.");
            case "18" -> ErrBuild(cod,"No se pueden cargar productos a documentos de venta cancelados.");
            case "19" -> ErrBuild(cod,"Para cargar productos de venta, es necesario que el documento sea de venta.");
            case "20" -> ErrBuild(cod,"El cliente del documento de venta no existe, no se podrán agregar productos.");
            case "21" -> ErrBuild(cod,"El cliente especificado no corresponde al cargado en la nota de venta.");
            case "22" -> ErrBuild(cod,"El producto especificado no existe.");
            case "23" -> ErrBuild(cod,"El producto se encuentra actualmente inactivo, por lo que no se puede ");
            case "24" -> ErrBuild(cod,"Este producto ya se encuentra cargado al documento de venta.");
            case "25" -> ErrBuild(cod,"El producto no cuenta con un inventario activo.");
            case "26" -> ErrBuild(cod,"No se cuenta con existencia suficiente.");
            case "27" -> ErrBuild(cod,"El producto sufrió un cambio de precio, favor de intentar cargarlo de nuevo.");
            case "28" -> ErrBuild(cod,"El descuento realizado es mayor al permitido, favor de recalcularlo.");
            case "29" -> ErrBuild(cod,"El precio unitario no corresponde al del producto, favor de eliminarlo y cargarlo de nuevo.");
            case "30" -> ErrBuild(cod,"El importe de Neto no se encuentra calculado correctamente.");
            case "31" -> ErrBuild(cod,"El importe de subtotal no se encuentra calculado correctamente.");
            case "32" -> ErrBuild(cod,"El importe de los impuestos no se encuentra calculado correctamente.");
            case "33" -> ErrBuild(cod,"El importe total del movimiento se encuentra mal calculado.");
            case "34" -> ErrBuild(cod,"La cantidad de producto a agregar debe ser de mínimo 1 articulo.");
            case "35" -> ErrBuild(cod,"Los importes colocados no son un tipo de datos correcto, o inferiores al mínimo permitido $0.00.");
            case "36" -> ErrBuild(cod,"No existe el documento especificado para la sustracción de productos.");
            case "37" -> ErrBuild(cod,"No se puede modificar un documento de venta completado.");
            case "38" -> ErrBuild(cod,"No se puede modificar un documento de venta cancelado");
            case "39" -> ErrBuild(cod,"El documento especificado no corresponde a un documento de venta.");
            case "40" -> ErrBuild(cod,"No existe el movimiento especificado en el documento de venta.");
            case "41" -> ErrBuild(cod,"El SKU no corresponde al producto del movimiento.");
            case "42" -> ErrBuild(cod,"La verificación de unidades no fue correcta, favor de validar refrescar el documento.");
            case "43" -> ErrBuild(cod,"No existe el documento que se desea cerrar.");
            case "44" -> ErrBuild(cod,"No se puede cerrar un documento sin movimientos registrados.");
            case "45" -> ErrBuild(cod,"El pago efectuado es distinto al importe total de la venta, favor de verificarlo.");
            case "46" -> ErrBuild(cod,"No se puede completar una venta que ya cuenta con un pago registrado.");
            case "47" -> ErrBuild(cod,"No se puede cerrar la venta si uno de los movimientos esta marcado como completado.");
            case "48" -> ErrBuild(cod,"Incongruencia en los cálculos entre los movimientos y el documento.");
            case "49" -> ErrBuild(cod,"Incongruencia en los cálculos enviados y los registrados en el documento.");
            case "50" -> ErrBuild(cod,"No se pueden modificar los movimientos de un documento con fecha distinta al del corte.");
            case "51" -> ErrBuild(cod,"Datos de la solicitud incorrectos.");
            case "52" -> ErrBuild(cod,"No se encontró el corte del día de la venta.");
            case "53" -> ErrBuild(cod,"El corte del día de este documento se encuentra cerrado, no se puede cerrar la venta.");
            default -> ErrBuild("ERR-U00",cod);
        };
    }

    private ErrorResponse comprasError(String cod) {
        return switch (cod.substring(5)){
            case "00" -> ErrBuild(cod,"No se pueden crear compras de otros días");
            case "01" -> ErrBuild(cod,"No existe el proveedor buscado.");
            case "02" -> ErrBuild(cod,"No se puede obtener el Documento con el Numero de ID especificado.");
            case "03" -> ErrBuild(cod,"El Proveedor especificado solo es cliente.");
            case "04" -> ErrBuild(cod,"No se especifico fecha para el documento.");
            case "05" -> ErrBuild(cod,"No se especifico el código del proveedor.");
            case "06" -> ErrBuild(cod,"No se pueden agregar productos a un documento cancelado.");
            case "07" -> ErrBuild(cod,"No se pueden agregar productos a un documento completado.");
            case "08" -> ErrBuild(cod,"No se pueden agregar productos de compra a un documento de venta.");
            case "09" -> ErrBuild(cod,"No existe el producto seleccionado.");
            case "10" -> ErrBuild(cod,"No se puede agregar el producto a la venta, puesto se encuentra desactivado.");
            case "11" -> ErrBuild(cod,"El producto seleccionado no cuenta con existencia.");
            case "12" -> ErrBuild(cod,"No se cuenta con existencia suficiente.");
            case "13" -> ErrBuild(cod,"Error al crear nuevo inventario para el producto.");
            case "14" -> ErrBuild(cod,"No se pueden comprar productos del tipo servicio.");
            case "15" -> ErrBuild(cod,"Incongruencia en los cálculos del movimiento/documento.");
            case "16" -> ErrBuild(cod,"Ya existe una partida del producto, previamente cargado en el documento.");
            case "17" -> ErrBuild(cod,"No se puede modificar un documento completado.");
            case "18" -> ErrBuild(cod,"No se pueden eliminar productos de venta en un documento de compra.");
            case "19" -> ErrBuild(cod,"El movimiento del producto no se encuentra asociado al documento.");
            case "20" -> ErrBuild(cod,"No se puede modificar un movimiento completado.");
            case "21" -> ErrBuild(cod,"No existe el inventario del producto, no se puede modificar.");
            case "22" -> ErrBuild(cod,"No se pueden eliminar productos de un documento cancelado.");
            case "23" -> ErrBuild(cod,"No se puede completar una compra cancelada.");
            case "24" -> ErrBuild(cod,"No se puede cerrar nuevamente una compra ya cerrada.");
            case "25" -> ErrBuild(cod,"El documento especificado no corresponde a un documento de compra.");
            case "26" -> ErrBuild(cod,"El importe de los pagos no corresponde con el total de la compra.");
            case "27" -> ErrBuild(cod,"El documento de compra no cuenta con movimientos para registrar.");
            case "28" -> ErrBuild(cod,"La compra ya cuenta con un pago previamente registrado.");
            case "29" -> ErrBuild(cod,"La suma de los movimientos no coincide con los importes del documento.");
            case "30" -> ErrBuild(cod,"No se puede crear otro documento de compra si ya se cuenta con uno activo para este proveedor.");
            case "31" -> ErrBuild(cod,"El descuento no puede se mayor al neto del movimiento.");
            case "32" -> ErrBuild(cod,"Solo se pueden cancelar documentos de compra.");
            case "33" -> ErrBuild(cod,"Solo se pueden cancelar compras con estatus vigente y completada.");
            case "34" -> ErrBuild(cod,"Hay un problema con el pago de la compra, no se encuentra activo.");
            case "35" -> ErrBuild(cod,"La compra no cuenta con pago registrado.");
            case "36" -> ErrBuild(cod,"El documento de compra no cuenta con movimientos.");
            case "37" -> ErrBuild(cod,"No se pueden cancelar movimientos previamente cancelados.");
            case "38" -> ErrBuild(cod,"No se puede cancelar un movimiento sin la existencia suficiente disponible.");
            case "39" -> ErrBuild(cod,"El proveedor no se encuentra activo.");
            case "40" -> ErrBuild(cod,"");
            case "41" -> ErrBuild(cod,"");
            case "42" -> ErrBuild(cod,"");
            case "43" -> ErrBuild(cod,"");
            case "44" -> ErrBuild(cod,"");
            case "45" -> ErrBuild(cod,"");
            case "46" -> ErrBuild(cod,"");
            case "47" -> ErrBuild(cod,"");
            case "48" -> ErrBuild(cod,"");
            case "49" -> ErrBuild(cod,"");
            case "50" -> ErrBuild(cod,"");
            default -> ErrBuild("ERR-U00",cod);
        };
    }

    private ErrorResponse ErrBuild(String err, String msg){
        return ErrorResponse.builder().err(err).Message(msg).build();
    }
}
