import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ColorizedLogo } from './colorized-logo';

describe('ColorizedLogo', () => {
  let component: ColorizedLogo;
  let fixture: ComponentFixture<ColorizedLogo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ColorizedLogo]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ColorizedLogo);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
