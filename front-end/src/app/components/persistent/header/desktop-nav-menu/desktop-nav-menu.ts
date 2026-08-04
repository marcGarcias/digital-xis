import { Component, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-desktop-nav-menu',
  standalone: true,
  templateUrl: './desktop-nav-menu.html',
  styleUrl: './desktop-nav-menu.css',
})
export class DesktopNavMenu {

  @Output() scroll = new EventEmitter<string>();

  scrollTo(section: string) {
    this.scroll.emit(section);
  }
}