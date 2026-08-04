import { Component, OnInit } from '@angular/core';
import { CMSService } from '../../../services/CMSService';
import { Info } from '../../../models/info.model';

@Component({
  selector: 'app-text-privacy-policy',
  standalone: true,
  imports: [],
  templateUrl: './text-privacy-policy.html',
  styleUrl: './text-privacy-policy.css',
})
export class TextPrivacyPolicy implements OnInit {

  email: string = 'contato@dxis.com.br'; // fallback

  constructor(
    private cmsService: CMSService
  ) { }

  ngOnInit(): void {

    this.cmsService
      .getSingleType<Info>('info')
      .subscribe(data => {

        if (data && data.email) {

          this.email = data.email;

        }

      });

  }

}