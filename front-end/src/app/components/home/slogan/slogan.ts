import { Component } from '@angular/core';

@Component({
  selector: 'app-slogan',
  imports: [],
  templateUrl: './slogan.html',
  styleUrl: './slogan.css',
})
export class Slogan {

  scrollToSection(event: Event, id: string): void {
    event.preventDefault();
    const element = document.getElementById(id);
    if (!element) return;
    const headerOffset = 76;
    const elementPosition = element.getBoundingClientRect().top + window.scrollY;
    window.scrollTo({ top: elementPosition - headerOffset, behavior: 'smooth' });
  }

}
