import {
  Component,
  OnInit,
  OnDestroy,
  AfterViewInit,
  Inject,
  PLATFORM_ID,
  NgZone,
  ChangeDetectorRef
} from '@angular/core';

import {
  CommonModule,
  isPlatformBrowser
} from '@angular/common';

import { environment } from '../../../../environments/environment';
import { CMSService } from '../../../services/CMSService';

@Component({
  selector: 'app-carousel',
  standalone: true,
  templateUrl: './carousel.html',
  styleUrls: ['./carousel.css'],
  imports: [CommonModule],
})
export class Carousel implements OnInit, OnDestroy, AfterViewInit {

  startX = 0;
  readonly swipeThreshold = 50;

  currentIndex = 0;
  initialized = false;
  isLoading = true;
  speedTime = 3500;

  private intervalId?: number;
  private isBrowser: boolean;

  private readonly baseUrl = environment.serverUrl;

  slides: {
    desktop: string;
    mobile: string;
    altText: string;
  }[] = [];

  fallbackSlides = [
    {
      desktop: 'https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=1100&h=400&fit=crop&auto=format',
      mobile: 'https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=768&h=220&fit=crop&auto=format',
      altText: 'Paisagem deslumbrante de campo de flores amarelas'
    },
    {
      desktop: 'https://images.unsplash.com/photo-1579783902614-a3fb3927b6a5?w=1100&h=400&fit=crop&auto=format',
      mobile: 'https://images.unsplash.com/photo-1579783902614-a3fb3927b6a5?w=768&h=220&fit=crop&auto=format',
      altText: 'Obra de arte abstrata com tons vibrantes de amarelo'
    },
    {
      desktop: 'https://images.unsplash.com/photo-1447752875215-b2761acb3c5d?w=1100&h=400&fit=crop&auto=format',
      mobile: 'https://images.unsplash.com/photo-1447752875215-b2761acb3c5d?w=768&h=220&fit=crop&auto=format',
      altText: 'Floresta de outono com árvores e folhas amarelas deslumbrantes'
    },
    {
      desktop: 'https://images.unsplash.com/photo-1541701494587-cb58502866ab?w=1100&h=400&fit=crop&auto=format',
      mobile: 'https://images.unsplash.com/photo-1541701494587-cb58502866ab?w=768&h=220&fit=crop&auto=format',
      altText: 'Pintura fluida acrílica moderna em tons de amarelo'
    },
    {
      desktop: 'https://images.unsplash.com/photo-1433832597046-4f10e10ac764?w=1100&h=400&fit=crop&auto=format',
      mobile: 'https://images.unsplash.com/photo-1433832597046-4f10e10ac764?w=768&h=220&fit=crop&auto=format',
      altText: 'Campos amarelos da Toscana sob o sol'
    },
    {
      desktop: 'https://images.unsplash.com/photo-1580136579312-94651dfd596d?w=1100&h=400&fit=crop&auto=format',
      mobile: 'https://images.unsplash.com/photo-1580136579312-94651dfd596d?w=768&h=220&fit=crop&auto=format',
      altText: 'Detalhe de pintura clássica com pigmentos amarelos'
    }
  ];

  constructor(
    @Inject(PLATFORM_ID) platformId: Object,
    private zone: NgZone,
    private cdr: ChangeDetectorRef,
    private cmsService: CMSService
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
  }

  ngOnInit(): void {
    this.useFallback();
  }

  useFallback() {

    this.isLoading = false;
    this.cdr.markForCheck();

    this.slides =
      this.fallbackSlides;

    if (this.isBrowser) {
      this.startAutoPlay();
    }

  }

  ngAfterViewInit(): void {

    if (this.isBrowser) {

      setTimeout(() => {

        this.initialized = true;

        this.cdr.markForCheck();

      });

    }

  }

  ngOnDestroy(): void {

    this.stopAutoPlay();

  }

  startAutoPlay(): void {

    this.zone.runOutsideAngular(() => {

      this.intervalId =
        window.setInterval(() => {

          this.zone.run(() =>
            this.next()
          );

        }, this.speedTime);

    });

  }

  stopAutoPlay(): void {

    if (this.intervalId !== undefined) {

      clearInterval(
        this.intervalId
      );

      this.intervalId =
        undefined;

    }

  }

  next(): void {

    if (!this.slides.length)
      return;

    this.currentIndex =
      (this.currentIndex + 1)
      % this.slides.length;

    this.cdr.markForCheck();

  }

  prev(): void {

    if (!this.slides.length)
      return;

    this.currentIndex =
      (
        this.currentIndex - 1
        + this.slides.length
      )
      % this.slides.length;

    this.cdr.markForCheck();

  }

  goTo(index: number): void {

    this.currentIndex = index;

  }

  onPointerDown(
    event: PointerEvent
  ): void {

    this.startX =
      event.clientX;

  }

  onPointerUp(
    event: PointerEvent
  ): void {

    const deltaX =
      event.clientX
      - this.startX;

    if (
      Math.abs(deltaX)
      > this.swipeThreshold
    ) {

      deltaX < 0
        ? this.next()
        : this.prev();

    }

  }

}