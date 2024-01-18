import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.css']
})
export class ClientsComponent {
  type : number = 0;

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.type = this.route.snapshot.data['type'];
  }
}
