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
  isPlatformBrowser,
  NgOptimizedImage
} from '@angular/common';

import { environment } from '../../../../environments/environment';
import { CMSService } from '../../../services/CMSService';

@Component({
  selector: 'app-carousel',
  standalone: true,
  templateUrl: './carousel.html',
  styleUrls: ['./carousel.css'],
  imports: [CommonModule, NgOptimizedImage],
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
      desktop: 'https://picsum.photos/1100/309.webp?random=9',
      mobile: 'https://picsum.photos/375/106.webp?random=10',
      altText: ''
    },
    {
      desktop: 'https://picsum.photos/1100/309.webp?random=11',
      mobile: 'https://picsum.photos/375/106.webp?random=12',
      altText: ''
    },
    {
      desktop: 'https://picsum.photos/1100/309.webp?random=13',
      mobile: 'https://picsum.photos/375/106.webp?random=14',
      altText: ''
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

    this.cmsService
      .getSingleType<any>('carousel')
      .subscribe({

        next: (data) => {

          if (!data) {
            this.useFallback();
            return;
          }

          const desktop =
            data.desktopImages ?? [];

          const mobile =
            data.mobileImages ?? [];

          const total =
            Math.min(
              desktop.length,
              mobile.length
            );

          this.slides = [];

          for (let i = 0; i < total; i++) {

            const d = desktop[i];
            const m = mobile[i];

            if (!d?.url || !m?.url)
              continue;

            this.slides.push({

              desktop:
                d.url.startsWith('http') ? d.url : this.baseUrl + d.url,

              mobile:
                m.url.startsWith('http') ? m.url : this.baseUrl + m.url,

              altText:
                d.alternativeText || m.alternativeText || ''

            });

          }

          if (!this.slides.length) {
            this.useFallback();
            return;
          }

          this.isLoading = false;
          this.cdr.markForCheck();

          if (this.isBrowser) {
            this.startAutoPlay();
          }

        },

        error: () => {

          this.useFallback();

        }

      });

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

  isInitialPriority(
    index: number
  ): boolean {

    return (
      index === 0
      && !this.initialized
    );

  }

  getLoadingStrategy(
    index: number
  ): 'lazy'
    | 'eager'
    | null {

    if (!this.initialized)
      return null;

    if (

      index ===
      this.currentIndex ||

      index ===
      this.currentIndex - 1 ||

      index ===
      this.currentIndex + 1

    ) {

      return 'eager';

    }

    return 'lazy';

  }

  isPriority(
    index: number
  ): boolean {

    const total =
      this.slides.length;

    const prev =
      (
        this.currentIndex - 1
        + total
      ) % total;

    const next =
      (
        this.currentIndex + 1
      ) % total;

    return (

      index ===
      this.currentIndex ||

      index === prev ||

      index === next

    );

  }

}