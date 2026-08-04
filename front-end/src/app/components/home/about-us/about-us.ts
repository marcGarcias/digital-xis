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

  <p>
    Trabalhamos com foco em <strong>performance</strong>, <strong>posicionamento de marca</strong> e <strong>crescimento sustentável</strong>, utilizando <strong>estratégias orientadas por dados</strong> para aumentar <strong>alcance</strong>, <strong>engajamento</strong> e <strong>conversão</strong>. Nosso objetivo é transformar sua <strong>presença digital</strong> em <strong>autoridade</strong> e <strong>resultados reais</strong>.
  </p>
  <br>
  <p>
    Nossa equipe é formada por <strong>profissionais qualificados</strong> em <strong>marketing estratégico</strong>, <strong>branding</strong> e <strong>comunicação digital</strong>, preparados para construir não apenas <strong>conteúdos visuais</strong>, mas uma <strong>identidade forte e autêntica</strong> para sua marca. Desenvolvemos uma <strong>comunicação clara</strong> e direcionada ao seu <strong>público-alvo</strong>, fortalecendo seu <strong>relacionamento com o mercado</strong>.
  </p>
  `;

  private fallbackImages = [
    // Esquerda superior — Canola field landscape
    {
      desktop: 'https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=450&h=315&fit=crop&auto=format',
      mobile: 'https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=320&h=166&fit=crop&auto=format',
      altText: 'Lindo campo de flores amarelas'
    },
    // Esquerda inferior — Abstract oil paint art
    {
      desktop: 'https://images.unsplash.com/photo-1579783928621-7a13d66a62d1?w=450&h=315&fit=crop&auto=format',
      mobile: 'https://images.unsplash.com/photo-1579783928621-7a13d66a62d1?w=320&h=166&fit=crop&auto=format',
      altText: 'Textura artística de tinta a óleo amarela'
    },
    // Direita vertical — Autumn leaves path landscape
    {
      desktop: 'https://images.unsplash.com/photo-1447752875215-b2761acb3c5d?w=450&h=638&fit=crop&auto=format',
      mobile: 'https://images.unsplash.com/photo-1447752875215-b2761acb3c5d?w=320&h=351&fit=crop&auto=format',
      altText: 'Caminho em floresta com folhas amarelas de outono'
    },
    // Inferior horizontal — Fluid yellow acrylic art
    {
      desktop: 'https://images.unsplash.com/photo-1541701494587-cb58502866ab?w=900&h=500&fit=crop&auto=format',
      mobile: 'https://images.unsplash.com/photo-1541701494587-cb58502866ab?w=700&h=203&fit=crop&auto=format',
      altText: 'Pintura moderna acrílica em tons amarelos'
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