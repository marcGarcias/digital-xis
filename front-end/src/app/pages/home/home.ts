import { Component, OnInit } from '@angular/core';
import { Title, Meta } from '@angular/platform-browser';
import { Carousel } from "../../components/home/carousel/carousel";
import { AboutUs } from "../../components/home/about-us/about-us";
import { FaqComponent } from "../../components/home/faq/faq";
import { Review } from "../../components/home/reviews/reviews";
import { FormComponent } from "../../components/home/form/form";
import { Slogan } from "../../components/home/slogan/slogan";

@Component({
  selector: 'app-home',
  imports: [Carousel, AboutUs, FaqComponent, Review, FormComponent, Slogan],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {

  constructor(
    private titleService: Title,
    private metaService: Meta
  ) {}

  ngOnInit(): void {
    this.titleService.setTitle('DigitalXis | Marketing Digital & Gestão de Redes Sociais');

    this.metaService.updateTag({ name: 'description', content: 'Agência de marketing 100% digital. Gestão de redes sociais, tráfego pago, identidade visual e criação de conteúdo estratégico para crescer sua marca online.' });
    this.metaService.updateTag({ name: 'robots', content: 'index, follow' });

    // Open Graph
    this.metaService.updateTag({ property: 'og:title', content: 'DigitalXis | Marketing Digital & Gestão de Redes Sociais' });
    this.metaService.updateTag({ property: 'og:description', content: 'Agência de marketing 100% digital. Gestão de redes sociais, tráfego pago, identidade visual e criação de conteúdo estratégico.' });
    this.metaService.updateTag({ property: 'og:url', content: 'https://digitalxis.com.br/' });

    // Twitter
    this.metaService.updateTag({ name: 'twitter:title', content: 'DigitalXis | Marketing Digital & Gestão de Redes Sociais' });
    this.metaService.updateTag({ name: 'twitter:description', content: 'Agência de marketing 100% digital. Gestão de redes sociais, tráfego pago, identidade visual e criação de conteúdo estratégico.' });
  }

}
