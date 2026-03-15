import { Injectable, Inject, PLATFORM_ID, NgZone } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import Lenis from 'lenis';

@Injectable({
  providedIn: 'root'
})
export class ScrollService {
  private lenis: Lenis | null = null;

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private ngZone: NgZone
  ) {}

  init(): void {
    if (!isPlatformBrowser(this.platformId)) return;

    this.ngZone.runOutsideAngular(() => {
      this.lenis = new Lenis({
        duration: 1.2,
        easing: (t) => Math.min(1, 1.001 - Math.pow(2, -10 * t)),
        orientation: 'vertical',
        gestureOrientation: 'vertical',
        smoothWheel: true,
        wheelMultiplier: 1,
        touchMultiplier: 2,
        infinite: false,
      });

      const raf = (time: number) => {
        this.lenis?.raf(time);
        requestAnimationFrame(raf);
      };

      requestAnimationFrame(raf);
    });
  }

  get instance(): Lenis | null {
    return this.lenis;
  }

  scrollTo(target: string | HTMLElement, options?: any): void {
    this.lenis?.scrollTo(target, options);
  }

  destroy(): void {
    this.lenis?.destroy();
    this.lenis = null;
  }
}
