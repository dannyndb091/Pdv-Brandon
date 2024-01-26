import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { DocumentDetailed } from 'src/app/DTOs/Documentos/document-detailed';
import { OpenDocument } from 'src/app/DTOs/Documentos/open-document';
import { ClientSelectComponent } from '../client-select/client-select.component';
import { SelectClient } from 'src/app/DTOs/Clientes/select-client';
import Swal from 'sweetalert2';
import { DocumentsService } from 'src/app/Services/documents.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorData } from 'src/app/DTOs/Otros/error-data';
import { DocGetNew } from 'src/app/DTOs/Documentos/doc-get-new';
import { ProductSelectComponent } from '../product-select/product-select.component';
import { Mov } from 'src/app/DTOs/Documentos/mov';
import { DelItem } from 'src/app/DTOs/Documentos/del-item';
import { AlertsService } from 'src/app/Services/alerts.service';
import { CancelDoc } from 'src/app/DTOs/Documentos/cancel-doc';

@Component({
  selector: 'app-document-detailed',
  templateUrl: './document-detailed.component.html',
  styleUrls: ['./document-detailed.component.css']
})
export class DocumentDetailedComponent {
  document: DocumentDetailed;
  windowTitle: string | any;
  clientText: string | any;
  cli: SelectClient | null = null;

  constructor(public dialogRef: MatDialogRef<DocumentDetailedComponent>, private docSer: DocumentsService, private alerts: AlertsService,
    private docService: DocumentsService, @Inject(MAT_DIALOG_DATA) public data: OpenDocument, private dialog: MatDialog) {
    this.document = new DocumentDetailed(0, new Date, "", "", new Array<Mov>, 0, 0, 0, 0, 0, 0, true, false, 0)
    console.log(data);
    if (data.newDoc) {
      switch (data.type) {
        case 0:
          break;
        case 1:
          this.windowTitle = "Venta";
          this.clientText = "Cliente";
          console.log(this.document.clCode);
          break;
        case 2:
          break;
      }
    } else {
      switch (data.type) {
        case 0:
          break;
        case 1:
          this.windowTitle = "Venta";
          this.clientText = "Cliente";
          this.cargarDoc();
          break;
        case 2:
          break;
      }
    }
  }

