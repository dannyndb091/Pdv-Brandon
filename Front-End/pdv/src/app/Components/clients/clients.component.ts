import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.css']
})
export class ClientsComponent implements OnInit {
  type : number = 0;
  titleDoc : string = "";
  btnText : string = ""; 

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.type = this.route.snapshot.data['type'];
    switch (this.type){
      case 0:{
        this.titleDoc = "Clientes";
        this.btnText = "Nuevo Cliente";
        break;
      }
      case 1:{
        this.titleDoc = "Proveedores";
        this.btnText = "Nuevo Proveedor";
        break;
      }
    }
  }
}
