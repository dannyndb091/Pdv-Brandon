import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-documents',
  templateUrl: './documents.component.html',
  styleUrls: ['./documents.component.css']
})
export class DocumentsComponent implements OnInit {
  type : number = 0;
  titleDoc : string = "";
  btnText : string = ""; 

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.type = this.route.snapshot.data['type'];
    switch (this.type){
      case 0:{
        this.titleDoc = "Documentos de Compra";
        this.btnText = "Nueva Compra";
        break;
      }
      case 1:{
        this.titleDoc = "Documentos de Venta";
        this.btnText = "Nueva Venta";
        break;
      }
      case 2:{
        this.titleDoc = "Documentos de Merma";
        this.btnText = "Nueva Merma";
        break;
      }
    }
  }
}
