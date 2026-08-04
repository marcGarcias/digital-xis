import { Component, HostListener } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';

import { HamburgerButton } from "./hamburguer-button/hamburguer-button";
import { DesktopNavMenu } from "./desktop-nav-menu/desktop-nav-menu";
import { ColorizedLogo } from "../../colorized-logo/colorized-logo";
import { MediaButton } from '../../media-button/media-button';
import { MobileNavMenu } from "./mobile-nav-menu/mobile-nav-menu";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    HamburgerButton,
    DesktopNavMenu,
    ColorizedLogo,
    MediaButton,
    MobileNavMenu
  ],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {

  isMenuOpen = false;
  isHome = false;

  constructor(private router: Router) {
    this.checkRoute();

    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => this.checkRoute());
  }

private checkRoute(): void {
  this.isHome = this.router.url === '/' || this.router.url === '';
}

  goHome(): void {
    if (!this.isHome) {
      this.router.navigate(['/']);
    }
  }

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
    document.body.style.overflow = this.isMenuOpen ? 'hidden' : '';
  }

  closeMenu(): void {
    this.isMenuOpen = false;
    document.body.style.overflow = '';
  }

  handleScroll(section: string): void {
    if (!this.isHome) return;
    this.scrollTo(section);
    this.closeMenu();
  }

  scrollTo(id: string): void {
    const element = document.getElementById(id);
    if (!element) return;

    const headerOffset = 64;
    const elementPosition =
      element.getBoundingClientRect().top + window.scrollY;

    const offsetPosition = elementPosition - headerOffset;

    window.scrollTo({
      top: offsetPosition,
      behavior: 'smooth'
    });
  }

  @HostListener('window:resize')
  onResize(): void {
    if (window.innerWidth >= 850 && this.isMenuOpen) {
      this.closeMenu();
    }
  }
}