import { Component, OnInit, AfterViewInit, OnDestroy } from '@angular/core';
import {
  FormBuilder,
  Validators,
  ReactiveFormsModule,
  FormGroup
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { MediaButton } from "../../media-button/media-button";

import { environment } from '../../../../environments/environment';

declare var turnstile: any;

@Component({
  selector: 'app-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule, MediaButton],
  templateUrl: './form.html',
  styleUrl: './form.css'
})
export class FormComponent implements OnInit, AfterViewInit, OnDestroy {

  contactForm!: FormGroup;

  isSubmitting = false;
  submitStatus: 'idle' | 'success' | 'error' = 'idle';
  loading = true;
  private widgetId: string | null = null;

  private readonly FEEDBACK_DELAY = 5000;

  subjects = [
    'Gestão de redes sociais',
    'Tráfego pago',
    'Identidade visual',
    'Consultoria',
    'Gestão de e-commerce',
    'Outro'
  ];

  sources = [
    'Instagram',
    'Google',
    'Indicação',
    'Outro'
  ];

  constructor(
    private fb: FormBuilder,
    private http: HttpClient
  ) {

    this.contactForm = this.fb.group({
      name: ['', Validators.required],
      company: [''],
      noCompany: [false],

      email: ['', [Validators.required, Validators.email]],

      phone: [
        '',
        [
          Validators.required,
          Validators.pattern(/^\(?\d{2}\)?\s?\d{4,5}-?\d{4}$/)
        ]
      ],

      instagram: [''],
      noInstagram: [false],

      subject: ['', Validators.required],
      source: ['', Validators.required],
      message: ['', Validators.required],
      privacyAccepted: [false, Validators.requiredTrue],
      captchaToken: ['', Validators.required]
    });

    this.setupCompanyToggle();
    this.setupInstagramToggle();
  }

  ngOnInit(): void {
    this.loading = false;
  }

  ngAfterViewInit(): void {
    this.renderTurnstile();
  }

  ngOnDestroy(): void {
    if (this.widgetId) {
      turnstile.remove(this.widgetId);
    }
  }

  private renderTurnstile() {
    if (typeof turnstile !== 'undefined') {
      this.widgetId = turnstile.render('#turnstile-container', {
        sitekey: environment.turnstileSiteKey,
        callback: (token: string) => {
          this.contactForm.get('captchaToken')?.setValue(token);
        },
        'error-callback': (error: any) => {
          console.error('Turnstile Error:', error);
          this.submitStatus = 'error';
        },
        'expired-callback': () => {
          this.contactForm.get('captchaToken')?.setValue('');
          turnstile.reset(this.widgetId);
        }
      });
    } else {
      // Tentar novamente em 1 segundo se o script ainda não carregou
      setTimeout(() => this.renderTurnstile(), 1000);
    }
  }

  private setupCompanyToggle() {
    const noCompany = this.contactForm.get('noCompany');
    const company = this.contactForm.get('company');

    noCompany?.valueChanges.subscribe(value => {
      if (value) {
        company?.reset();
        company?.disable();
      } else {
        company?.enable();
      }
    });
  }

  private setupInstagramToggle() {
    const noInstagram = this.contactForm.get('noInstagram');
    const instagram = this.contactForm.get('instagram');

    noInstagram?.valueChanges.subscribe(value => {
      if (value) {
        instagram?.reset();
        instagram?.disable();
      } else {
        instagram?.enable();
      }
    });
  }

  private formatName(name: string): string {
    return name
      .trim()
      .toLowerCase()
      .split(' ')
      .filter(Boolean)
      .map(word => word[0].toUpperCase() + word.slice(1))
      .join(' ');
  }

  private formatEmail(email: string): string {
    return email.trim().toLowerCase();
  }

  private formatPhone(phone: string): string {
    return phone.replace(/\D/g, '');
  }

  get nameInvalid() {
    const control = this.contactForm.get('name');
    return control?.invalid && control?.touched;
  }

  get emailInvalid() {
    const control = this.contactForm.get('email');
    return control?.invalid && control?.touched;
  }

  get phoneInvalid() {
    const control = this.contactForm.get('phone');
    return control?.invalid && control?.touched;
  }

  get messageInvalid() {
    const control = this.contactForm.get('message');
    return control?.invalid && control?.touched;
  }

  get privacyInvalid() {
    const control = this.contactForm.get('privacyAccepted');
    return control?.invalid && control?.touched;
  }

  onSubmit() {
    if (this.contactForm.invalid || this.isSubmitting) {
      this.contactForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.submitStatus = 'idle';

    const raw = this.contactForm.getRawValue();

    const payload = {
      ...raw,
      name: this.formatName(raw.name),
      email: this.formatEmail(raw.email),
      phone: this.formatPhone(raw.phone)
    };

    this.http.post(`${environment.apiUrl}/contact`, payload)
      .subscribe({
        next: () => {
          this.isSubmitting = false;
          this.submitStatus = 'success';
          this.contactForm.reset();
          if (this.widgetId) {
            turnstile.reset(this.widgetId);
          }

          setTimeout(() => {
            this.submitStatus = 'idle';
          }, this.FEEDBACK_DELAY);
        },
        error: () => {
          this.isSubmitting = false;
          this.submitStatus = 'error';
          if (this.widgetId) {
            turnstile.reset(this.widgetId);
          }

          setTimeout(() => {
            this.submitStatus = 'idle';
          }, this.FEEDBACK_DELAY);
        }
      });
  }
}
