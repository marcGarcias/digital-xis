// src/app/persistent/header/media-button/media-button.ts

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CMSService } from '../../services/CMSService';
import { Info } from '../../models/info.model';

@Component({
  selector: 'app-media-button',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './media-button.html',
  styleUrl: './media-button.css',
})
export class MediaButton implements OnInit {

  info: Info = {
    email: 'odigitalxis@gmail.com',
    cnpj: '62.829.941/0001-90',
    copyrightYear: '2026',
    whatsappRaw: '5511916544339',
    whatsappFormatted: '(11) 91654-4339',
    instagramFormatted: '@odigitalxis',
    instagramRaw: 'odigitalxis'
  };

  constructor(private cmsService: CMSService) {}

  ngOnInit(): void {
    this.cmsService
      .getSingleType<Info>('info')
      .subscribe(data => {
        if (data) {
          this.info = data;
        }
      });
  }

  get whatsappLink(): string {
    return this.info?.whatsappRaw ? `https://wa.me/${this.info.whatsappRaw}` : '#';
  }

  get instagramLink(): string {
    return this.info?.instagramRaw ? `https://www.instagram.com/${this.info.instagramRaw}` : '#';
  }
}