import { animate, style, transition, trigger } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [
    trigger('fadeInOut', [
      transition('void => *', [
        style({ opacity: 0 }),
        animate(2000, style({ opacity: 1 }))
      ]),
      transition('* => void', [
        animate(2000, style({ opacity: 0 }))
      ])
    ])
  ]
})
export class AppComponent {
  title = 'pdv';
  btnPressed : boolean[] = [false,false,false,false,false,false,false,false];
  intLastPressed : number = -1;

  onPressMenu(x : number){
    console.log(this.btnPressed);
    this.btnPressed[x] = true;
    if (this.intLastPressed >= 0) this.btnPressed[this.intLastPressed] = false;    
    this.intLastPressed = x;

    console.log(this.btnPressed);
  }
}
