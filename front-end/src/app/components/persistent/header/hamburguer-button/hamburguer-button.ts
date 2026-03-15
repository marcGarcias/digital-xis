import { Component, input, output } from '@angular/core';

@Component({
  selector: 'app-hamburger-button',
  standalone: true,
  template: `
    <button
      class="hamburguer-btn"
      [class.open]="open()"
      (click)="toggle.emit()"
      aria-label="Abrir menu"
      [attr.aria-expanded]="open()">
      <img src="assets/images/misc/menu-bar.svg" alt="" width="24" height="24">
    </button>
  `,
  styleUrl: './hamburguer-button.css',
})
export class HamburgerButton {
  open = input<boolean>(false);
  toggle = output<void>();
}
