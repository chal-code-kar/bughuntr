import { Component, OnInit, Input, HostBinding } from '@angular/core';
import { Router } from '@angular/router';
import {animate, state, style, transition, trigger} from '@angular/animations';
import { Globals } from '../globals';

@Component({
  selector: 'app-mobile-menu',
  templateUrl: './mobile-menu.component.html',
  styleUrls: ['./mobile-menu.component.css'],
  animations: [
    trigger('indicatorRotate', [
      state('collapsed', style({transform: 'rotate(0deg)'})),
      state('expanded', style({transform: 'rotate(180deg)'})),
      transition('expanded <=> collapsed',
        animate('225ms cubic-bezier(0.4,0.0,0.2,1)')
      ),
    ])
  ]
})
export class MobileMenuComponent implements OnInit {

  expanded: boolean = false;
  @HostBinding('attr.aria-expanded') ariaExpanded = this.expanded;
  @Input() item: any;
  @Input() depth: number;
  constructor(public router: Router,private globals: Globals) {
if (this.depth === undefined) {
this.depth = 0;
}
}

onItemSelected(item: any) {
if (!item.subMenus || !item.subMenus.length) {
const url = item.url;
if (url && typeof url === 'string' && url.startsWith('/') && !url.startsWith('//') && !url.includes('://')) {
  this.router.navigate([url]);
} else {
  this.router.navigate(['/']);
}
this.globals.view=!this.globals.view;
}
if (item.subMenus && item.subMenus.length) {
this.expanded = !this.expanded;
}
}

ngOnInit(){}


}
