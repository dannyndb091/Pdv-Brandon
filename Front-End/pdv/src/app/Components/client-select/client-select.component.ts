import { ChangeDetectorRef, Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { SelectClient } from 'src/app/DTOs/Clientes/select-client';
import { ClientsService } from 'src/app/Services/clients.service';
import * as $ from 'jquery';
import 'datatables.net';
import 'datatables.net-bs4';

@Component({
  selector: 'app-client-select',
  templateUrl: './client-select.component.html',
  styleUrls: ['./client-select.component.css']
})
export class ClientSelectComponent implements OnInit {
  dialog: any;
  clients: SelectClient[] | null = null;
  dataTable : any = null;

  constructor(public dialogRef: MatDialogRef<ClientSelectComponent>, private cliServ: ClientsService, private chRef: ChangeDetectorRef,
    @Inject(MAT_DIALOG_DATA) public data: number){ }

    ngOnInit(): void{
      console.log(this.data);
      switch(this.data){
        case 0:
          console.log("Cargar Clientes");
          this.cargarClientes();
          break;
        case 1:
          this.cargarProveedores();
          break;
      }
    }
  
  cargarClientes(){
    console.log("Inicia Cargar Clientes");
    this.cliServ.getToSell().subscribe(data => {
      this.clients = data.body;
      
      this.chRef.detectChanges();

      const table : any = $('#ClientsTable');
      this.dataTable = table.DataTable({
        lengthMenu: [15],
        pageLength: 15,
        order: [[0,'desc']],
        language: {
          "sProcessing":     "Procesando...",
          "sLengthMenu":     "Mostrar _MENU_ registros por pagina.",
          "sZeroRecords":    "No se encontraron registros.",
          "sInfo":           "Mostrando de _START_ a _END_ de _TOTAL_ registros.",
          "sInfoEmpty":      "Mostrando 0 de 0 de 0 registros.",
          "sInfoFiltered":   "(filtrado de _MAX_ registros totales)",
          "sInfoPostFix":    "",
          "sSearch":         "Buscar:",
          "sUrl":            "",
          "sInfoThousands":  ",",
          "sLoadingRecords": "Cargando...",
          "oPaginate": {
            "sFirst":    "Primero",
            "sLast":     "Último",
            "sNext":     "Siguiente",
            "sPrevious": "Anterior"
          },
          "oAria": {
            "sSortAscending":  ": Activar para ordenar la columna de manera ascendente",
            "sSortDescending": ": Activar para ordenar la columna de manera descendente"
          }
        }
      });             
    });
  }

  cargarProveedores(){

  }
  
  selectClient(cli: SelectClient){
    this.dialogRef.close(cli);
  }

  closeDialog() {
    this.dialogRef.close(undefined);
  }
}
