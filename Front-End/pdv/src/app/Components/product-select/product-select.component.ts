import { ChangeDetectorRef, Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { SelectProduct } from 'src/app/DTOs/Productos/select-product';
import { ProductsService } from 'src/app/Services/products.service';
import * as $ from 'jquery';
import 'datatables.net';
import 'datatables.net-bs4';
import Swal from 'sweetalert2';
import { Mov } from 'src/app/DTOs/Documentos/mov';
import { AddItem } from './add-item';
import { DocumentsService } from 'src/app/Services/documents.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ErrorData } from 'src/app/DTOs/Otros/error-data';

@Component({
  selector: 'app-product-select',
  templateUrl: './product-select.component.html',
  styleUrls: ['./product-select.component.css']
})
export class ProductSelectComponent {
  dialog: any;
  products: SelectProduct[] | null = null;
  dataTable: any = null;
  selectedProd: boolean | any;
  prod: SelectProduct = new SelectProduct("", "", "", 0, 0, 0, 0, 0, 0);
  discountSelected: boolean[] = [true, false, false, false, false];
  discount: number = 0;
  inputValue: number | any;
  qtyProduct: number = 0;
  mov: Mov = new Mov(0, 0, "", "", 0, 0, 0, 0, 0, 0, 0, 0, false);
  btnAtras: boolean;

  constructor(public dialogRef: MatDialogRef<ProductSelectComponent>, private chRef: ChangeDetectorRef, private proServ: ProductsService,
    @Inject(MAT_DIALOG_DATA) public data: any, private docSer: DocumentsService) {
    console.log(data);
    if (this.data[3] == null) {
      this.btnAtras = true;
      this.selectedProd = false;
      this.cargarProductos();
      this.selectedProd = false;
    } else {
      this.btnAtras = false;
      this.selectedProd = true;
      this.cargarMov(data[1], data[3]);
    }
  }

  cargarProductos() {
    console.log("Inicia Cargar Clientes");
    this.proServ.getProductsSell().subscribe(
      data => {
        this.products = data.body;
        console.log(this.products);

        this.chRef.detectChanges();

        const table: any = $('#ProductsTable');
        this.dataTable = table.DataTable({
          lengthMenu: [15],
          pageLength: 15,
          order: [[0, 'desc']],
          language: {
            "sProcessing": "Procesando...",
            "sLengthMenu": "Mostrar _MENU_ registros por pagina.",
            "sZeroRecords": "No se encontraron registros.",
            "sInfo": "Mostrando de _START_ a _END_ de _TOTAL_ registros.",
            "sInfoEmpty": "Mostrando 0 de 0 de 0 registros.",
            "sInfoFiltered": "(filtrado de _MAX_ registros totales)",
            "sInfoPostFix": "",
            "sSearch": "Buscar:",
            "sUrl": "",
            "sInfoThousands": ",",
            "sLoadingRecords": "Cargando...",
            "oPaginate": {
              "sFirst": "Primero",
              "sLast": "Último",
              "sNext": "Siguiente",
              "sPrevious": "Anterior"
            },
            "oAria": {
              "sSortAscending": ": Activar para ordenar la columna de manera ascendente",
              "sSortDescending": ": Activar para ordenar la columna de manera descendente"
            }
          }
        });
      },
      error => {
        console.log("Error: " + error);
      }
    );
  }

  selectProduct(pro: SelectProduct) {
    this.prod = pro;
    this.selectedProd = true;
    this.qtyProduct = 0;
    this.discount = 0;
    this.discountSelected = [true, false, false, false, false]
    this.mov.proCode = pro.proCode;
    this.mov.proName = pro.proName;
    this.mov.movPu = pro.proPrice;
    this.calculateMov();
  }

  closeDialog() {
    this.dialogRef.close(undefined);
  }

  deselectPro() {
    this.selectedProd = false;
    this.inputValue = 0;
  }

  changeDisc(n: number) {
    if (n > (this.prod.proMaxPercDisc * 100)) {
      Swal.fire("Error", "El descuento seleccionado no puede ser mayor al permitido por el producto.");
    } else {
      switch (n) {
        case 0:
          this.changeDiscFalse(this.discount);
          this.discountSelected[0] = true;
          break;
        case 5:
          this.changeDiscFalse(this.discount);
          this.discountSelected[1] = true;
          break;
        case 10:
          this.changeDiscFalse(this.discount);
          this.discountSelected[2] = true;
          break;
        case 15:
          this.changeDiscFalse(this.discount);
          this.discountSelected[3] = true;
          break;
        case 20:
          this.changeDiscFalse(this.discount);
          this.discountSelected[4] = true;
          break;
      }
      this.discount = n;
      this.calculateMov();
    }
    console.log(this.discountSelected);
  }

  changeDiscFalse(n: number) {
    switch (n) {
      case 0:
        this.discountSelected[0] = false;
        break;
      case 5:
        this.discountSelected[1] = false;
        break;
      case 10:
        this.discountSelected[2] = false;
        break;
      case 15:
        this.discountSelected[3] = false;
        break;
      case 20:
        this.discountSelected[4] = false;
        break;
    }
  }

  paintDisc(n: number) {
    if (this.discountSelected[n]) return "btn btn-success";
    return "btn btn-light";
  }

  onlyNumbers(event: KeyboardEvent): void {
    const keyCode = event.keyCode;

    if ((keyCode < 48 || keyCode > 57) && keyCode !== 8) {
      event.preventDefault();
    }
  }

  confirmInventory() {
    if (this.inputValue != null) {
      if (this.inputValue <= this.prod.invQty) {
        console.log(this.inputValue + " : " + this.prod.invQty);
        this.qtyProduct = this.inputValue;
        this.calculateMov();
      } else {
        this.inputValue = this.qtyProduct;
        Swal.fire("Error", "No se puede vender mas del producto disponible en inventario.");
      }
    } else {
      this.qtyProduct = 0;
      this.calculateMov();
    }
  }

  calculateMov() {
    this.mov.proQty = this.qtyProduct;
    this.mov.movNet = this.mov.movPu * this.mov.proQty;
    this.mov.movDiscount = this.mov.movNet * (this.discount / 100);
    this.mov.movSubtotal = this.mov.movNet - this.mov.movDiscount;
    this.mov.movTax = this.mov.movSubtotal * 0.16;
    this.mov.movTotal = this.mov.movSubtotal + this.mov.movTax;
    console.log(this.mov);
  }

  saveMov() {
    const addItem: AddItem = new AddItem(this.data[1], this.data[0], this.mov.proCode, this.mov.proQty, this.mov.movPu,
      this.mov.movNet, this.mov.movDiscount, this.mov.movSubtotal, this.mov.movTax, this.mov.movTotal, 0)
    
    if (this.data[3] == null){
      this.docSer.addItem(addItem).subscribe(
        result => {
          const final: Mov = result as Mov;
          this.mov.movType = this.data[2];
          this.mov.movLine = final.movLine;
          this.dialogRef.close(this.mov);
        },
        (error: HttpErrorResponse) => {
          console.log(error);
          const errorData: ErrorData = error.error;
          Swal.fire("¡Error!", errorData.err + " : " + errorData.message, 'error');
        }
      )
    } else {
      addItem.movLine = this.data[3];
      this.docSer.modifyItem(addItem).subscribe(
        () => {
          this.mov.movType = this.data[2];
          this.dialogRef.close(this.mov);
        },
        (error: HttpErrorResponse) => {
          console.log(error);
          const errorData: ErrorData = error.error;
          Swal.fire("¡Error!", errorData.err + " : " + errorData.message, 'error');
        }
      )
    }
  }

  cargarMov(invoice: number, movLine: number) {
    this.docSer.obtenerMov(invoice, movLine).subscribe(
      result => {
        this.mov = result.body as Mov;
        console.log(this.mov);
        this.docSer.obtenerMovProd(this.mov.proCode).subscribe(
          resultProd => {
            this.prod = resultProd.body as SelectProduct;
            this.prod.invQty += this.mov.proQty;
            this.prod.invRes -= this.mov.proQty;
            this.inputValue = this.mov.proQty;
            this.qtyProduct = this.mov.proQty;
          },
          (error: HttpErrorResponse) => {
            console.log(error);
            const errorData: ErrorData = error.error;
            Swal.fire("¡Error!", errorData.err + " : " + errorData.message, 'error').then(
              () => {this.dialogRef.close(undefined)}
            );
          }
        )
      },
      (error: HttpErrorResponse) => {
        console.log(error);
        const errorData: ErrorData = error.error;
        Swal.fire("¡Error!", errorData.err + " : " + errorData.message, 'error').then(
          () => {this.dialogRef.close(undefined)}
        );
      }
    )
  }
}
