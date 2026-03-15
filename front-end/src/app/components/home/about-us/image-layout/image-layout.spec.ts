import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImageLayout } from './image-layout';

describe('ImageLayout', () => {
  let component: ImageLayout;
  let fixture: ComponentFixture<ImageLayout>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ImageLayout]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ImageLayout);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
