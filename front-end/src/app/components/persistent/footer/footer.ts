import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { GrayscaleLogo } from '../../grayscale-logo/grayscale-logo';
import { CMSService } from '../../../services/CMSService';
import { Info } from '../../../models/info.model';

@Component({
  selector: 'app-footer',
  imports: [GrayscaleLogo],
  templateUrl: './footer.html',
  styleUrl: './footer.css',
})
export class Footer implements OnInit {

  info: Info = {
    email: 'odigitalxis@gmail.com',
    cnpj: '62.829.941/0001-90',
    copyrightYear: '2026',
    whatsappRaw: '5511916544339',
    whatsappFormatted: '(11) 91654-4339',
    instagramFormatted: '@odigitalxis',
    instagramRaw: 'odigitalxis'
  };

  constructor(
    private cmsService: CMSService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.cmsService
      .getSingleType<Info>('info')
      .subscribe(data => {
        if (!data) return;

        this.info = {
          email: data.email || this.info.email,
          cnpj: this.formatCnpj(data.cnpj) || this.info.cnpj,
          copyrightYear: data.copyrightYear || this.info.copyrightYear,
          whatsappRaw: data.whatsappRaw || this.info.whatsappRaw,
          whatsappFormatted: data.whatsappFormatted || this.info.whatsappFormatted,
          instagramFormatted: data.instagramFormatted || this.info.instagramFormatted,
          instagramRaw: data.instagramRaw || this.info.instagramRaw,
        };

        this.cdr.detectChanges();
      });
  }

  private formatCnpj(cnpj: string): string {
    if (!cnpj) return '';
    const digits = cnpj.replace(/\D/g, '');
    if (digits.length !== 14) return cnpj;
    return digits.replace(
      /^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})$/,
      '$1.$2.$3/$4-$5'
    );
  }
}