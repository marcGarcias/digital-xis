import { Component, output } from '@angular/core';
import { MediaButton } from "../../../media-button/media-button";

@Component({
  selector: 'app-mobile-nav-menu',
  standalone: true,
  imports: [MediaButton],
  template: `
    <div class="mobile-menu-header">
      <span class="title">
        <app-media-button></app-media-button>
      </span>

      <button class="close-btn" (click)="close.emit()"></button>
    </div>

    <ul class="mobile-menu-list">
      <li (click)="onItemClick('inicio')"><a href="#inicio" (click)="$event.preventDefault()">INICIO</a></li>
      <li (click)="onItemClick('sobre')"><a href="#sobre" (click)="$event.preventDefault()">SOBRE NÓS</a></li>
      <li (click)="onItemClick('depoimentos')"><a href="#depoimentos" (click)="$event.preventDefault()">DEPOIMENTOS</a></li>
      <li (click)="onItemClick('contato')"><a href="#contato" (click)="$event.preventDefault()">CONTATO</a></li>
      <li (click)="onItemClick('faq')"><a href="#faq" (click)="$event.preventDefault()">FAQ</a></li>
    </ul>
  `,
  styleUrl: './mobile-nav-menu.css',
})
export class MobileNavMenu {

  close = output<void>();
  scroll = output<string>();

  onItemClick(section: string) {
    this.scroll.emit(section);
    setTimeout(() => this.close.emit(), 120);
  }
}