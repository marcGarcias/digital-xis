import {
  Component,
  HostListener,
  OnInit,
  Inject,
  PLATFORM_ID,
  ChangeDetectorRef
} from '@angular/core';

import {
  CommonModule,
  isPlatformBrowser
} from '@angular/common';

import { CMSService } from '../../../services/CMSService';

interface ReviewModel {
  comment: string;
  userName: string;
}

interface DepoimentoDTO {
  name: string;
  comment: string;
}

@Component({
  selector: 'app-reviews',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reviews.html',
  styleUrls: ['./reviews.css'],
})
export class Review implements OnInit {

  constructor(
    private cmsService: CMSService,
    private cdr: ChangeDetectorRef,
    @Inject(PLATFORM_ID) private platformId: Object
  ) { }

  isDesktop = false;
  pageIndex = 0;
  itemsPerPage = 1;

  loading = true;

  reviews: ReviewModel[] = [];
  pages: ReviewModel[][] = [];
  duplicatedReviews: ReviewModel[] = [];

  selectedReview: ReviewModel | null = null;

  private touchStartX = 0;
  private touchEndX = 0;

  private navigationLocked = false;
  private readonly navigationDelay = 200;

  private fallbackReviews: ReviewModel[] = [
    {
      userName: 'Alex E Sara',
      comment: 'Trabalho e atendimento excepcional! Foi uma data muito importante e o que nos preocupava era as lembranças que ficariam através das fotos... Confiamos na pessoa certa! Em cada click foi demonstrado a leveza e delicadeza do nosso evento.'
    },
    {
      userName: 'Aline E Flávio',
      comment: 'Simplesmente amei, uma experiência incrível. Ali não são apenas fotos, são registros de muito carinho e amor.'
    },
    {
      userName: 'Andressa',
      comment: 'Foi muito bom! Ela me deixou muito segura e confortável.'
    },
    {
      userName: 'Danilo Catalano',
      comment: 'Foi muito importante para minha campanha de 2020. Todas as fotos foram feitas com grande profissionalismo.'
    },
    {
      userName: 'Danielle Lima',
      comment: 'Eu amei as fotos e amei mais ainda a sua entrega. Vc é uma profissional de mão cheia, super dedicada e caprichosa demais. Vc foca nos detalhes, fora a simpatia, leveza e a paciência. Obrigada!'
    }
  ];

  ngOnInit(): void {
    this.checkScreen();
    this.loadReviews();
  }

  @HostListener('window:resize')
  onResize(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.checkScreen();
      this.prepareLayout();
    }
  }

  @HostListener('document:keydown.escape')
  onEsc(): void {
    this.closeOverlay();
  }

  private checkScreen(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.isDesktop = window.innerWidth >= 1024;
    } else {
      this.isDesktop = false;
    }
  }

  private loadReviews(): void {
    this.cmsService
      .getCollection<DepoimentoDTO>('review')
      .subscribe({
        next: (data) => {
          this.reviews = !data || data.length === 0
            ? this.fallbackReviews
            : data.map(item => ({
              userName: item.name,
              comment: item.comment
            }));

          this.prepareLayout();
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.reviews = this.fallbackReviews;
          this.prepareLayout();
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
  }

  private prepareLayout(): void {
    if (this.isDesktop) {
      this.duplicatedReviews = [...this.reviews, ...this.reviews];
    } else {
      this.pages = [];
      for (let i = 0; i < this.reviews.length; i += this.itemsPerPage) {
        this.pages.push(this.reviews.slice(i, i + this.itemsPerPage));
      }
    }
    this.pageIndex = 0;
  }

  get transform(): string {
    return `translateX(-${this.pageIndex * 100}%)`;
  }

  private lockNavigation(): void {
    this.navigationLocked = true;
    setTimeout(() => {
      this.navigationLocked = false;
    }, this.navigationDelay);
  }

  next(): void {
    if (this.navigationLocked || this.pages.length === 0) return;
    this.lockNavigation();
    this.pageIndex = this.pageIndex < this.pages.length - 1
      ? this.pageIndex + 1
      : 0;
  }

  prev(): void {
    if (this.navigationLocked || this.pages.length === 0) return;
    this.lockNavigation();
    this.pageIndex = this.pageIndex > 0
      ? this.pageIndex - 1
      : this.pages.length - 1;
  }

  goToPage(index: number): void {
    if (this.navigationLocked) return;
    this.lockNavigation();
    this.pageIndex = index;
  }

  onTouchStart(event: TouchEvent): void {
    this.touchStartX = event.changedTouches[0].clientX;
  }

  onTouchMove(event: TouchEvent): void {
    this.touchEndX = event.changedTouches[0].clientX;
  }

  onTouchEnd(): void {
    const delta = this.touchStartX - this.touchEndX;
    if (Math.abs(delta) > 50) {
      delta > 0 ? this.next() : this.prev();
    }
  }

  openOverlay(review: ReviewModel): void {
    this.selectedReview = review;
    if (isPlatformBrowser(this.platformId)) {
      document.body.style.overflow = 'hidden';
    }
  }

  closeOverlay(): void {
    this.selectedReview = null;
    if (isPlatformBrowser(this.platformId)) {
      document.body.style.overflow = '';
    }
  }
}