  selectClient() {
    if (this.document.invoice == 0) {
      let dialogRef = this.dialog.open(ClientSelectComponent, {
        minHeight: '700px',
        width: '70vw',
        data: 0,
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result == undefined) {
          Swal.fire("Operación cancelada", "No se selecciono ningun cliente");
        } else {
          this.cli = new SelectClient(result.clCode, result.clName, result.clMail, result.clCellphone);
          console.log(this.cli);
          this.docSer.createSell(this.cli).subscribe(
            (result) => {
              console.log(result);
              let newDoc: DocGetNew = result as DocGetNew;
              this.document.clCode = this.cli?.clCode as string;
              this.document.clName = this.cli?.clName as string;
              this.document.docCompleted = false;
              this.document.docStatus = true;
              this.document.invoice = newDoc.invoice;
              this.document.docDate = new Date("2024-01-23");
              this.document.docDiscount = 0;
              this.document.docNet = 0;
              this.document.docSubtotal = 0;
              this.document.docTax = 0;
              this.document.docTotal = 0;
              this.document.docProductQty = 0;
              this.document.docType = 2;
            },
            (error: HttpErrorResponse) => {
              console.log(error);
              const errorData: ErrorData = error.error;
              Swal.fire("¡Error!", errorData.err + " : " + errorData.message, 'error');
            }
          );
        }
      }
      );
    }
  }

  addProduct() {
    if (this.document.invoice != undefined) {
      let dialogRef = this.dialog.open(ProductSelectComponent, {
        minHeight: '700px',
        width: '70vw',
        data: [this.document.clCode, this.document.invoice, 2, null]
      });

      dialogRef.afterClosed().subscribe(
        result => {
          if (result == undefined) {
            Swal.fire("Operación cancelada", "No se selecciono ningun producto");
          } else {
            console.log(result);
            this.document.movs.push(result as Mov);
            console.log(this.document);
            this.calcDoc(1, result as Mov);
          }
        },
        (error: HttpErrorResponse) => {
          console.log(error);
          const errorData: ErrorData = error.error;
          Swal.fire("¡Error!", errorData.err + " : " + errorData.message, 'error');
        }
      );
    }
  }

  delMov(m: Mov, i: number) {
    this.docSer.deleteItem(new DelItem(this.document.invoice, m.proCode, m.movLine, m.proQty)).subscribe(
      () => {
        this.calcDoc(2, this.document.movs[i]);
        this.document.movs.splice(i, 1);
        Swal.fire("Correcto!", "Producto eliminado correctamente.", 'success');
      },
      (error: HttpErrorResponse) => {
        console.log(error);
        const errorData: ErrorData = error.error;
        Swal.fire("¡Error!", errorData.err + " : " + errorData.message, 'error');
      }
    )
  }

  modifyMov(m: Mov, i: number) {
    if (this.document.invoice != undefined) {
      let dialogRef = this.dialog.open(ProductSelectComponent, {
        minHeight: '700px',
        width: '70vw',
        data: [this.document.clCode, this.document.invoice, 2, m.movLine]
      });

      dialogRef.afterClosed().subscribe(
        result => {
          if (result == undefined) {
            Swal.fire("Operación cancelada", "No se modifico el producto");
          } else {
            this.calcDoc(2, this.document.movs[i]);
            console.log(result);
            this.document.movs[i] = result as Mov;
            console.log(this.document);
            this.calcDoc(1, result as Mov);
          }
        },
        (error: HttpErrorResponse) => {
          console.log(error);
          const errorData: ErrorData = error.error;
          Swal.fire("¡Error!", errorData.err + " : " + errorData.message, 'error');
        }
      );
    }
  }

  calcDoc(i: number, m: Mov) {
    switch (i) {
      case 1: //Agregar datos del Mov
        this.document.docProductQty += m.proQty;
        this.document.docNet += m.movNet;
        this.document.docDiscount += m.movDiscount;
        this.document.docSubtotal += m.movSubtotal;
        this.document.docTax += m.movTax;
        this.document.docTotal += m.movTotal;
        break;
      case 2: //Eliminar datos del Mov
        this.document.docProductQty -= m.proQty;
        this.document.docNet -= m.movNet;
        this.document.docDiscount -= m.movDiscount;
        this.document.docSubtotal -= m.movSubtotal;
        this.document.docTax -= m.movTax;
        this.document.docTotal -= m.movTotal;
        break;
    }
  }

  closeDialog() {
    this.dialogRef.close(undefined);
  }

  cargarDoc() {
    this.docSer.obtenerDoc(this.data.invoice).subscribe(
      result => {
        this.document = result.body as DocumentDetailed;
      },
      (error: HttpErrorResponse) => {
        console.log(error);
        const errorData: ErrorData = error.error;
        Swal.fire("¡Error!", errorData.err + " : " + errorData.message, 'error');
      }
    )
  }

  cancelarText() {
    if (this.document.docCompleted) {
      return "Cancelar";
    } else {
      return "Eliminar";
    }
  }

  deleteDoc() {
    if (!this.document.docCompleted) {
      this.alerts.alertConfirmation(
        "Eliminación de Venta",
        "Favor de confirmar si desea eliminar la nota de venta #" + this.document.invoice,
        'warning',
        "Confirmar",
        "Cancelar"
      ).then(result => {
        if (result.value) {
          this.docService.deleteSell(new CancelDoc(this.document.invoice, this.document.clCode)).subscribe(
            () => {
              Swal.fire("Confirmado", "Documento de venta #" + this.document.invoice + " eliminado correctamente.", 'success').then(
                () => {
                  this.dialogRef.close(undefined);
                }
              );
            },
            (error: HttpErrorResponse) => {
              console.log(error);
              const errorData: ErrorData = error.error;
              Swal.fire("¡Error!", errorData.err + " : " + errorData.message, 'error');
            }
          );
        } else if (result.dismiss === Swal.DismissReason.cancel) {
          Swal.fire("Operación cancelada", "No se elimino el documento #" + this.document.invoice + ".", 'error');
        }
      })
    } else {
      this.alerts.alertConfirmation(
        "Cancelación de Venta",
        "Favor de confirmar si desea cancelar la nota de venta #" + this.document.invoice,
        'warning',
        "Confirmar",
        "Cancelar"
      ).then(result => {
        if (result.value) {
          this.docService.cancelSell(new CancelDoc(this.document.invoice, "")).subscribe(
            () => {
              Swal.fire("Confirmado", "Documento de venta #" + this.document.invoice + " cancelado correctamente.", 'success').then(
                () => {
                  this.dialogRef.close(undefined);
                }
              );
            },
            (error: HttpErrorResponse) => {
              console.log(error);
              const errorData: ErrorData = error.error;
              Swal.fire("¡Error!", errorData.err + " : " + errorData.message, 'error');
            }
          );
        } else if (result.dismiss === Swal.DismissReason.cancel) {
          Swal.fire("Operación cancelada", "No se elimino el documento #" + this.document.invoice + ".", 'error');
        }
      })
    }
  }
}
