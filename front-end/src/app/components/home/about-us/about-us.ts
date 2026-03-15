import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ImageLayout } from './image-layout/image-layout';
import { CMSService } from '../../../services/CMSService';
import { CommonModule } from '@angular/common';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-about-us',
  standalone: true,
  imports: [ImageLayout, CommonModule],
  templateUrl: './about-us.html',
  styleUrls: ['./about-us.css'],
})
export class AboutUs implements OnInit {

  aboutText = '';

  imagens: Array<{
    desktop: string;
    mobile: string;
    altText: string;
  }> = [];

  loading = true;

  private readonly baseUrl = environment.serverUrl;

  private readonly fallbackText = `
  <p>
    Somos uma <strong>empresa de Marketing Digital e Gestão de Redes Sociais</strong> <strong>100% digital</strong>, especializada em <strong>desenvolver marcas no ambiente online</strong>. Atuamos com <strong>estratégias de criação de conteúdo</strong>, <strong>planejamento</strong> e <strong>gestão de mídias sociais</strong> para posicionar sua empresa na vitrine mais poderosa da atualidade: <strong>a internet</strong>.
  </p>
  <br>
  <p>
    Trabalhamos com foco em <strong>performance</strong>, <strong>posicionamento de marca</strong> e <strong>crescimento sustentável</strong>, utilizando <strong>estratégias orientadas por dados</strong> para aumentar <strong>alcance</strong>, <strong>engajamento</strong> e <strong>conversão</strong>. Nosso objetivo é transformar sua <strong>presença digital</strong> em <strong>autoridade</strong> e <strong>resultados reais</strong>.
  </p>
  <br>
  <p>
    Nossa equipe é formada por <strong>profissionais qualificados</strong> em <strong>marketing estratégico</strong>, <strong>branding</strong> e <strong>comunicação digital</strong>, preparados para construir não apenas <strong>conteúdos visuais</strong>, mas uma <strong>identidade forte e autêntica</strong> para sua marca. Desenvolvemos uma <strong>comunicação clara</strong> e direcionada ao seu <strong>público-alvo</strong>, fortalecendo seu <strong>relacionamento com o mercado</strong>.
  </p>
  `;

  private fallbackImages = [
    {
      desktop: 'assets/images/fallbacks/about-us/desktop/esquerda_superior.webp',
      mobile: 'assets/images/fallbacks/about-us/mobile/esquerda_superior.webp',
      altText: ''
    },
    {
      desktop: 'assets/images/fallbacks/about-us/desktop/esquerda_inferior.webp',
      mobile: 'assets/images/fallbacks/about-us/mobile/esquerda_inferior.webp',
      altText: ''
    },
    {
      desktop: 'assets/images/fallbacks/about-us/desktop/direita_vertical.webp',
      mobile: 'assets/images/fallbacks/about-us/mobile/direita_vertical.webp',
      altText: ''
    },
    {
      desktop: 'assets/images/fallbacks/about-us/desktop/inferior_horizontal.webp',
      mobile: 'assets/images/fallbacks/about-us/mobile/inferior_horizontal.webp',
      altText: ''
    }
  ];

  constructor(
    private cmsService: CMSService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.imagens = [...this.fallbackImages];
    this.loading = true;

    this.cmsService.getSingleType('about')
      .subscribe({
        next: (data: any) => {
          this.aboutText = data?.description || this.fallbackText;

          const imageKeys = [
            { desktop: 'topLeftImageDesktop', mobile: 'topLeftImageMobile' },
            { desktop: 'bottomLeftImageDesktop', mobile: 'bottomLeftImageMobile' },
            { desktop: 'rightImageDesktop', mobile: 'rightImageMobile' },
            { desktop: 'bottomImageDesktop', mobile: 'bottomImageMobile' },
          ];

          imageKeys.forEach(({ desktop: dk, mobile: mk }, i) => {
            const d = data?.[dk];
            const m = data?.[mk];

            if (d?.url || m?.url) {
              this.imagens[i] = {
                desktop: d?.url
                  ? (d.url.startsWith('http') ? d.url : this.baseUrl + d.url)
                  : this.fallbackImages[i].desktop,
                mobile: m?.url
                  ? (m.url.startsWith('http') ? m.url : this.baseUrl + m.url)
                  : this.fallbackImages[i].mobile,
                altText: d?.alternativeText || m?.alternativeText || this.fallbackImages[i].altText
              };
            }
          });

          this.loading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.aboutText = this.fallbackText;
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
  }
}