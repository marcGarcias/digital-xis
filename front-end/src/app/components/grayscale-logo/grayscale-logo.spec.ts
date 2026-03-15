import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GrayscaleLogo } from './grayscale-logo';

describe('GrayscaleLogo', () => {
  let component: GrayscaleLogo;
  let fixture: ComponentFixture<GrayscaleLogo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GrayscaleLogo]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GrayscaleLogo);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
