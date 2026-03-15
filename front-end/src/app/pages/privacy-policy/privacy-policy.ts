import { Component, OnInit } from '@angular/core';
import { Title, Meta } from '@angular/platform-browser';
import { TitlePrivacyPolicy } from "../../components/privacy-policy/title-privacy-policy/title-privacy-policy";
import { TextPrivacyPolicy } from "../../components/privacy-policy/text-privacy-policy/text-privacy-policy";

@Component({
  selector: 'app-privacy-policy',
  imports: [TitlePrivacyPolicy, TextPrivacyPolicy],
  templateUrl: './privacy-policy.html',
  styleUrl: './privacy-policy.css',
})
export class PrivacyPolicy implements OnInit {

  constructor(
    private titleService: Title,
    private metaService: Meta
  ) { }

  ngOnInit(): void {
    this.titleService.setTitle('Política de Privacidade | DigitalXis');

    this.metaService.updateTag({ name: 'description', content: 'Leia nossa política de privacidade e entenda como a DigitalXis coleta, usa e protege seus dados.' });
    this.metaService.updateTag({ name: 'robots', content: 'index, follow' });

    // Open Graph
    this.metaService.updateTag({ property: 'og:title', content: 'Política de Privacidade | DigitalXis' });
    this.metaService.updateTag({ property: 'og:description', content: 'Leia nossa política de privacidade e entenda como a DigitalXis coleta, usa e protege seus dados.' });
    this.metaService.updateTag({ property: 'og:url', content: 'https://digitalxis.com.br/politica-de-privacidade' });

    // Twitter
    this.metaService.updateTag({ name: 'twitter:title', content: 'Política de Privacidade | DigitalXis' });
    this.metaService.updateTag({ name: 'twitter:description', content: 'Leia nossa política de privacidade e entenda como a DigitalXis coleta, usa e protege seus dados.' });
  }

}
