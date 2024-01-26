import { ChangeDetectorRef, Component, ElementRef, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Docs } from 'src/app/DTOs/Documentos/docs';
import { DocumentsService } from 'src/app/Services/documents.service';
import * as $ from 'jquery';
import 'datatables.net';
import 'datatables.net-bs4';
import { AlertsService } from 'src/app/Services/alerts.service';
import { CancelDoc } from 'src/app/DTOs/Documentos/cancel-doc';
import { ErrorData } from 'src/app/DTOs/Otros/error-data';
import { HttpErrorResponse } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';
import { DocumentDetailedComponent } from '../document-detailed/document-detailed.component';
import { OpenDocument } from 'src/app/DTOs/Documentos/open-document';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-documents',
  templateUrl: './documents.component.html',
  styleUrls: ['./documents.component.css']
})

export class DocumentsComponent implements OnInit {
  type: number = 0;
  titleDoc: string = "";
  btnText: string = "";
  docs: Docs[] | null = null;
  dataTable: any = null;

  constructor(private route: ActivatedRoute, private el: ElementRef, private alerts: AlertsService,
    private docService: DocumentsService, private chRef: ChangeDetectorRef, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.type = this.route.snapshot.data['type'];
    switch (this.type) {
      case 0: {
        this.titleDoc = "Documentos de Compra";
        this.btnText = "Nueva Compra";
        this.loadDocs(0, false);
        break;
      }
      case 1: {
        this.titleDoc = "Documentos de Venta";
        this.btnText = "Nueva Venta";
        this.loadDocs(1, false);
        break;
      }
      case 2: {
        this.titleDoc = "Documentos de Merma";
        this.btnText = "Nueva Merma";
        this.loadDocs(2, false);
        break;
      }
    }
  }

  private loadDocs(mode: number, reload: boolean) {
    switch (mode) {
      case 1: {
        this.docService.obtenerVentas().subscribe(data => {
          this.docs = data.body;

          this.chRef.detectChanges();

          const table: any = $('#documentos');
          this.dataTable = table.DataTable({
            lengthMenu: [10, 25, 50, 100, 200],
            pageLength: 25,
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
        });
        break;
      }
      case 0: {
        this.docService.obtenerCompras().subscribe(data => {
          this.docs = data.body;

          this.chRef.detectChanges();

          const table: any = $('#documentos');
          this.dataTable = table.DataTable({
            lengthMenu: [10, 25, 50, 100, 200],
            pageLength: 25,
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
        });
        break;
      }
    }
  }

  badges(n: number) {
    switch (n) {
      case 0: return "badge badge-pill badge-warning";
      case 1: return "badge badge-pill badge-primary";
      case 2: return "badge badge-pill badge-danger";
    }
    return null;
  }

  nuevaVenta() {
    const data: OpenDocument = new OpenDocument(this.type, true, 0);
    let dialogRef = this.dialog.open(DocumentDetailedComponent, {
      minHeight: '800px',
      minWidth: '1000px',
      height: '90vh',
      width: '80vw',
      data: data,
    });

    dialogRef.afterClosed().subscribe(result => {
      $('#documentos').DataTable().destroy();
      this.loadDocs(1, true);
    });
  }

  cancelarVenta(invoice: number, status: number, clCode: string) {
    switch (status) {
      case 0: {
        this.alerts.alertConfirmation(
          "Eliminación de Venta",
          "Favor de confirmar si desea eliminar la nota de venta #" + invoice,
          'warning',
          "Confirmar",
          "Cancelar"
        ).then(result => {
          if (result.value) {
            this.docService.deleteSell(new CancelDoc(invoice, clCode)).subscribe(
              () => {
                $('#documentos').DataTable().destroy();
                this.loadDocs(1, true);
                Swal.fire("Confirmado", "Documento de venta #" + invoice + " eliminado correctamente.", 'success');
              },
              (error: HttpErrorResponse) => {
                console.log(error);
                const errorData: ErrorData = error.error;
                Swal.fire("¡Error!", errorData.err + " : " + errorData.message, 'error');
              }
            );
          } else if (result.dismiss === Swal.DismissReason.cancel) {
            Swal.fire("Operación cancelada", "No se elimino el documento #" + invoice + ".", 'error');
          }
        })
        break;
      }
      case 1: {
        this.alerts.alertConfirmation(
          "Cancelación de Venta",
          "Favor de confirmar si desea cancelar la nota de venta #" + invoice,
          'warning',
          "Confirmar",
          "Cancelar"
        ).then(result => {
          if (result.value) {
            this.docService.cancelSell(new CancelDoc(invoice, "")).subscribe(
              () => {
                $('#documentos').DataTable().destroy();
                this.loadDocs(1, true);
                Swal.fire("Confirmado", "Documento de venta #" + invoice + " cancelado correctamente.", 'success');
              },
              (error: HttpErrorResponse) => {
                console.log(error);
                const errorData: ErrorData = error.error;
                Swal.fire("¡Error!", errorData.err + " : " + errorData.message, 'error');
              }
            );
          } else if (result.dismiss === Swal.DismissReason.cancel) {
            Swal.fire("Operación cancelada", "No se elimino el documento #" + invoice + ".", 'error');
          }
        })
        break;
      }
    }
  }

  cerrarVenta(doc: Docs) {
    this.alerts.tinyAlert("Cerrar Venta");
  }

  modifyDoc(invoice: number) {
    const data: OpenDocument = new OpenDocument(this.type, false, invoice);
    let dialogRef = this.dialog.open(DocumentDetailedComponent, {
      minHeight: '800px',
      minWidth: '1000px',
      height: '90vh',
      width: '80vw',
      data: data,
    });

    dialogRef.afterClosed().subscribe(result => {
      $('#documentos').DataTable().destroy();
      this.loadDocs(1, true);
    });
  }
}
