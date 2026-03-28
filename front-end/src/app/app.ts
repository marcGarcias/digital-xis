import { Component, signal, Renderer2, Inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { environment } from '../environments/environment';
import { Header } from './components/persistent/header/header';
import { RouterOutlet } from '@angular/router';
import { Footer } from './components/persistent/footer/footer';
import { ScrollService } from './services/ScrollService';

@Component({
  selector: 'app-root',
  imports: [Header, RouterOutlet, Footer],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('front-end');

  constructor(
    private scrollService: ScrollService,
    private renderer: Renderer2,
    @Inject(DOCUMENT) private document: Document
  ) { }

  ngOnInit(): void {
    this.scrollService.init();

    if (environment.googleAnalyticsId) {
      const gtagScript = this.renderer.createElement('script');
      gtagScript.async = true;
      gtagScript.src = `https://www.googletagmanager.com/gtag/js?id=${environment.googleAnalyticsId}`;
      this.renderer.appendChild(this.document.head, gtagScript);

      const gtagInitScript = this.renderer.createElement('script');
      gtagInitScript.text = `
        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());
        gtag('config', '${environment.googleAnalyticsId}');
      `;
      this.renderer.appendChild(this.document.head, gtagInitScript);
    }

    console.log(
      "%cDeveloped by Marcelo Garcias \n%chttps://marcelogarcias.dev.br\n%cdev.garcias@proton.me",
      "font-size:14px;font-weight:bold;color:#c9a84c;",
      "color:#888;",
      "color:#888;"
    );
  }

}
